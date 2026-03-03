package cloud.laboratory.n.micrafantasy.skill;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

/**
 * バフ・デバフ適用時に視覚エフェクト＋サウンドを自動付与するヘルパー
 *
 * <p>使い方：
 * <pre>
 *   EffectHelper.applyBuff(player, new MobEffectInstance(MobEffects.STRENGTH, 200, 1));
 *   EffectHelper.applyDebuff(level, target, new MobEffectInstance(MobEffects.SLOWNESS, 60, 2));
 * </pre>
 */
public final class EffectHelper {

    private EffectHelper() {}

    // ----------------------------------------------------------------
    // バフ（対象はプレイヤー自身など BENEFICIAL / NEUTRAL エフェクト）
    // パーティクル: HAPPY_VILLAGER（緑のキラキラ）
    // サウンド     : AMBIENT_CAVE（柔らかい発動音）
    // ----------------------------------------------------------------

    /**
     * バフを適用し、発動パーティクル＋サウンドを出す。
     *
     * @param target 適用対象エンティティ
     * @param effect 付与するエフェクト
     */
    public static void applyBuff(LivingEntity target, MobEffectInstance effect) {
        target.addEffect(effect);
        if (!(target.level() instanceof ServerLevel level)) return;
        double x = target.getX(), y = target.getY() + target.getBbHeight() * 0.5, z = target.getZ();

        // 緑のキラキラパーティクル（ベネフィシャル系）
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                x, y, z,
                12,           // count
                0.4, 0.5, 0.4,// offset x/y/z
                0.1           // speed
        );
        level.playSound(null, x, y, z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.6f, 1.4f);
    }

    // ----------------------------------------------------------------
    // デバフ（対象は敵エンティティ など HARMFUL エフェクト）
    // パーティクル: ANGRY_VILLAGER（赤い煙）
    // サウンド     : ELDER_GUARDIAN_CURSE（重い不快音）
    // ----------------------------------------------------------------

    /**
     * デバフを適用し、発動パーティクル＋サウンドを出す。
     *
     * @param level  サーバーレベル
     * @param target 適用対象エンティティ
     * @param effect 付与するエフェクト
     */
    public static void applyDebuff(ServerLevel level, LivingEntity target, MobEffectInstance effect) {
        target.addEffect(effect);
        double x = target.getX(), y = target.getY() + target.getBbHeight() * 0.5, z = target.getZ();

        // 赤い煙パーティクル（ハーム系）
        level.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                x, y, z,
                8,            // count
                0.3, 0.4, 0.3,// offset x/y/z
                0.05          // speed
        );
        level.playSound(null, x, y, z,
                SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.PLAYERS, 0.5f, 1.2f);
    }

    // ----------------------------------------------------------------
    // 自動判別（エフェクトのカテゴリを見てバフ/デバフを切り替え）
    // ----------------------------------------------------------------

    /**
     * エフェクトのカテゴリを自動判別してバフ/デバフを適用する。
     * BENEFICIAL / NEUTRAL → {@link #applyBuff}
     * HARMFUL              → {@link #applyDebuff}
     *
     * @param level  サーバーレベル（デバフ時に使用）
     * @param target 適用対象エンティティ
     * @param effect 付与するエフェクト
     */
    public static void apply(ServerLevel level, LivingEntity target, MobEffectInstance effect) {
        MobEffect effectType = effect.getEffect().value();
        if (effectType.getCategory() == MobEffectCategory.HARMFUL) {
            applyDebuff(level, target, effect);
        } else {
            applyBuff(target, effect);
        }
    }
}

