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

/**
 * 3: クレメンシー（魔法スキル）
 * 効果：回復＋継続回復（Regeneration）
 * アンロック：Lv.5
 * 詠唱時間：1.5秒（30tick）
 */
public class ClemencySkill implements ISkill {

    private static final float STAMINA_COST    = 0f;
    private static final float MANA_COST       = 40f;
    private static final int   COOLDOWN_TICKS  = 8 * 20;  // 8秒
    private static final int   CAST_TIME_TICKS = 30;      // 1.5秒
    private static final int   UNLOCK_LEVEL    = 1;
    private static final float HEAL            = 10f;

    @Override public int getId()            { return 3; }
    @Override public String getName()       { return "Clemency"; }
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

        // 即時回復
        player.heal(HEAL);
        // 継続回復：Regeneration Lv2（10秒）
        EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.REGENERATION, 10 * 20, 1));

        level.playSound(null, x, y, z, SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.5f, 1.8f);
    }
}
