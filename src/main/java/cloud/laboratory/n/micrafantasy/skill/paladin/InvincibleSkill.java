package cloud.laboratory.n.micrafantasy.skill.paladin;

import cloud.laboratory.n.micrafantasy.skill.EffectHelper;
import cloud.laboratory.n.micrafantasy.skill.ISkill;
import cloud.laboratory.n.micrafantasy.skill.SkillType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * 8: インビンシブル（ジョブスキル）
 * 効果：敵対心アップ＋無敵（Resistance Lv5 + Regeneration Lv5）
 * アンロック：Lv.50
 */
public class InvincibleSkill implements ISkill {

    private static final float STAMINA_COST    = 0f;
    private static final float MANA_COST       = 0f;
    private static final int   COOLDOWN_TICKS  = 24000; // ジョブスキルは1日1回
    private static final int   CAST_TIME_TICKS = 0;
    private static final int   UNLOCK_LEVEL    = 50;
    private static final float RANGE           = 12f;

    @Override public int getId()            { return 8; }
    @Override public String getName()       { return "Invincible"; }
    @Override public SkillType getType()    { return SkillType.JOB; }
    @Override public float getStaminaCost() { return STAMINA_COST; }
    @Override public float getManaCost()    { return MANA_COST; }
    @Override public int getCooldownTicks() { return COOLDOWN_TICKS; }
    @Override public int getCastTimeTicks() { return CAST_TIME_TICKS; }
    @Override public int getUnlockLevel()   { return UNLOCK_LEVEL; }

    @Override
    public void execute(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        double x = player.getX(), y = player.getY() + 1, z = player.getZ();

        // 敵対心アップ
        AABB area = player.getBoundingBox().inflate(RANGE);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area,
                e -> e != player && !e.isAlliedTo(player));
        for (LivingEntity entity : targets) {
            if (entity instanceof Mob mob) mob.setTarget(player);
        }

        // 無敵：Resistance Lv5（100tick ≒ 5秒）+ Regeneration Lv5（100tick）
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.RESISTANCE,   200, 4));
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.REGENERATION, 200, 4));

        level.playSound(null, x, y, z, SoundEvents.TOTEM_USE,     SoundSource.PLAYERS, 1.0f, 1.0f);
        level.playSound(null, x, y, z, SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 0.8f, 1.5f);
    }
}
