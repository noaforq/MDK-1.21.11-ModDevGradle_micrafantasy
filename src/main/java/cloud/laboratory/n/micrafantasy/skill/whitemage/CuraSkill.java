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
 * 5: ケアルラ（魔法スキル）
 * 効果：自身を大回復＋Regeneration Lv2（強）
 * アンロック：Lv.20
 * 詠唱時間：2秒（40tick）
 */
public class CuraSkill implements ISkill {

    private static final float STAMINA_COST    = 0f;
    private static final float MANA_COST       = 50f;
    private static final int   COOLDOWN_TICKS  = 10 * 20; // 10秒
    private static final int   CAST_TIME_TICKS = 40;      // 2秒
    private static final int   UNLOCK_LEVEL    = 20;
    private static final float HEAL            = 18f;

    @Override public int getId()            { return 5; }
    @Override public String getName()       { return "Cura"; }
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
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.REGENERATION, 12 * 20, 1));
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.ABSORPTION,   30 * 20, 1));

        level.playSound(null, x, y, z, SoundEvents.PLAYER_LEVELUP,  SoundSource.PLAYERS, 0.6f, 1.8f);
        level.playSound(null, x, y, z, SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS, SoundSource.PLAYERS, 0.4f, 2.0f);
    }
}
