package cloud.laboratory.n.micrafantasy.event;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.job.JobData;
import cloud.laboratory.n.micrafantasy.job.JobType;
import cloud.laboratory.n.micrafantasy.network.ModNetwork;
import cloud.laboratory.n.micrafantasy.network.packet.SyncJobDataPayload;
import cloud.laboratory.n.micrafantasy.registry.ModAttachmentTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = MicrafantasyMod.MODID)
public class JobExperienceHandler {

    /** 敵を倒したとき / プレイヤーが死亡したとき */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        // ── プレイヤー自身が死亡：累計EXP半減 → レベル再計算 ──
        if (entity instanceof ServerPlayer player) {
            JobData data = player.getData(ModAttachmentTypes.JOB_DATA.get());
            if (data.getJobType() == JobType.NONE) return;

            int beforeTotal = data.getTotalExperience();
            int beforeLevel = data.getLevel();
            data.onDeath();
            int afterTotal  = data.getTotalExperience();
            int afterLevel  = data.getLevel();

            MicrafantasyMod.LOGGER.debug(
                    "[JobExp] {} が死亡。累計EXP {} -> {} / Lv {} -> {}",
                    player.getName().getString(), beforeTotal, afterTotal, beforeLevel, afterLevel);

            player.displayClientMessage(
                    Component.translatable("message.micrafantasy.job_exp_halved"), true);

            ModNetwork.sendToPlayer(player, new SyncJobDataPayload(data));
            return;
        }

        // ── 敵を倒した：EXP加算 ──
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        JobData data = player.getData(ModAttachmentTypes.JOB_DATA.get());
        if (data.getJobType() == JobType.NONE) return;

        int expGain = (int) (entity.getMaxHealth() * 5f);
        boolean leveledUp = data.addExperience(expGain);

        // リミットゲージも増加
        data.setLimitGauge(data.getLimitGauge() + entity.getMaxHealth() * 2f);

        if (leveledUp) {
            player.displayClientMessage(
                    Component.translatable("message.micrafantasy.level_up", data.getLevel()), false);
        }

        ModNetwork.sendToPlayer(player, new SyncJobDataPayload(data));
    }
}
