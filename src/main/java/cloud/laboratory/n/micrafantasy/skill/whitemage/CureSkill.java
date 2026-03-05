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
 * 2: ケアル（魔法スキル）
 * 効果：自身を即時回復＋Regeneration Lv1
 * アンロック：Lv.1
 * 詠唱時間：1.5秒（30tick）
 */
public class CureSkill implements ISkill {

    private static final float STAMINA_COST    = 0f;
    private static final float MANA_COST       = 25f;
    private static final int   COOLDOWN_TICKS  = 4 * 20; // 4秒
    private static final int   CAST_TIME_TICKS = 30;     // 1.5秒
    private static final int   UNLOCK_LEVEL    = 1;
    private static final float HEAL            = 8f;

    @Override public int getId()            { return 2; }
    @Override public String getName()       { return "Cure"; }
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
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.REGENERATION, 8 * 20, 0));

        level.playSound(null, x, y, z, SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.4f, 2.0f);
    }
}
