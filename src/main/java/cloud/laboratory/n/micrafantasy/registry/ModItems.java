package cloud.laboratory.n.micrafantasy.registry;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.item.PaladinJobStone;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MicrafantasyMod.MODID);
    // ナイトのジョブストーン
    public static final DeferredItem<PaladinJobStone> PALADIN_JOB_STONE =
            ITEMS.registerItem(
                    "paladin_job_stone",
                    PaladinJobStone::new,
                    () -> new Item.Properties().stacksTo(1)
            );
    // クリスタルブロックのブロックアイテム
    public static final DeferredItem<BlockItem> JOB_CRYSTAL_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("job_crystal_block", ModBlocks.JOB_CRYSTAL_BLOCK);
}
