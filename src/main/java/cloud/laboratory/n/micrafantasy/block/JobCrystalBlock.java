package cloud.laboratory.n.micrafantasy.block;

import cloud.laboratory.n.micrafantasy.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("NullableProblems")
public class JobCrystalBlock extends Block {

    public JobCrystalBlock(Properties properties) {
        super(properties);
    }

    /**
     * クライアント側のランダムティックでパーティクルを発生させ、
     * ブロックが強烈な光を放っているように見せる。
     */
    @Override
    @SuppressWarnings("NullableProblems")
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double bx = pos.getX() + 0.5;
        double by = pos.getY() + 0.5;
        double bz = pos.getZ() + 0.5;

        // ① END_ROD（青白い輝き）: 多量・広範囲に放射
        int endRodCount = 10 + random.nextInt(6); // 10〜15個
        for (int i = 0; i < endRodCount; i++) {
            double ox = (random.nextDouble() - 0.5) * 2.4;
            double oy = (random.nextDouble() - 0.5) * 2.4;
            double oz = (random.nextDouble() - 0.5) * 2.4;
            double vx = (random.nextDouble() - 0.5) * 0.08;
            double vy =  random.nextDouble()        * 0.10;
            double vz = (random.nextDouble() - 0.5) * 0.08;
            level.addParticle(ParticleTypes.END_ROD,
                    bx + ox, by + oy, bz + oz,
                    vx, vy, vz);
        }

        // ② GLOW（強い発光点）: ブロック表面付近で輝く
        int glowCount = 6 + random.nextInt(4); // 6〜9個
        for (int i = 0; i < glowCount; i++) {
            double ox = (random.nextDouble() - 0.5);
            double oy = (random.nextDouble() - 0.5);
            double oz = (random.nextDouble() - 0.5);
            level.addParticle(ParticleTypes.GLOW,
                    bx + ox, by + oy, bz + oz,
                    0.0, 0.0, 0.0);
        }

        // ③ SOUL_FIRE_FLAME（青い炎）: 上方向に漂い神秘感を演出
        int flameCount = 2 + random.nextInt(3); // 2〜4個
        for (int i = 0; i < flameCount; i++) {
            double ox = (random.nextDouble() - 0.5) * 1.4;
            double oz = (random.nextDouble() - 0.5) * 1.4;
            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                    bx + ox, by + 0.5 + random.nextDouble() * 0.5, bz + oz,
                    0.0, 0.03, 0.0);
        }

        // ④ ENCHANT（魔法の光の粒）: 周囲に飛び散る
        if (random.nextInt(2) == 0) {
            int enchCount = 3 + random.nextInt(3); // 3〜5個
            for (int i = 0; i < enchCount; i++) {
                double ox = (random.nextDouble() - 0.5) * 1.8;
                double oy = (random.nextDouble() - 0.5) * 1.8;
                double oz = (random.nextDouble() - 0.5) * 1.8;
                double vx = (random.nextDouble() - 0.5) * 0.12;
                double vy =  random.nextDouble()        * 0.12;
                double vz = (random.nextDouble() - 0.5) * 0.12;
                level.addParticle(ParticleTypes.ENCHANT,
                        bx + ox, by + oy, bz + oz,
                        vx, vy, vz);
            }
        }
    }

    /**
     * ブロックが壊れた後にジョブストーンをドロップする。
     */
    @Override
    @SuppressWarnings("NullableProblems")
    public void playerDestroy(Level level, Player player,
                              BlockPos pos, BlockState state,
                              @Nullable BlockEntity blockEntity,
                              ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);

        if (!level.isClientSide()) {
            Block.popResource(level, pos, new ItemStack(ModItems.PALADIN_JOB_STONE.asItem()));
            // クリスタルの欠片を1〜3個ドロップ
            int shardCount = 1 + level.getRandom().nextInt(3);
            Block.popResource(level, pos, new ItemStack(ModItems.CRYSTAL_SHARD.asItem(), shardCount));
        }
    }
}
