package cloud.laboratory.n.micrafantasy.skill.whitemage;

import cloud.laboratory.n.micrafantasy.network.ModNetwork;
import cloud.laboratory.n.micrafantasy.skill.ISkill;
import cloud.laboratory.n.micrafantasy.skill.SkillHelper;
import cloud.laboratory.n.micrafantasy.skill.SkillType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * 4: ストンラ（魔法スキル）
 * 効果：周囲の全敵に範囲魔法ダメージ
 * アンロック：Lv.12
 * 詠唱時間：2秒（40tick）
 */
public class StonraSkill implements ISkill {

    private static final float STAMINA_COST    = 0f;
    private static final float MANA_COST       = 35f;
    private static final int   COOLDOWN_TICKS  = 5 * 20; // 5秒
    private static final int   CAST_TIME_TICKS = 40;     // 2秒
    private static final int   UNLOCK_LEVEL    = 12;
    private static final float DAMAGE_MULTIPLIER = 1.8f;
    private static final float RANGE           = 8f;

    @Override public int getId()            { return 4; }
    @Override public String getName()       { return "Stonra"; }
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

        if (targets.isEmpty()) {
            SkillHelper.hitBlockInSight(player, RANGE);
        } else {
            for (LivingEntity target : targets) {
                boolean hit = target.hurtServer(level, player.damageSources().magic(), damage);
                if (hit) {
                    level.playSound(null, x, y, z, SoundEvents.STONE_BREAK, SoundSource.PLAYERS, 1.0f, 0.7f);
                    ModNetwork.sendDamage(player, damage, true);
                }
            }
        }
    }
}
