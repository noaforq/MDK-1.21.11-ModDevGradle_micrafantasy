package cloud.laboratory.n.micrafantasy.event;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.job.JobData;
import cloud.laboratory.n.micrafantasy.job.JobType;
import cloud.laboratory.n.micrafantasy.network.ModNetwork;
import cloud.laboratory.n.micrafantasy.network.packet.SyncJobDataPayload;
import cloud.laboratory.n.micrafantasy.registry.ModAttachmentTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
@EventBusSubscriber(modid = MicrafantasyMod.MODID)
public class FoodEatHandler {
    /** 食事でスタミナ回復 */
    @SubscribeEvent
    public static void onItemFinishedUsing(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ItemStack stack = event.getItem();
        FoodProperties food = stack.get(DataComponents.FOOD);
        if (food == null) return;
        JobData data = player.getData(ModAttachmentTypes.JOB_DATA.get());
        if (data.getJobType() == JobType.NONE) return;
        // 栄養価に比例してスタミナ回復
        float staminaGain = food.nutrition() * 3f;
        data.setStamina(data.getStamina() + staminaGain);
        ModNetwork.sendToPlayer(player, new SyncJobDataPayload(data));
    }
}
