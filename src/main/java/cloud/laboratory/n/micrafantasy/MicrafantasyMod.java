package cloud.laboratory.n.micrafantasy;
import cloud.laboratory.n.micrafantasy.config.MicrafantasyConfig;
import cloud.laboratory.n.micrafantasy.network.ModNetwork;
import cloud.laboratory.n.micrafantasy.registry.ModAttachmentTypes;
import cloud.laboratory.n.micrafantasy.registry.ModBlocks;
import cloud.laboratory.n.micrafantasy.registry.ModCreativeTabs;
import cloud.laboratory.n.micrafantasy.registry.ModItems;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;
@Mod(MicrafantasyMod.MODID)
public class MicrafantasyMod {
    public static final String MODID = "micrafantasy";
    public static final Logger LOGGER = LogUtils.getLogger();
    public MicrafantasyMod(IEventBus modEventBus, ModContainer modContainer) {
        // 設定ファイルをロード（最初に実行）
        MicrafantasyConfig.load();
        LOGGER.info("Micrafantasy MOD initializing... (debug_mode={})", MicrafantasyConfig.isDebugMode());
        // レジストリ登録
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);
        // ネットワーク登録
        modEventBus.addListener(this::onRegisterPayloads);
        modEventBus.addListener(this::commonSetup);
    }
    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Micrafantasy common setup complete");
    }
    private void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        ModNetwork.register(event);
    }
}
