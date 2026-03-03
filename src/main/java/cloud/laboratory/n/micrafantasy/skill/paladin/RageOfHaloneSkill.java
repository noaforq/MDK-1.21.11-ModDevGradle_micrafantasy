package cloud.laboratory.n.micrafantasy.skill.paladin;

import cloud.laboratory.n.micrafantasy.skill.ISkill;
import cloud.laboratory.n.micrafantasy.skill.SkillHelper;
import cloud.laboratory.n.micrafantasy.skill.SkillType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * 6: レイジ・オブ・ハルオーネ（物理スキル）
 * 効果：範囲大攻撃
 * アンロック：Lv.26
 */
public class RageOfHaloneSkill implements ISkill {

    private static final float STAMINA_COST    = 25f;
    private static final float MANA_COST       = 0f;
    private static final int   COOLDOWN_TICKS  = 4 * 20;  // 4秒
    private static final int   CAST_TIME_TICKS = 0;
    private static final int   UNLOCK_LEVEL    = 26;
    private static final float DAMAGE_MULTIPLIER = 2.5f;  // 武器攻撃力 × 2.5
    private static final float RANGE           = 8f;

    @Override public int getId()            { return 6; }
    @Override public String getName()       { return "Rage of Halone"; }
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

        // 武器攻撃力 × 2.5
        float damage = SkillHelper.getWeaponDamage(player) * DAMAGE_MULTIPLIER;

        // 範囲大攻撃
        for (LivingEntity target : targets) {
            // 武器振りモーション
            player.swing(InteractionHand.MAIN_HAND);
            level.playSound(null, x, y, z, SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0f, 0.8f);
            level.playSound(null, x, y, z, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 0.8f, 0.6f);
            target.hurtServer(level, player.damageSources().playerAttack(player), damage);
        }
    }
}
