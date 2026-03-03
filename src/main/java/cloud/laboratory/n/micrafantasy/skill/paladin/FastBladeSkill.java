package cloud.laboratory.n.micrafantasy.skill.paladin;

import cloud.laboratory.n.micrafantasy.skill.EffectHelper;
import cloud.laboratory.n.micrafantasy.skill.ISkill;
import cloud.laboratory.n.micrafantasy.skill.SkillHelper;
import cloud.laboratory.n.micrafantasy.skill.SkillType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * 4: ファスト・ブレード（物理スキル）
 * 効果：範囲小攻撃＋攻撃速度アップ（Haste）
 * アンロック：Lv.1
 */
public class FastBladeSkill implements ISkill {

    private static final float STAMINA_COST    = 10f;
    private static final float MANA_COST       = 0f;
    private static final int   COOLDOWN_TICKS  = 1 * 20;  // 1秒
    private static final int   CAST_TIME_TICKS = 0;
    private static final int   UNLOCK_LEVEL    = 1;
    private static final float DAMAGE_MULTIPLIER = 1.5f;  // 武器攻撃力 × 1.5
    private static final float RANGE           = 4f;

    @Override public int getId()            { return 4; }
    @Override public String getName()       { return "Fast Blade"; }
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

        AABB area = player.getBoundingBox().inflate(RANGE);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area,
                e -> e != player && !e.isAlliedTo(player));

        // 攻撃速度アップ（Haste Lv2、5秒）
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.HASTE, 5 * 20, 1));

        // 武器攻撃力 × 1.5
        float damage = SkillHelper.getWeaponDamage(player) * DAMAGE_MULTIPLIER;

        // 範囲小攻撃
        for (LivingEntity target : targets) {
            // 武器振りモーション
            player.swing(InteractionHand.MAIN_HAND);
            level.playSound(null, x, y, z, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0f, 1.2f);
            target.hurtServer(level, player.damageSources().playerAttack(player), damage);
        }
    }

    /** サブクラスから使えるよう公開 */
    protected LivingEntity getClosestEnemy(ServerPlayer player, double range) {
        AABB area = player.getBoundingBox().inflate(range);
        List<LivingEntity> targets = ((ServerLevel) player.level()).getEntitiesOfClass(
                LivingEntity.class, area, e -> e != player && !e.isAlliedTo(player));
        return targets.isEmpty() ? null : targets.get(0);
    }
}
