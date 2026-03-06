package cloud.laboratory.n.micrafantasy.registry;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.item.CrystalShardItem;
import cloud.laboratory.n.micrafantasy.item.HealingPotionItem;
import cloud.laboratory.n.micrafantasy.item.PaladinJobStone;
import cloud.laboratory.n.micrafantasy.item.WhiteMageJobStone;
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
    // 白魔道士のジョブストーン
    public static final DeferredItem<WhiteMageJobStone> WHITE_MAGE_JOB_STONE =
            ITEMS.registerItem(
                    "white_mage_job_stone",
                    WhiteMageJobStone::new,
                    () -> new Item.Properties().stacksTo(1)
            );

    // クリスタルブロックのブロックアイテム
    public static final DeferredItem<BlockItem> JOB_CRYSTAL_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("job_crystal_block", ModBlocks.JOB_CRYSTAL_BLOCK);

    // 薬草（BlockItem）
    public static final DeferredItem<BlockItem> HERB =
            ITEMS.registerSimpleBlockItem("herb", ModBlocks.HERB,
                    () -> new Item.Properties().stacksTo(64));

    // クリスタル鉱石（ブロックアイテム）
    public static final DeferredItem<BlockItem> CRYSTAL_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("crystal_ore", ModBlocks.CRYSTAL_ORE);

    // クリスタルの欠片
    public static final DeferredItem<CrystalShardItem> CRYSTAL_SHARD =
            ITEMS.registerItem(
                    "crystal_shard",
                    CrystalShardItem::new,
                    () -> new Item.Properties().stacksTo(64)
            );

    // 回復ポーション（スタック16個まで）
    public static final DeferredItem<HealingPotionItem> HEALING_POTION =
            ITEMS.registerItem(
                    "healing_potion",
                    HealingPotionItem::new,
                    () -> new Item.Properties().stacksTo(16)
            );
}
