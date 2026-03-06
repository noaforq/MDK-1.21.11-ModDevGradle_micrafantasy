package cloud.laboratory.n.micrafantasy.registry;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MicrafantasyMod.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MICRAFANTASY_TAB =
            CREATIVE_MODE_TABS.register("micrafantasy_tab", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.micrafantasy"))
                            .withTabsBefore(CreativeModeTabs.COMBAT)
                            .icon(() -> ModItems.PALADIN_JOB_STONE.get().getDefaultInstance())
                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.PALADIN_JOB_STONE.get());
                                output.accept(ModItems.WHITE_MAGE_JOB_STONE.get());
                                output.accept(ModItems.JOB_CRYSTAL_BLOCK_ITEM.get());
                            })
                            .build()
            );
}
