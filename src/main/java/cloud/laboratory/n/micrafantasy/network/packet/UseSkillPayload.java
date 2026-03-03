package cloud.laboratory.n.micrafantasy.network.packet;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.skill.SkillRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UseSkillPayload(int skillId) implements CustomPacketPayload {

    public static final Type<UseSkillPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "use_skill"));

    public static final StreamCodec<FriendlyByteBuf, UseSkillPayload> CODEC =
            StreamCodec.of(
                    (buf, payload) -> buf.writeVarInt(payload.skillId()),
                    buf -> new UseSkillPayload(buf.readVarInt())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleOnServer(UseSkillPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                SkillRegistry.executeSkill(serverPlayer, payload.skillId());
            }
        });
    }
}
