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
 * 5: ライオットソード（物理スキル）
 * 効果：範囲攻撃＋攻撃力アップ（Strength）
 * アンロック：Lv.15
 */
public class RiotSwordSkill implements ISkill {

    private static final float STAMINA_COST    = 15f;
    private static final float MANA_COST       = 0f;
    private static final int   COOLDOWN_TICKS  = 2 * 20;  // 2秒
    private static final int   CAST_TIME_TICKS = 0;
    private static final int   UNLOCK_LEVEL    = 15;
    private static final float DAMAGE_MULTIPLIER = 2.0f;  // 武器攻撃力 × 2.0
    private static final float RANGE           = 6f;

    @Override public int getId()            { return 5; }
    @Override public String getName()       { return "Riot Sword"; }
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

        // 攻撃力アップ（Strength Lv2、8秒）
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.STRENGTH, 8 * 20, 1));

        // 武器攻撃力 × 2.0
        float damage = SkillHelper.getWeaponDamage(player) * DAMAGE_MULTIPLIER;

        // 範囲攻撃
        for (LivingEntity target : targets) {
            // 武器振りモーション
            player.swing(InteractionHand.MAIN_HAND);
            level.playSound(null, x, y, z, SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 1.0f, 0.9f);
            target.hurtServer(level, player.damageSources().playerAttack(player), damage);
        }
    }
}
