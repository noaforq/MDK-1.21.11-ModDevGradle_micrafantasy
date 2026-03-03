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
 * 7: センチネル（魔法スキル）
 * 効果：敵対心アップ＋大軽減（Resistance Lv4）
 * アンロック：Lv.38
 */
public class SentinelSkill implements ISkill {

    private static final float STAMINA_COST    = 0f;
    private static final float MANA_COST       = 30f;
    private static final int   COOLDOWN_TICKS  = 120 * 20; // 120秒
    private static final int   CAST_TIME_TICKS = 0;
    private static final int   UNLOCK_LEVEL    = 38;
    private static final float RANGE           = 10f;

    @Override public int getId()            { return 7; }
    @Override public String getName()       { return "Sentinel"; }
    @Override public SkillType getType()    { return SkillType.MAGIC; }
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

        // 大軽減：Resistance Lv4（15秒）
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.RESISTANCE, 15 * 20, 3));

        level.playSound(null, x, y, z, SoundEvents.ARMOR_EQUIP_DIAMOND.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
        level.playSound(null, x, y, z, SoundEvents.BELL_BLOCK,                  SoundSource.PLAYERS, 0.6f, 1.3f);
    }
}
