package cloud.laboratory.n.micrafantasy.network;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.network.packet.SyncJobDataPayload;
import cloud.laboratory.n.micrafantasy.network.packet.UseSkillPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
public class ModNetwork {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MicrafantasyMod.MODID).versioned("1.0");
        // サーバー → クライアント: ジョブデータ同期
        registrar.playToClient(
                SyncJobDataPayload.TYPE,
                SyncJobDataPayload.CODEC,
                SyncJobDataPayload::handleOnClient
        );
        // クライアント → サーバー: スキル使用
        registrar.playToServer(
                UseSkillPayload.TYPE,
                UseSkillPayload.CODEC,
                UseSkillPayload::handleOnServer
        );
    }
    public static void sendToPlayer(ServerPlayer player, SyncJobDataPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }
}
