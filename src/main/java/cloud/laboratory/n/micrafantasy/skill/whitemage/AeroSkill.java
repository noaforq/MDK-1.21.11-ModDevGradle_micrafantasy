package cloud.laboratory.n.micrafantasy.skill.whitemage;

import cloud.laboratory.n.micrafantasy.skill.EffectHelper;
import cloud.laboratory.n.micrafantasy.skill.ISkill;
import cloud.laboratory.n.micrafantasy.skill.SkillHelper;
import cloud.laboratory.n.micrafantasy.skill.SkillType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

/**
 * 3: エアロ（魔法スキル）
 * 効果：視線方向の1体に Poison（継続ダメージ）＋即時魔法ダメージ
 * アンロック：Lv.4
 */
public class AeroSkill implements ISkill {

    private static final float STAMINA_COST    = 0f;
    private static final float MANA_COST       = 20f;
    private static final int   COOLDOWN_TICKS  = 6 * 20; // 6秒
    private static final int   CAST_TIME_TICKS = 0;
    private static final int   UNLOCK_LEVEL    = 4;
    private static final float DAMAGE_MULTIPLIER = 1.5f;
    private static final double REACH          = 16.0;

    @Override public int getId()            { return 3; }
    @Override public String getName()       { return "Aero"; }
    @Override public SkillType getType()    { return SkillType.MAGIC; }
    @Override public float getStaminaCost() { return STAMINA_COST; }
    @Override public float getManaCost()    { return MANA_COST; }
    @Override public int getCooldownTicks() { return COOLDOWN_TICKS; }
    @Override public int getCastTimeTicks() { return CAST_TIME_TICKS; }
    @Override public int getUnlockLevel()   { return UNLOCK_LEVEL; }

    @Override
    public void execute(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();

        Vec3 eyePos  = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        Vec3 endPos  = eyePos.add(lookVec.scale(REACH));

        AABB searchBox = player.getBoundingBox().inflate(REACH);
        List<LivingEntity> candidates = level.getEntitiesOfClass(
                LivingEntity.class, searchBox,
                e -> e != player && !e.isAlliedTo(player) && e.isAlive());

        Optional<LivingEntity> opt = candidates.stream()
                .filter(e -> e.getBoundingBox().inflate(0.3).clip(eyePos, endPos).isPresent())
                .min(java.util.Comparator.comparingDouble(e -> e.distanceToSqr(player)));

        if (opt.isPresent()) {
            LivingEntity target = opt.get();
            float damage = SkillHelper.getPlayerBaseAttackDamage(player) * DAMAGE_MULTIPLIER;
            damage = Math.max(1f, damage);
            target.hurtServer(level, player.damageSources().magic(), damage);
            // 継続ダメージ：Poison Lv1（12秒）
            EffectHelper.applyDebuff(level, target, new MobEffectInstance(MobEffects.POISON, 12 * 20, 0));

            level.playSound(null, target.getX(), target.getY(), target.getZ(),
                    SoundEvents.BREEZE_WIND_CHARGE_BURST, SoundSource.PLAYERS, 1.0f, 1.2f);
        }
    }
}
