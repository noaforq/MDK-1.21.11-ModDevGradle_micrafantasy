package cloud.laboratory.n.micrafantasy.network;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.network.packet.DefenseStatePayload;
import cloud.laboratory.n.micrafantasy.network.packet.ShowDamagePayload;
import cloud.laboratory.n.micrafantasy.network.packet.StopDefensePayload;
import cloud.laboratory.n.micrafantasy.network.packet.SyncJobDataPayload;
import cloud.laboratory.n.micrafantasy.network.packet.UseSkillPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
public class ModNetwork {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MicrafantasyMod.MODID).versioned("1.0");
        registrar.playToClient(SyncJobDataPayload.TYPE, SyncJobDataPayload.CODEC, SyncJobDataPayload::handleOnClient);
        registrar.playToClient(ShowDamagePayload.TYPE,  ShowDamagePayload.CODEC,  ShowDamagePayload::handleOnClient);
        registrar.playToClient(DefenseStatePayload.TYPE, DefenseStatePayload.CODEC, DefenseStatePayload::handleOnClient);
        registrar.playToServer(UseSkillPayload.TYPE,    UseSkillPayload.CODEC,    UseSkillPayload::handleOnServer);
        registrar.playToServer(StopDefensePayload.TYPE, StopDefensePayload.CODEC, StopDefensePayload::handleOnServer);
    }
    public static void sendToPlayer(ServerPlayer player, SyncJobDataPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }
    public static void sendDamage(ServerPlayer player, float damage, boolean isCritical) {
        PacketDistributor.sendToPlayer(player, new ShowDamagePayload(damage, isCritical));
    }
    public static void sendDefenseState(ServerPlayer player, boolean defending, boolean shieldInOffHand) {
        PacketDistributor.sendToPlayer(player, new DefenseStatePayload(defending, shieldInOffHand));
    }
}

