package cloud.laboratory.n.micrafantasy.skill;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

/**
 * 与ダメージを伴うリミットブレイク・魔法スキル・ジョブスキルの
 * 与ダメージ1/10を地形に与えるユーティリティ
 */
public class TerrainDamageHelper {

    /**
     * エンティティ周辺のブロックをダメージ量に応じて破壊する
     * @param level  サーバーレベル
     * @param target 攻撃対象エンティティ
     * @param damage 与ダメージ（実際に地形に与えるのは1/10）
     */
    public static void applyTerrainDamage(ServerLevel level, LivingEntity target, float damage) {
        float terrainDmg = damage * 0.1f;
        // 破壊半径（ダメージに比例、最大2ブロック）
        int radius = Math.min(2, Math.max(0, (int)(terrainDmg / 3f)));
        if (radius <= 0) return;

        BlockPos center = target.blockPosition();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos pos = center.offset(dx, dy, dz);
                    var state = level.getBlockState(pos);
                    if (!state.isAir()) {
                        float hardness = state.getDestroySpeed(level, pos);
                        // 硬さがterrainDmgより低いブロックのみ破壊
                        if (hardness >= 0 && hardness <= terrainDmg) {
                            level.destroyBlock(pos, true);
                        }
                    }
                }
            }
        }
    }
}

