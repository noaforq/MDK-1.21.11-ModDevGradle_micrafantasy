package cloud.laboratory.n.micrafantasy.skill;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.entity.player.Player;

/**
 * スキル共通ユーティリティ
 */
public final class SkillHelper {

    private SkillHelper() {}

    /**
     * プレイヤーの現在の攻撃力（装備・バフ込みの計算済み値）を返す。
     * Attributes.ATTACK_DAMAGE はメインハンド武器の属性を含む。
     *
     * @param player 対象プレイヤー
     * @return 攻撃力（float）
     */
    public static float getWeaponDamage(Player player) {
        double dmg = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        return (float) Math.max(1.0, dmg);
    }

    /**
     * プレイヤーが持つ盾（オフハンド優先・なければメインハンド）の現在耐久値を返す。
     * 盾を持っていない場合は 0 を返す。
     *
     * @param player 対象プレイヤー
     * @return 盾の現在耐久値（最大耐久 - 損傷値）
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
}


