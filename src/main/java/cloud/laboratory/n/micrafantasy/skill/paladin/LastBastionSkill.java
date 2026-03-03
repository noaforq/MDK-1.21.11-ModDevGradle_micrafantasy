package cloud.laboratory.n.micrafantasy.skill.paladin;

import cloud.laboratory.n.micrafantasy.skill.EffectHelper;
import cloud.laboratory.n.micrafantasy.skill.ISkill;
import cloud.laboratory.n.micrafantasy.skill.SkillType;
import cloud.laboratory.n.micrafantasy.skill.TerrainDamageHelper;
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
 * 9: ラスト・バスティオン（リミットブレイク）
 * 効果：敵対心アップ＋超攻撃＋超軽減＋超バフ
 * アンロック：Lv.50（リミットブレイク）
 */
public class LastBastionSkill implements ISkill {

    private static final float STAMINA_COST    = 0f;
    private static final float MANA_COST       = 0f;
    private static final int   COOLDOWN_TICKS  = 0;   // リミットゲージで管理
    private static final int   CAST_TIME_TICKS = 0;
    private static final int   UNLOCK_LEVEL    = 50;
    private static final float DAMAGE          = 40f;
    private static final float RANGE           = 20f;

    @Override public int getId()            { return 9; }
    @Override public String getName()       { return "Last Bastion"; }
    @Override public SkillType getType()    { return SkillType.LIMIT_BREAK; }
    @Override public float getStaminaCost() { return STAMINA_COST; }
    @Override public float getManaCost()    { return MANA_COST; }
    @Override public int getCooldownTicks() { return COOLDOWN_TICKS; }
    @Override public int getCastTimeTicks() { return CAST_TIME_TICKS; }
    @Override public int getUnlockLevel()   { return UNLOCK_LEVEL; }

    @Override
    public void execute(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        double x = player.getX(), y = player.getY() + 1, z = player.getZ();

        AABB area = player.getBoundingBox().inflate(RANGE);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area,
                e -> e != player && !e.isAlliedTo(player));

        // 敵対心アップ（全敵をプレイヤーへ向ける）
        for (LivingEntity entity : targets) {
            if (entity instanceof Mob mob) mob.setTarget(player);
        }

        // 超攻撃
        for (LivingEntity target : targets) {
            target.hurtServer(level, player.damageSources().playerAttack(player), DAMAGE);
            TerrainDamageHelper.applyTerrainDamage(level, target, DAMAGE);
        }

        // 超軽減：Resistance Lv5（200tick）
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.RESISTANCE,   200, 4));
        // 超バフ：Regeneration Lv3（200tick）+ Strength Lv3（200tick）+ Haste Lv3（200tick）
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.REGENERATION, 200, 2));
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.STRENGTH,     200, 2));
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.HASTE,        200, 2));

        level.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.5f, 0.8f);
        level.playSound(null, x, y, z, SoundEvents.LIGHTNING_BOLT_THUNDER,  SoundSource.PLAYERS, 1.0f, 1.2f);
    }
}
