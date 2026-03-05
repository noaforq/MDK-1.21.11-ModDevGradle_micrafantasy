package cloud.laboratory.n.micrafantasy.registry;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
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
}
