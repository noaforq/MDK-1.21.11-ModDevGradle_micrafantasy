package cloud.laboratory.n.micrafantasy.block;

import cloud.laboratory.n.micrafantasy.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class JobCrystalBlock extends Block {

    public JobCrystalBlock(Properties properties) {
        super(properties);
    }

    /**
     * ブロックが壊れた後にジョブストーンをドロップする。
     * ロードテーブルに依存せず直接スポーンさせる。
     */
    @Override
    public void playerDestroy(net.minecraft.world.level.Level level, Player player,
                              BlockPos pos, BlockState state,
                              net.minecraft.world.level.block.entity.BlockEntity blockEntity,
                              ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);

        if (!level.isClientSide()) {
            Block.popResource(level, pos, new ItemStack(ModItems.PALADIN_JOB_STONE.get()));
        }
    }
}
