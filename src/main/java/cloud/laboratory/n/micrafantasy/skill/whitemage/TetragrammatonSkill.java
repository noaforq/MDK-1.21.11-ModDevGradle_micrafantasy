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

/**
 * 7: テトラグラマトン（魔法スキル）
 * 効果：即時大回復（詠唱不要・クールダウンのみ）
 * アンロック：Lv.45
 */
public class TetragrammatonSkill implements ISkill {

    private static final float STAMINA_COST    = 0f;
    private static final float MANA_COST       = 40f;
    private static final int   COOLDOWN_TICKS  = 60 * 20; // 60秒
    private static final int   CAST_TIME_TICKS = 0;
    private static final int   UNLOCK_LEVEL    = 45;
    private static final float HEAL            = 30f;

    @Override public int getId()            { return 7; }
    @Override public String getName()       { return "Tetragrammaton"; }
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

        player.heal(HEAL);
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.REGENERATION, 15 * 20, 2));
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.ABSORPTION,   60 * 20, 3));

        level.playSound(null, x, y, z, SoundEvents.TOTEM_USE,     SoundSource.PLAYERS, 0.8f, 1.8f);
        level.playSound(null, x, y, z, SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 2.0f);
    }
}
