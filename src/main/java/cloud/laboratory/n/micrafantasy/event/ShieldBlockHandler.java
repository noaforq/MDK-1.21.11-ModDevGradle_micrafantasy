package cloud.laboratory.n.micrafantasy.event;

import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/**
 * 盾パッシブ防御ハンドラ
 *
 * 盾を装備（メインハンド or オフハンド）しているプレイヤーが
 * 攻撃を受けたとき、一定確率でダメージを軽減する。
 *
 * 発動条件：
 *   - サーバーサイドのプレイヤーのみ対象
 *   - メインハンド or オフハンドに ShieldItem を所持
 *   - ブロッキング中（use状態）でなくてもパッシブ発動
 *
 * 発動率：25%
 * 軽減率：50%
 */
@EventBusSubscriber(modid = MicrafantasyMod.MODID)
public class ShieldBlockHandler {

    /** 盾パッシブ発動確率 */
    private static final float BLOCK_CHANCE    = 0.25f; // 25%
    /** ダメージ軽減率 */
    private static final float DAMAGE_REDUCTION = 0.50f; // 50%

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        // サーバーサイドのプレイヤーのみ
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // 盾を所持しているか確認（メインハンド or オフハンド）
        if (!hasShield(player)) return;

        // 確率判定
        RandomSource rng = player.level().getRandom();
        if (rng.nextFloat() >= BLOCK_CHANCE) return;

        // ダメージを50%に軽減
        float original = event.getAmount();
        float reduced  = original * (1.0f - DAMAGE_REDUCTION);
        event.setAmount(reduced);

        // 視覚・音響フィードバック
        playBlockEffect(player);

        MicrafantasyMod.LOGGER.debug(
                "[Shield] {} の盾パッシブ発動：ダメージ {} → {}",
                player.getName().getString(), original, reduced);
    }

    /** メインハンドまたはオフハンドに盾を持っているか */
    private static boolean hasShield(ServerPlayer player) {
        ItemStack main = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack off  = player.getItemInHand(InteractionHand.OFF_HAND);
        return main.getItem() instanceof ShieldItem
                || off.getItem()  instanceof ShieldItem;
    }

    /** 盾ブロック発動時のパーティクル＋サウンド */
    private static void playBlockEffect(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        double x = player.getX();
        double y = player.getY() + player.getBbHeight() * 0.5;
        double z = player.getZ();

        // パーティクル（星のキラキラ）
        level.sendParticles(ParticleTypes.CRIT,
                x, y, z,
                6,           // count
                0.3, 0.3, 0.3, // offset
                0.1);          // speed

        // サウンド1：盾ブロック音（メイン）
        level.playSound(null, x, y, z,
                SoundEvents.SHIELD_BLOCK.value(),
                SoundSource.PLAYERS,
                1.0f, 1.0f + (level.getRandom().nextFloat() - 0.5f) * 0.2f);
        // サウンド2：金属的な重い発動音
        level.playSound(null, x, y, z,
                SoundEvents.ARMOR_EQUIP_IRON.value(),
                SoundSource.PLAYERS,
                0.6f, 1.6f);
    }
}






