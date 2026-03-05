package cloud.laboratory.n.micrafantasy.skill.whitemage;

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
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * 9: アサイラム（リミットブレイク）
 * 効果：自身HP全回復 ＋ 周囲全敵に大ダメージ ＋ 強力な全バフ
 * アンロック：Lv.1（リミットブレイク）
 */
public class AsylumSkill implements ISkill {

    private static final float STAMINA_COST    = 0f;
    private static final float MANA_COST       = 0f;
    private static final int   COOLDOWN_TICKS  = 0;   // リミットゲージで管理
    private static final int   CAST_TIME_TICKS = 0;
    private static final int   UNLOCK_LEVEL    = 1;
    private static final float DAMAGE          = 50f;
    private static final float RANGE           = 20f;

    @Override public int getId()            { return 9; }
    @Override public String getName()       { return "Asylum"; }
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

        // 自身HP全回復
        player.setHealth(player.getMaxHealth());

        // 周囲全敵に聖属性大ダメージ
        AABB area = player.getBoundingBox().inflate(RANGE);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area,
                e -> e != player && !e.isAlliedTo(player) && e.isAlive());
        for (LivingEntity target : targets) {
            target.hurtServer(level, player.damageSources().magic(), DAMAGE);
        }

        // 強力な全バフ
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.REGENERATION, 300, 4));
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.ABSORPTION,   300, 4));
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.RESISTANCE,   300, 2));
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.GLOWING,       60, 0));

        level.playSound(null, x, y, z, SoundEvents.BEACON_ACTIVATE,       SoundSource.PLAYERS, 1.5f, 1.5f);
        level.playSound(null, x, y, z, SoundEvents.TOTEM_USE,             SoundSource.PLAYERS, 1.0f, 1.2f);
        level.playSound(null, x, y, z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.0f, 1.8f);
    }
}
