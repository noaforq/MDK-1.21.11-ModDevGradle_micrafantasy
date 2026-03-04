package cloud.laboratory.n.micrafantasy.event;

import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.config.MicrafantasyConfig;
import cloud.laboratory.n.micrafantasy.job.JobData;
import cloud.laboratory.n.micrafantasy.job.JobType;
import cloud.laboratory.n.micrafantasy.network.ModNetwork;
import cloud.laboratory.n.micrafantasy.network.packet.SyncJobDataPayload;
import cloud.laboratory.n.micrafantasy.registry.ModAttachmentTypes;
import cloud.laboratory.n.micrafantasy.registry.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * デバッグモード用イベントハンドラ
 *
 * debug_mode=true のとき、ログイン時に以下を実行する：
 *   1. すべてのジョブストーンを配布（未所持の場合のみ）
 *   2. すべてのジョブのレベルを debug_job_level に設定
 *   3. すべてのジョブのカレント経験値を 0 にリセット
 *
 * ログイン直後はインベントリが未ロードの可能性があるため 20tick 後に処理する。
 */
@EventBusSubscriber(modid = MicrafantasyMod.MODID)
public class DebugEventHandler {

    private static final Map<UUID, Integer> pendingGive = new HashMap<>();
    private static final int GIVE_DELAY_TICKS = 20;

    /** サーバーtick：遅延処理 */
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (pendingGive.isEmpty()) return;

        Map<UUID, Integer> copy = new HashMap<>(pendingGive);
        for (Map.Entry<UUID, Integer> entry : copy.entrySet()) {
            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
                pendingGive.remove(entry.getKey());
                ServerPlayer player = event.getServer().getPlayerList().getPlayer(entry.getKey());
                if (player != null) {
                    applyDebug(player);
                }
            } else {
                pendingGive.put(entry.getKey(), remaining);
            }
        }
    }

    /** プレイヤーログイン時：デバッグモードなら遅延キューに追加 */
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!MicrafantasyConfig.isDebugMode()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        pendingGive.put(player.getUUID(), GIVE_DELAY_TICKS);
        MicrafantasyMod.LOGGER.info("[Debug] Queued debug apply for {} ({}tick delay)",
                player.getName().getString(), GIVE_DELAY_TICKS);
    }

    /** デバッグ処理本体 */
    private static void applyDebug(ServerPlayer player) {
        int targetLevel = Math.max(1, MicrafantasyConfig.getDebugJobLevel());

        // ── 1. 全ジョブストーン配布（未所持の場合のみ） ──
        giveItemIfNotHeld(player, new ItemStack(ModItems.PALADIN_JOB_STONE.get()));
        // 今後ジョブが追加されたらここに追記
        // giveItemIfNotHeld(player, new ItemStack(ModItems.WARRIOR_JOB_STONE.get()));

        // ── 2 & 3. 全ジョブのレベルをセット、EXPを0にリセット ──
        // 現在装備中のジョブのデータを更新する
        // （将来的に複数ジョブデータを持つ場合はここを拡張）
        JobData data = player.getData(ModAttachmentTypes.JOB_DATA.get());
        if (data.getJobType() != JobType.NONE) {
            data.setLevel(targetLevel);
            data.setExperience(0);
            data.setTotalExperience(0);
            // experienceToNextLevel を targetLevel に合わせて再計算
            // 式: (level * 10) ^ 2
            int n = targetLevel * 10;
            data.setExperienceToNextLevel(n * n);
            // ST/MPはジョブレベル非依存（固定100f）
            data.setStamina(data.getMaxStamina());
            data.setMana(data.getMaxMana());
            ModNetwork.sendToPlayer(player, new SyncJobDataPayload(data));
        }

        MicrafantasyMod.LOGGER.info("[Debug] Applied debug to {}: level={}, exp=0, gave all job stones",
                player.getName().getString(), targetLevel);
    }

    /** 既に同じアイテムを持っていない場合のみ付与する */
    private static void giveItemIfNotHeld(ServerPlayer player, ItemStack stack) {
        boolean alreadyHas = player.getInventory().hasAnyOf(java.util.Set.of(stack.getItem()));
        if (!alreadyHas) {
            if (!player.addItem(stack)) {
                player.drop(stack, false);
            }
            MicrafantasyMod.LOGGER.info("[Debug] Gave {} to {}",
                    stack.getItem().getDescriptionId(), player.getName().getString());
        }
    }
}

