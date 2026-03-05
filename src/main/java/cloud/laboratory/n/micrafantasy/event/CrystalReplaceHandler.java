package cloud.laboratory.n.micrafantasy.event;

import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.config.MicrafantasyConfig;
import cloud.laboratory.n.micrafantasy.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;

import java.util.List;
import java.util.Set;

/**
 * チャンクロード時に既存ブロックをランダムでクリスタルブロックに置き換えるハンドラ。
 *
 * config.properties で制御：
 *   crystal_replace_enabled       : 有効/無効
 *   crystal_replace_chance        : 置換確率（0.0〜1.0）
 *   crystal_replace_min_y         : 置換対象Y最小
 *   crystal_replace_max_y         : 置換対象Y最大
 *   crystal_replace_max_per_chunk : 1チャンク当たりの最大置換数（0=無制限）
 *
 * 置換対象ブロック：石、深層岩、花崗岩、閃緑岩、安山岩、砂岩、赤砂岩、凝灰岩
 * ・既にクリスタルブロックが存在するチャンクは置換をスキップ（重複防止）
 */
@EventBusSubscriber(modid = MicrafantasyMod.MODID)
public class CrystalReplaceHandler {

    /** 置換対象のブロック種別 */
    private static final Set<net.minecraft.world.level.block.Block> REPLACEABLE = Set.of(
            Blocks.STONE,
            Blocks.DEEPSLATE,
            Blocks.GRANITE,
            Blocks.DIORITE,
            Blocks.ANDESITE,
            Blocks.SANDSTONE,
            Blocks.RED_SANDSTONE,
            Blocks.TUFF
    );

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!MicrafantasyConfig.isCrystalReplaceEnabled()) return;
        LevelAccessor level = event.getLevel();
        if (level.isClientSide()) return;
        if (!(event.getChunk() instanceof LevelChunk chunk)) return;
        if (level instanceof ServerLevel serverLevel) {
            replaceInChunk(serverLevel, chunk);
        }
    }

    /**
     * 指定チャンク内のブロックをクリスタルに置換する。
     * ChunkEvent.Load および DebugEventHandler から呼ばれる。
     */
    public static void replaceInChunk(ServerLevel level, LevelChunk chunk) {
        if (!MicrafantasyConfig.isCrystalReplaceEnabled()) return;

        ChunkPos chunkPos = chunk.getPos();
        int minY = Math.max(level.getMinY(), MicrafantasyConfig.getCrystalReplaceMinY());
        int maxY = Math.min(level.getMaxY(), MicrafantasyConfig.getCrystalReplaceMaxY());
        float chance = MicrafantasyConfig.getCrystalReplaceChance();
        int maxPerChunk = MicrafantasyConfig.getCrystalReplaceMaxPerChunk();

        RandomSource rng = level.getRandom();
        BlockState crystalState = ModBlocks.JOB_CRYSTAL_BLOCK.get().defaultBlockState();
        BlockState stoneState   = Blocks.STONE.defaultBlockState();

        // ── フェーズ1：既存クリスタルブロックをすべて石に戻す ──
        int reverted = 0;
        for (BlockPos pos : BlockPos.betweenClosed(
                chunkPos.getMinBlockX(), minY, chunkPos.getMinBlockZ(),
                chunkPos.getMaxBlockX(), maxY, chunkPos.getMaxBlockZ()
        )) {
            if (chunk.getBlockState(pos).is(ModBlocks.JOB_CRYSTAL_BLOCK.get())) {
                chunk.setBlockState(pos.immutable(), stoneState, 11);
                reverted++;
            }
        }
        if (reverted > 0) {
            chunk.markUnsaved();
            MicrafantasyMod.LOGGER.info(
                    "[CrystalReplace] チャンク ({}, {}) で {} 個のクリスタルを石に戻しました",
                    chunkPos.x, chunkPos.z, reverted);
        }

        // ── フェーズ2：石系ブロックをランダムでクリスタルに置換 ──
        int replaced = 0;
        BlockPos firstPos = null;
        for (BlockPos pos : BlockPos.betweenClosed(
                chunkPos.getMinBlockX(), minY, chunkPos.getMinBlockZ(),
                chunkPos.getMaxBlockX(), maxY, chunkPos.getMaxBlockZ()
        )) {
            if (maxPerChunk > 0 && replaced >= maxPerChunk) break;
            BlockState current = chunk.getBlockState(pos);
            if (!REPLACEABLE.contains(current.getBlock())) continue;
            if (rng.nextFloat() >= chance) continue;

            BlockPos immPos = pos.immutable();
            chunk.setBlockState(immPos, crystalState, 11);
            if (firstPos == null) firstPos = immPos;
            replaced++;
        }

        if (replaced > 0) {
            chunk.markUnsaved();
            MicrafantasyMod.LOGGER.info(
                    "[CrystalReplace] チャンク ({}, {}) で {} 個をクリスタルに置換 (Y:{} ~ Y:{}) 最初の座標: {}",
                    chunkPos.x, chunkPos.z, replaced, minY, maxY, firstPos);

            // 全プレイヤーのチャットに[Hydaelyn]メッセージとして座標を通知
            List<ServerPlayer> players = level.getServer().getPlayerList().getPlayers();
            if (!players.isEmpty() && firstPos != null) {
                BlockPos fp = firstPos;
                String msg = String.format(
                        "§d§l[Hydaelyn]§r§d Hear... Feel... Think...。§f(%d, %d, %d)",
                        fp.getX(), fp.getY(), fp.getZ());
                for (ServerPlayer p : players) {
                    p.displayClientMessage(Component.literal(msg), false);
                }
            }
        }
    }
}

