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
 * 1: 挑発（物理スキル）
 * 効果：敵対心アップ＋移動速度ダウン付与
 * アンロック：Lv.1
 */
public class ProvokeSkill implements ISkill {

    private static final float STAMINA_COST    = 15f;
    private static final float MANA_COST       = 0f;
    private static final int   COOLDOWN_TICKS  = 30 * 20; // 30秒
    private static final int   CAST_TIME_TICKS = 0;
    private static final int   UNLOCK_LEVEL    = 1;
    private static final float RANGE           = 20f;

    @Override public int getId()            { return 1; }
    @Override public String getName()       { return "Provoke"; }
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

        for (LivingEntity entity : targets) {
            // 敵対心アップ
            if (entity instanceof Mob mob) mob.setTarget(player);
            // 移動速度ダウン（Slowness Lv2、5秒）
            EffectHelper.applyDebuff(level, entity, new MobEffectInstance(MobEffects.SLOWNESS, 5 * 20, 1));
        }

        level.playSound(null, x, y, z, SoundEvents.RAVAGER_ROAR, SoundSource.PLAYERS, 1.0f, 1.0f);
    }
}
