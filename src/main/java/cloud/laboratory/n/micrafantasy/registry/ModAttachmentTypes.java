package cloud.laboratory.n.micrafantasy.registry;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.job.JobData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.function.Supplier;

public class ModAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MicrafantasyMod.MODID);

    public static final Supplier<AttachmentType<JobData>> JOB_DATA =
            ATTACHMENT_TYPES.register("job_data", () ->
                    AttachmentType.builder(JobData::new)
                            .serialize(new IAttachmentSerializer<JobData>() {
                                @Override
                                public JobData read(IAttachmentHolder holder, ValueInput input) {
                                    return input.read("data",
                                            net.minecraft.nbt.CompoundTag.CODEC)
                                            .map(JobData::fromNbt)
                                            .orElseGet(JobData::new);
                                }
                                @Override
                                public boolean write(JobData attachment, ValueOutput output) {
                                    output.store("data", net.minecraft.nbt.CompoundTag.CODEC, attachment.toNbt());
                                    return true;
                                }
                            })
                            .copyOnDeath()
                            .build()
            );
}
