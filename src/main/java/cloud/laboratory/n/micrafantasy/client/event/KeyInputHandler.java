package cloud.laboratory.n.micrafantasy.client.event;

import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.client.keybind.ModKeyBindings;
import cloud.laboratory.n.micrafantasy.job.JobData;
import cloud.laboratory.n.micrafantasy.network.packet.StopDefensePayload;
import cloud.laboratory.n.micrafantasy.network.packet.UseSkillPayload;
import cloud.laboratory.n.micrafantasy.registry.ModAttachmentTypes;
import cloud.laboratory.n.micrafantasy.skill.ISkill;
import cloud.laboratory.n.micrafantasy.skill.SkillRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShieldItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@EventBusSubscriber(modid = MicrafantasyMod.MODID, value = Dist.CLIENT)
public class KeyInputHandler {

    private static final int SLOT_DEFENSE = 10;

    public static int pressedSlot = -1;
    private static boolean wasDefenseKeyDown = false;

    // ① Pre：盾ありの場合 startUsingItem を毎tick維持
    @SubscribeEvent
    public static void onClientTickPre(ClientTickEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        boolean defenseKeyDown = ModKeyBindings.SKILL_KEYS[SLOT_DEFENSE].isDown();
        boolean shieldInOff  = mc.player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ShieldItem;
        boolean shieldInMain = mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ShieldItem;
        boolean hasShield    = shieldInOff || shieldInMain;

        if (defenseKeyDown && hasShield) {
            if (!mc.player.isUsingItem()) {
                InteractionHand hand = shieldInOff ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
                mc.player.startUsingItem(hand);
            }
        }
    }

    // ② Post：pressedSlot更新・パケット送信制御
    @SubscribeEvent
    public static void onClientTickPost(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) {
            pressedSlot       = -1;
            wasDefenseKeyDown = false;
            return;
        }

        pressedSlot = -1;
        for (int i = 0; i < ModKeyBindings.SKILL_KEYS.length; i++) {
            if (ModKeyBindings.SKILL_KEYS[i].isDown()) { pressedSlot = i; break; }
        }

        boolean defenseKeyDown = ModKeyBindings.SKILL_KEYS[SLOT_DEFENSE].isDown();

        if (defenseKeyDown && !wasDefenseKeyDown) {
            // 押した瞬間のみサーバーへ送信
            ClientPacketDistributor.sendToServer(
                    new UseSkillPayload(SLOT_DEFENSE, mc.player.getYRot(), mc.player.getXRot()));
        }
        if (!defenseKeyDown && wasDefenseKeyDown) {
            // 離した瞬間：サーバーに構え解除通知
            ClientPacketDistributor.sendToServer(new StopDefensePayload());
            if (mc.player.isUsingItem()) mc.player.stopUsingItem();
        }
        wasDefenseKeyDown = defenseKeyDown;
    }

    // ③ キー入力：防御スキル以外を処理
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        for (int i = 0; i < ModKeyBindings.SKILL_KEYS.length; i++) {
            if (i == SLOT_DEFENSE) { ModKeyBindings.SKILL_KEYS[i].consumeClick(); continue; }
            if (ModKeyBindings.SKILL_KEYS[i].consumeClick()) {
                triggerSwingMotion(mc.player, i);
                ClientPacketDistributor.sendToServer(
                        new UseSkillPayload(i, mc.player.getYRot(), mc.player.getXRot()));
            }
        }
    }

    private static void triggerSwingMotion(Player player, int slotId) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        JobData data = mc.player.getData(ModAttachmentTypes.JOB_DATA.get());
        ISkill skill = SkillRegistry.getSkill(data.getJobType(), slotId);
        if (skill == null) return;
        InteractionHand hand = skill.swingHand();
        if (hand != null) player.swing(hand);
    }
}
