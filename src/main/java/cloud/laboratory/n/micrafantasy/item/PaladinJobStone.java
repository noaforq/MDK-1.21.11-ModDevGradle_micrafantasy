package cloud.laboratory.n.micrafantasy.item;
import cloud.laboratory.n.micrafantasy.job.JobData;
import cloud.laboratory.n.micrafantasy.job.JobType;
import cloud.laboratory.n.micrafantasy.network.ModNetwork;
import cloud.laboratory.n.micrafantasy.network.packet.SyncJobDataPayload;
import cloud.laboratory.n.micrafantasy.registry.ModAttachmentTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import java.util.function.Consumer;
public class PaladinJobStone extends Item {
    public PaladinJobStone(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            JobData data = serverPlayer.getData(ModAttachmentTypes.JOB_DATA.get());
            if (data.getJobType() == JobType.PALADIN) {
                // 既にナイトの場合はジョブを解除
                data.setJobType(JobType.NONE);
                serverPlayer.displayClientMessage(
                        Component.translatable("message.micrafantasy.job_removed"), true);
            } else {
                // ナイトにジョブチェンジ
                data.setJobType(JobType.PALADIN);
                serverPlayer.displayClientMessage(
                        Component.translatable("message.micrafantasy.job_changed.paladin"), true);
            }
            // クライアントへ同期
            ModNetwork.sendToPlayer(serverPlayer, new SyncJobDataPayload(data));
        }
        return InteractionResult.SUCCESS;
    }
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.micrafantasy.paladin_job_stone.tooltip"));
    }
}
