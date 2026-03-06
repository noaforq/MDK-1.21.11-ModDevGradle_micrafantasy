package cloud.laboratory.n.micrafantasy.block;

import cloud.laboratory.n.micrafantasy.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * クリスタル鉱石ブロック
 * 石の中に埋まって生成される鉱石。
 * 壊すとクリスタルの欠片を1〜3個ドロップする。
 */
@SuppressWarnings("NullableProblems")
public class CrystalOreBlock extends Block {

    public CrystalOreBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void playerDestroy(Level level, Player player,
                              BlockPos pos, BlockState state,
                              @Nullable BlockEntity blockEntity,
                              ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (level instanceof ServerLevel serverLevel) {
            int count = 1 + serverLevel.getRandom().nextInt(3); // 1〜3個
            for (int i = 0; i < count; i++) {
                Block.popResource(serverLevel, pos,
                        new ItemStack(ModItems.CRYSTAL_SHARD.asItem(), 1));
            }
        }
    }

    /** シルクタッチ以外でもブロック自体をドロップしないようにする */
    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos,
                                ItemStack stack, boolean dropExperience) {
        // vanilla の ore experience ドロップのみ処理（アイテムは playerDestroy 側で出す）
        super.spawnAfterBreak(state, level, pos, stack, dropExperience);
    }
}

