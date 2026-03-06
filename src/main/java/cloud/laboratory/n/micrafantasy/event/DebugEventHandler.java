package cloud.laboratory.n.micrafantasy.event;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.config.MicrafantasyConfig;
import cloud.laboratory.n.micrafantasy.job.JobData;
import cloud.laboratory.n.micrafantasy.job.JobType;
import cloud.laboratory.n.micrafantasy.network.ModNetwork;
import cloud.laboratory.n.micrafantasy.network.packet.SyncJobDataPayload;
import cloud.laboratory.n.micrafantasy.registry.ModAttachmentTypes;
import cloud.laboratory.n.micrafantasy.registry.ModBlocks;
import cloud.laboratory.n.micrafantasy.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@EventBusSubscriber(modid = MicrafantasyMod.MODID)
public class DebugEventHandler {
    private static final Map<UUID, Integer> pendingGive = new HashMap<>();
    private static final int GIVE_DELAY_TICKS = 20;
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (pendingGive.isEmpty()) return;
        Map<UUID, Integer> copy = new HashMap<>(pendingGive);
        for (Map.Entry<UUID, Integer> entry : copy.entrySet()) {
            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
                pendingGive.remove(entry.getKey());
                ServerPlayer player = event.getServer().getPlayerList().getPlayer(entry.getKey());
                if (player != null) applyDebug(player);
            } else {
                pendingGive.put(entry.getKey(), remaining);
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!MicrafantasyConfig.isDebugMode()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        pendingGive.put(player.getUUID(), GIVE_DELAY_TICKS);
        MicrafantasyMod.LOGGER.info("[Debug] Queued debug apply for {} ({}tick delay)", player.getName().getString(), GIVE_DELAY_TICKS);
    }
    private static void applyDebug(ServerPlayer player) {
        int targetLevel = Math.max(1, MicrafantasyConfig.getDebugJobLevel());
        giveItemIfNotHeld(player, new ItemStack(ModItems.PALADIN_JOB_STONE.get()));
        giveItemIfNotHeld(player, new ItemStack(ModItems.WHITE_MAGE_JOB_STONE.get()));
        giveStack(player, new ItemStack(ModItems.HERB.get(), 16));
        giveStack(player, new ItemStack(ModItems.CRYSTAL_SHARD.get(), 16));
        giveStack(player, new ItemStack(Items.GLASS_BOTTLE, 16));
        if (player.level() instanceof ServerLevel serverLevel) {
            // crystal_ore を足元 1ブロック下に 3×3 設置
            BlockPos oreBase = player.blockPosition().below();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    serverLevel.setBlock(oreBase.offset(dx, 0, dz), ModBlocks.CRYSTAL_ORE.get().defaultBlockState(), 3);
                }
            }
            MicrafantasyMod.LOGGER.info("[Debug] Placed crystal_ore blocks around {} at {}", player.getName().getString(), oreBase);
            // job_crystal_block を足元 2ブロック上（地上）に 3×3 設置
            BlockPos crystalBase = player.blockPosition().above(2);
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    serverLevel.setBlock(crystalBase.offset(dx, 0, dz), ModBlocks.JOB_CRYSTAL_BLOCK.get().defaultBlockState(), 3);
                }
            }
            MicrafantasyMod.LOGGER.info("[Debug] Placed job_crystal_block around {} at {}", player.getName().getString(), crystalBase);
        }
        JobData data = player.getData(ModAttachmentTypes.JOB_DATA.get());
        if (data.getJobType() != JobType.NONE) {
            data.setLevel(targetLevel);
            data.setExperience(0);
            data.setTotalExperience(0);
            int n = targetLevel * 10;
            data.setExperienceToNextLevel(n * n);
            data.setStamina(data.getMaxStamina());
            data.setMana(data.getMaxMana());
            ModNetwork.sendToPlayer(player, new SyncJobDataPayload(data));
        }
        MicrafantasyMod.LOGGER.info("[Debug] Applied debug to {}: level={}", player.getName().getString(), targetLevel);
    }
    private static void giveItemIfNotHeld(ServerPlayer player, ItemStack stack) {
        boolean alreadyHas = player.getInventory().hasAnyOf(java.util.Set.of(stack.getItem()));
        if (!alreadyHas) {
            if (!player.addItem(stack)) player.drop(stack, false);
        }
    }
    private static void giveStack(ServerPlayer player, ItemStack stack) {
        if (!player.addItem(stack)) player.drop(stack, false);
    }
}
