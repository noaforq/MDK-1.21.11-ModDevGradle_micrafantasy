package cloud.laboratory.n.micrafantasy.skill.paladin;

import cloud.laboratory.n.micrafantasy.network.ModNetwork;
import cloud.laboratory.n.micrafantasy.skill.EffectHelper;
import cloud.laboratory.n.micrafantasy.skill.ISkill;
import cloud.laboratory.n.micrafantasy.skill.SkillHelper;
import cloud.laboratory.n.micrafantasy.skill.SkillType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * 2: シールド・バッシュ（物理スキル）
 * 効果：敵対心アップ＋スタン（Slowness Lv127）
 * アンロック：Lv.10
 * 使用条件：盾（ShieldItem）を装備していること
 */
public class ShieldBashSkill implements ISkill {

    private static final float STAMINA_COST    = 20f;
    private static final float MANA_COST       = 0f;
    private static final int   COOLDOWN_TICKS  = 15 * 20; // 15秒
    private static final int   CAST_TIME_TICKS = 0;
    private static final int   UNLOCK_LEVEL    = 10;
    private static final float DAMAGE          = 6f;
    private static final float RANGE           = 4f;

    @Override public int getId()            { return 2; }
    @Override public String getName()       { return "Shield Bash"; }
    @Override public SkillType getType()    { return SkillType.PHYSICAL; }
    @Override public float getStaminaCost() { return STAMINA_COST; }
    @Override public float getManaCost()    { return MANA_COST; }
    @Override public int getCooldownTicks() { return COOLDOWN_TICKS; }
    @Override public int getCastTimeTicks() { return CAST_TIME_TICKS; }
    @Override public int getUnlockLevel()   { return UNLOCK_LEVEL; }

    @Override
    public boolean canUse(ServerPlayer player) {
        boolean hasShield =
                player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ShieldItem ||
                player.getItemInHand(InteractionHand.OFF_HAND).getItem()  instanceof ShieldItem;
        if (!hasShield) {
            player.displayClientMessage(
                    Component.literal("シールド・バッシュには盾の装備が必要です"), true);
            return false;
        }
        return true;
    }

    @Override
    public boolean canUseClient(net.minecraft.world.entity.player.Player player) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ShieldItem ||
               player.getItemInHand(InteractionHand.OFF_HAND).getItem()  instanceof ShieldItem;
    }

    /** 盾はオフハンド優先でswing（クライアント側で実行される） */
    @Override
    public net.minecraft.world.InteractionHand swingHand() {
        return net.minecraft.world.InteractionHand.OFF_HAND;
    }

    @Override
    public void execute(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        double x = player.getX(), y = player.getY(), z = player.getZ();


        AABB area = player.getBoundingBox().inflate(RANGE);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area,
                e -> e != player && !e.isAlliedTo(player));

        for (LivingEntity target : targets) {
            // 敵対心アップ
            if (target instanceof Mob mob) mob.setTarget(player);
            // ダメージ
            boolean hit = target.hurtServer(level, player.damageSources().playerAttack(player), DAMAGE);
            if (hit) ModNetwork.sendDamage(player, DAMAGE, false);
            // スタン時間：盾の現在耐久値 / 10 tick（最小1秒、上限 Slowness Lv127 × 20tick）
            int shieldDur = SkillHelper.getShieldDurability(player);
            if (127 < shieldDur) {
                shieldDur = 127;
            }
            EffectHelper.applyDebuff(level, target, new MobEffectInstance(MobEffects.SLOWNESS, shieldDur, 127, false, false));
        }

        level.playSound(null, x, y, z, SoundEvents.SHIELD_BLOCK.value(), SoundSource.PLAYERS, 1.0f, 0.7f);
    }
}
