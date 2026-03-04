package cloud.laboratory.n.micrafantasy.network.packet;

import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Server → Client: 防御構え開始/終了を通知する。
 * defending=true  → startUsingItem（盾構え開始）
 * defending=false → stopUsingItem（盾構え解除）
 */
public record DefenseStatePayload(boolean defending, boolean shieldInOffHand)
        implements CustomPacketPayload {

    public static final Type<DefenseStatePayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "defense_state"));

    public static final StreamCodec<FriendlyByteBuf, DefenseStatePayload> CODEC =
            StreamCodec.of(
                    (buf, p) -> { buf.writeBoolean(p.defending()); buf.writeBoolean(p.shieldInOffHand()); },
                    buf -> new DefenseStatePayload(buf.readBoolean(), buf.readBoolean())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handleOnClient(DefenseStatePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            if (payload.defending()) {
                // 盾構え開始：盾を持っている手で startUsingItem
                InteractionHand hand = payload.shieldInOffHand()
                        ? InteractionHand.OFF_HAND
                        : InteractionHand.MAIN_HAND;
                if (!mc.player.isUsingItem()) {
                    mc.player.startUsingItem(hand);
                }
            } else {
                // 構え解除
                if (mc.player.isUsingItem()) {
                    mc.player.stopUsingItem();
                }
            }
        });
    }
}

