package cloud.laboratory.n.micrafantasy.client.event;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.client.keybind.ModKeyBindings;
import cloud.laboratory.n.micrafantasy.network.packet.UseSkillPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
@EventBusSubscriber(modid = MicrafantasyMod.MODID, value = Dist.CLIENT)
public class KeyInputHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        for (int i = 0; i < ModKeyBindings.SKILL_KEYS.length; i++) {
            if (ModKeyBindings.SKILL_KEYS[i].consumeClick()) {
                ClientPacketDistributor.sendToServer(new UseSkillPayload(i));
            }
        }
    }
}
