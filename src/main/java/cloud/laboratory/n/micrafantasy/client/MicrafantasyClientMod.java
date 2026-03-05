package cloud.laboratory.n.micrafantasy.client;

import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.client.keybind.ModKeyBindings;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@Mod(value = MicrafantasyMod.MODID, dist = Dist.CLIENT)
public class MicrafantasyClientMod {

    public MicrafantasyClientMod(IEventBus modEventBus, ModContainer container) {
        MicrafantasyMod.LOGGER.info("Micrafantasy Client initialized");
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onRegisterKeyMappings);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        MicrafantasyMod.LOGGER.info("Micrafantasy Client Setup");
    }

    private void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.registerCategory(ModKeyBindings.CATEGORY);

        // スキルキー (slot 0〜10)
        for (KeyMapping key : ModKeyBindings.SKILL_KEYS) {
            event.register(key);
        }

        // カメラ操作キー
        event.register(ModKeyBindings.CAMERA_ZOOM_IN);
        event.register(ModKeyBindings.CAMERA_ZOOM_OUT);
        event.register(ModKeyBindings.CAMERA_VIEW);
        event.register(ModKeyBindings.CAMERA_RESET);
        event.register(ModKeyBindings.CAMERA_UP);
        event.register(ModKeyBindings.CAMERA_DOWN);
        event.register(ModKeyBindings.CAMERA_LEFT);
        event.register(ModKeyBindings.CAMERA_RIGHT);
    }
}
