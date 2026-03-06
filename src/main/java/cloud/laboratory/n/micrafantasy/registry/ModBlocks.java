package cloud.laboratory.n.micrafantasy.registry;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.block.CrystalOreBlock;
import cloud.laboratory.n.micrafantasy.block.HerbBlock;
import cloud.laboratory.n.micrafantasy.block.JobCrystalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MicrafantasyMod.MODID);

    public static final DeferredBlock<JobCrystalBlock> JOB_CRYSTAL_BLOCK =
            BLOCKS.registerBlock(
                    "job_crystal_block",
                    JobCrystalBlock::new,
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLUE)
                            .strength(3.0f, 6.0f)
                            .sound(SoundType.AMETHYST)
                            .lightLevel(state -> 15)
            );

    /** 薬草ブロック（花と同じく草の上に生える） */
    public static final DeferredBlock<HerbBlock> HERB =
            BLOCKS.registerBlock(
                    "herb",
                    HerbBlock::new,
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.PLANT)
                            .noCollision()
                            .noOcclusion()
                            .instabreak()
                            .sound(SoundType.GRASS)
                            .offsetType(BlockBehaviour.OffsetType.XZ)
            );

    /** クリスタル鉱石ブロック（石の中に生成・壊すと欠片ドロップ） */
    public static final DeferredBlock<CrystalOreBlock> CRYSTAL_ORE =
            BLOCKS.registerBlock(
                    "crystal_ore",
                    CrystalOreBlock::new,
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(3.0f, 3.0f)
                            .sound(SoundType.STONE)
                            .lightLevel(state -> 4)
            );
}
