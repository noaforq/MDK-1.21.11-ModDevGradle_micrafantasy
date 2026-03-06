package cloud.laboratory.n.micrafantasy.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

/** クリスタルの欠片 - ポーションの素材 */
public class CrystalShardItem extends Item {
    public CrystalShardItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder,
                                TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.micrafantasy.crystal_shard.tooltip"));
    }
}
