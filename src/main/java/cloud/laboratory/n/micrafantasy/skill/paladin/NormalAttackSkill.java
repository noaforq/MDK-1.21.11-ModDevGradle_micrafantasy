package cloud.laboratory.n.micrafantasy.skill.paladin;

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
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

/**
 * 0: 通常攻撃（物理スキル）
 * 効果：視線方向の最近傍エンティティ1体に (baseAtk + weaponAtk) * 1.0 ダメージ
 * アンロック：Lv.1
 */
public class NormalAttackSkill implements ISkill {

    private static final float  STAMINA_COST    = 0f;
    private static final float  MANA_COST       = 0f;
    private static final int    COOLDOWN_TICKS  = 0;
    private static final int    CAST_TIME_TICKS = 0;
    private static final int    UNLOCK_LEVEL    = 1;
    private static final double REACH           = 3.5;

    @Override public int getId()            { return 0; }
    @Override public String getName()       { return "Attack"; }
    @Override public SkillType getType()    { return SkillType.PHYSICAL; }
    @Override public float getStaminaCost() { return STAMINA_COST; }
    @Override public float getManaCost()    { return MANA_COST; }
    @Override public int getCooldownTicks() { return COOLDOWN_TICKS; }
    @Override public int getCastTimeTicks() { return CAST_TIME_TICKS; }
    @Override public int getUnlockLevel()   { return UNLOCK_LEVEL; }

    @Override
    public void execute(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();

        // 視線レイキャストで対象を選択
        Vec3 eyePos  = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        Vec3 endPos  = eyePos.add(lookVec.scale(REACH));

        AABB searchBox = player.getBoundingBox().inflate(REACH);
        List<LivingEntity> candidates = level.getEntitiesOfClass(
                LivingEntity.class, searchBox,
                e -> e != player && !e.isAlliedTo(player) && e.isAlive());

        Optional<LivingEntity> opt = candidates.stream()
                .filter(e -> e.getBoundingBox().inflate(0.1).clip(eyePos, endPos).isPresent())
                .min(java.util.Comparator.comparingDouble(e -> e.distanceToSqr(player)));

        if (opt.isPresent()) {
            LivingEntity target = opt.get();
            float damage = SkillHelper.getPlayerBaseAttackDamage(player)
                         + SkillHelper.getWeaponAttackDamage(player);
            damage = Math.max(1f, damage);
            boolean hit = target.hurtServer(level, player.damageSources().playerAttack(player), damage);
            if (hit) {
                level.playSound(null, target.getX(), target.getY(), target.getZ(),
                        SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 1.0f, 1.0f);
                ModNetwork.sendDamage(player, damage, false);
            }
        } else {
            // Mob がいない → 視線方向のブロックを攻撃
            SkillHelper.hitBlockInSight(player, REACH);
        }
    }
}
