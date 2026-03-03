package cloud.laboratory.n.micrafantasy.event;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.job.JobData;
import cloud.laboratory.n.micrafantasy.job.JobType;
import cloud.laboratory.n.micrafantasy.network.ModNetwork;
import cloud.laboratory.n.micrafantasy.network.packet.SyncJobDataPayload;
import cloud.laboratory.n.micrafantasy.registry.ModAttachmentTypes;
import cloud.laboratory.n.micrafantasy.skill.SkillRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
@EventBusSubscriber(modid = MicrafantasyMod.MODID)
public class ResourceTickHandler {
    // 20tick = 1秒、自然回復は10秒に1ポイント
    private static final int REGEN_INTERVAL = 200;
    private static int tickCounter = 0;
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        tickCounter++;
        boolean doRegen = (tickCounter % REGEN_INTERVAL == 0);
        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            JobData data = player.getData(ModAttachmentTypes.JOB_DATA.get());
            if (data.getJobType() == JobType.NONE) continue;
            // クールダウン＆詠唱tick処理
            data.tickCooldowns();
            // 詠唱完了チェック → スキル発動（fireCastedSkill内で同期済み）
            if (data.isCastComplete()) {
                SkillRegistry.fireCastedSkill(player);
                continue;
            }
            // 自然回復
            if (doRegen) {
                data.setStamina(data.getStamina() + 2.0f);
                data.setMana(data.getMana() + 2.0f);
            }
            ModNetwork.sendToPlayer(player, new SyncJobDataPayload(data));
        }
    }
    /** 被ダメージでリミットゲージ蓄積 */
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        JobData data = player.getData(ModAttachmentTypes.JOB_DATA.get());
        if (data.getJobType() == JobType.NONE) return;
        float dmg = event.getNewDamage();
        data.setLimitGauge(data.getLimitGauge() + dmg * 5f);
        ModNetwork.sendToPlayer(player, new SyncJobDataPayload(data));
    }
    /** ログイン時にデータ同期 */
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            JobData data = player.getData(ModAttachmentTypes.JOB_DATA.get());
            ModNetwork.sendToPlayer(player, new SyncJobDataPayload(data));
        }
    }

    /** 睡眠完了でマナ全回復 */
    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (event.updateLevel()) { // 夜をスキップした場合のみ（通常の起床）
            JobData data = player.getData(ModAttachmentTypes.JOB_DATA.get());
            if (data.getJobType() == JobType.NONE) return;
            data.setMana(data.getMaxMana()); // マナ全回復
            ModNetwork.sendToPlayer(player, new SyncJobDataPayload(data));
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.translatable("message.micrafantasy.mana_restored"), true);
        }
    }
}
