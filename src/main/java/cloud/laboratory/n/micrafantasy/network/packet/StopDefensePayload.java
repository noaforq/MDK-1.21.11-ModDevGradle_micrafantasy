package cloud.laboratory.n.micrafantasy.network.packet;

import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.network.ModNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Client → Server: 防御キーを離した（構え解除）を通知する。
 */
public record StopDefensePayload() implements CustomPacketPayload {

    public static final Type<StopDefensePayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "stop_defense"));

    public static final StreamCodec<FriendlyByteBuf, StopDefensePayload> CODEC =
            StreamCodec.of((buf, p) -> {}, buf -> new StopDefensePayload());

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handleOnServer(StopDefensePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            // サーバー側の構え解除
            if (player.isUsingItem()) {
                player.stopUsingItem();
            }
            // クライアントに構え解除を通知
            ModNetwork.sendDefenseState(player, false, false);
        });
    }
}

