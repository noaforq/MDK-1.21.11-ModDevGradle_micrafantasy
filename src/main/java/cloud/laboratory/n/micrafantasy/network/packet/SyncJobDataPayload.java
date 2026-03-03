package cloud.laboratory.n.micrafantasy.network.packet;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.job.JobData;
import cloud.laboratory.n.micrafantasy.registry.ModAttachmentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncJobDataPayload(CompoundTag nbt) implements CustomPacketPayload {

    public static final Type<SyncJobDataPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "sync_job_data"));

    public static final StreamCodec<FriendlyByteBuf, SyncJobDataPayload> CODEC =
            StreamCodec.of(
                    (buf, payload) -> buf.writeNbt(payload.nbt()),
                    buf -> new SyncJobDataPayload(buf.readNbt())
            );

    public SyncJobDataPayload(JobData data) {
        this(data.toNbt());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleOnClient(SyncJobDataPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                JobData data = JobData.fromNbt(payload.nbt());
                mc.player.setData(ModAttachmentTypes.JOB_DATA.get(), data);
            }
        });
    }
}
