package cloud.laboratory.n.micrafantasy.block;

import cloud.laboratory.n.micrafantasy.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * 薬草ブロック - 花と同様に草の上に生える植物。
 * 壊すと薬草アイテムを1〜2個ドロップする。
 */
@SuppressWarnings("NullableProblems")
public class HerbBlock extends BushBlock {

    private static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);

    public HerbBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level,
                               BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
                              @Nullable BlockEntity blockEntity, ItemStack tool) {
        // super は loot table を参照するが BushBlock では空になる場合があるため直接ドロップ
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (!level.isClientSide()) {
            int count = 1 + level.getRandom().nextInt(2); // 1〜2個
            Block.popResource(level, pos, new ItemStack(ModItems.HERB.get(), count));
        }
    }
}

