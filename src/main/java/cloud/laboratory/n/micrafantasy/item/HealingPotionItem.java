package cloud.laboratory.n.micrafantasy.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

/**
 * 回復ポーション（ジョブ依存なし）
 * クラフト：薬草 + 水バケツ + クリスタルの欠片 → 3個
 * 使用するとHPを4ハート回復する。スタック16個まで。
 */
public class HealingPotionItem extends Item {

    private static final float HEAL_AMOUNT = 8.0f; // 4ハート

    public HealingPotionItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32; // 約1.6秒
    }

    /** 右クリックで使用開始 */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    /** 使用完了時にHP回復 + ガラス瓶を返却 */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide() && entity instanceof ServerPlayer player) {
            player.heal(HEAL_AMOUNT);
            player.sendSystemMessage(
                    Component.translatable("message.micrafantasy.potion_used",
                            (int)(HEAL_AMOUNT / 2)));
            // ガラス瓶を返却
            ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
            if (!player.addItem(bottle)) {
                player.drop(bottle, false);
            }
        }
        stack.shrink(1);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder,
                                TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.micrafantasy.healing_potion.tooltip",
                (int)(HEAL_AMOUNT / 2)));
    }
}
