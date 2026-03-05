package cloud.laboratory.n.micrafantasy.network.packet;

import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * サーバー → クライアント：ダメージフライテキスト表示パケット。
 * クライアント側で ActionBar にダメージ量を表示する。
 */
public record ShowDamagePayload(float damage, boolean isCritical) implements CustomPacketPayload {

    public static final Type<ShowDamagePayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "show_damage"));

    public static final StreamCodec<FriendlyByteBuf, ShowDamagePayload> CODEC =
            StreamCodec.of(
                    (buf, p) -> { buf.writeFloat(p.damage()); buf.writeBoolean(p.isCritical()); },
                    buf -> new ShowDamagePayload(buf.readFloat(), buf.readBoolean())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handleOnClient(ShowDamagePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            // ダメージ量を整数表示（小数第一位まで）
            int dmg = Math.round(payload.damage());
            String prefix = payload.isCritical() ? "§e★ CRITICAL  " : "§c⚔ ";
            String msg    = prefix + "§f" + dmg + " §7damage";

            mc.player.displayClientMessage(Component.literal(msg), true);
        });
    }
}

