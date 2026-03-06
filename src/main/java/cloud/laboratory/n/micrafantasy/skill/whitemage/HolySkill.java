package cloud.laboratory.n.micrafantasy.skill.whitemage;

import cloud.laboratory.n.micrafantasy.network.ModNetwork;
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

import java.util.List;

/**
 * 6: ホーリー（魔法スキル）
 * 効果：周囲全敵に強力な範囲魔法ダメージ＋スタン（Slowness Lv10 で擬似スタン）
 * アンロック：Lv.35
 * 詠唱時間：2.5秒（50tick）
 */
public class HolySkill implements ISkill {

    private static final float STAMINA_COST    = 0f;
    private static final float MANA_COST       = 60f;
    private static final int   COOLDOWN_TICKS  = 15 * 20; // 15秒
    private static final int   CAST_TIME_TICKS = 50;      // 2.5秒
    private static final int   UNLOCK_LEVEL    = 35;
    private static final float DAMAGE_MULTIPLIER = 3.5f;
    private static final float RANGE           = 10f;

    @Override public int getId()            { return 6; }
    @Override public String getName()       { return "Holy"; }
    @Override public SkillType getType()    { return SkillType.MAGIC; }
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
                e -> e != player && !e.isAlliedTo(player) && e.isAlive());

        float damage = SkillHelper.getPlayerBaseAttackDamage(player) * DAMAGE_MULTIPLIER;
        damage = Math.max(1f, damage);

        for (LivingEntity target : targets) {
            boolean hit = target.hurtServer(level, player.damageSources().magic(), damage);
            if (hit) {
                // 擬似スタン：Slowness Lv10（3秒）
                EffectHelper.applyDebuff(level, target,
                        new MobEffectInstance(MobEffects.SLOWNESS, 3 * 20, 10));
                ModNetwork.sendDamage(player, damage, true);
            }
        }

        level.playSound(null, x, y, z, SoundEvents.BEACON_ACTIVATE,    SoundSource.PLAYERS, 1.0f, 1.5f);
        level.playSound(null, x, y, z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.5f, 2.0f);
    }
}
