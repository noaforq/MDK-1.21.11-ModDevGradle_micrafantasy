package cloud.laboratory.n.micrafantasy.skill.paladin;

import cloud.laboratory.n.micrafantasy.network.ModNetwork;
import cloud.laboratory.n.micrafantasy.skill.ISkill;
import cloud.laboratory.n.micrafantasy.skill.SkillType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ShieldItem;

/**
 * 10(「.」): 通常防御（物理スキル）
 * 効果：
 *   - 盾装備時  → Resistance Lv1（30秒）付与 ＋ 盾構えアニメ（サーバー→クライアント通知）
 *   - 盾なし時  → Resistance Lv1（30秒）付与
 * アンロック：Lv.1
 */
public class NormalDefenseSkill implements ISkill {

    private static final float STAMINA_COST    = 0f;
    private static final float MANA_COST       = 0f;
    private static final int   COOLDOWN_TICKS  = 0;
    private static final int   CAST_TIME_TICKS = 0;
    private static final int   UNLOCK_LEVEL    = 1;

    private static final int SHIELD_USE_TICKS  = 30 * 20;
    private static final int NO_SHIELD_TICKS   = 30 * 20;

    @Override public int getId()            { return 10; }
    @Override public String getName()       { return "Defense"; }
    @Override public SkillType getType()    { return SkillType.PHYSICAL; }
    @Override public float getStaminaCost() { return STAMINA_COST; }
    @Override public float getManaCost()    { return MANA_COST; }
    @Override public int getCooldownTicks() { return COOLDOWN_TICKS; }
    @Override public int getCastTimeTicks() { return CAST_TIME_TICKS; }
    @Override public int getUnlockLevel()   { return UNLOCK_LEVEL; }

    @Override
    public void execute(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        double x = player.getX(), y = player.getY(), z = player.getZ();

        boolean shieldInOff  = player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ShieldItem;
        boolean shieldInMain = player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ShieldItem;
        boolean hasShield    = shieldInOff || shieldInMain;

        boolean alreadyBlocking = player.isUsingItem();

        if (hasShield) {
            player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, SHIELD_USE_TICKS, 0, false, false));
            if (!alreadyBlocking) {
                InteractionHand hand = shieldInOff ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
                player.startUsingItem(hand);
                ModNetwork.sendDefenseState(player, true, shieldInOff);
                level.playSound(null, x, y, z,
                        SoundEvents.SHIELD_BLOCK.value(), SoundSource.PLAYERS, 1.0f, 1.1f);
            }
            player.displayClientMessage(Component.literal("防御態勢"), true);
        } else {
            player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, NO_SHIELD_TICKS, 0, false, false));
            if (!alreadyBlocking) {
                level.playSound(null, x, y, z,
                        SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.PLAYERS, 0.8f, 1.0f);
            }
            player.displayClientMessage(Component.literal("防御態勢（盾なし）"), true);
        }
    }
}
