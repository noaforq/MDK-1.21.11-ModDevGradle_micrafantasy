package cloud.laboratory.n.micrafantasy.skill;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * スキル共通ユーティリティ
 */
public final class SkillHelper {

    private SkillHelper() {}

    /**
     * プレイヤーの現在の攻撃力（装備・バフ込みの計算済み値）を返す。
     */
    public static float getWeaponDamage(Player player) {
        double dmg = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        return (float) Math.max(1.0, dmg);
    }

    /**
     * プレイヤーのベース攻撃力（武器由来の加算を除く）を返す。
     */
    public static float getPlayerBaseAttackDamage(Player player) {
        var attr = player.getAttribute(Attributes.ATTACK_DAMAGE);
        double base = (attr != null) ? attr.getBaseValue() : 1.0;
        return (float) Math.max(1.0, base);
    }

    /**
     * メインハンドの武器由来の攻撃力加算値のみを返す。
     */
    public static float getWeaponAttackDamage(Player player) {
        double total = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        var attr = player.getAttribute(Attributes.ATTACK_DAMAGE);
        double base = (attr != null) ? attr.getBaseValue() : 1.0;
        return (float) Math.max(0.0, total - base);
    }

    /**
     * 盾の現在耐久値を返す（オフハンド優先）。
     */
    public static int getShieldDurability(Player player) {
        ItemStack offhand = player.getOffhandItem();
        if (offhand.getItem() instanceof ShieldItem) {
            return Math.max(0, offhand.getMaxDamage() - offhand.getDamageValue());
        }
        ItemStack mainhand = player.getMainHandItem();
        if (mainhand.getItem() instanceof ShieldItem) {
            return Math.max(0, mainhand.getMaxDamage() - mainhand.getDamageValue());
        }
        return 0;
    }

    /**
     * プレイヤーの視線方向にあるブロックをバニラの destroyBlock で破壊する。
     * Mob が範囲内にいない場合に物理スキルから呼ぶことで木なども攻撃できる。
     *
     * @param player 攻撃プレイヤー
     * @param reach  射程（ブロック単位）
     * @return ブロックを破壊した場合 true
     */
    public static boolean hitBlockInSight(ServerPlayer player, double reach) {
        // 視線レイキャストでブロックヒットを取得
        HitResult hit = player.pick(reach, 1.0f, false);
        if (hit.getType() != HitResult.Type.BLOCK) return false;

        BlockPos pos = ((BlockHitResult) hit).getBlockPos();
        ServerLevel level = (ServerLevel) player.level();
        BlockState state = level.getBlockState(pos);

        // 空気・流体（水・溶岩）はスキップ
        if (state.isAir() || !state.getFluidState().isEmpty()) return false;

        // バニラのブロック破壊（ドロップあり）
        boolean destroyed = level.destroyBlock(pos, true, player);
        if (destroyed) {
            // 破壊音はdestroyBlock内で鳴るので追加不要
            return true;
        }
        return false;
    }
}


