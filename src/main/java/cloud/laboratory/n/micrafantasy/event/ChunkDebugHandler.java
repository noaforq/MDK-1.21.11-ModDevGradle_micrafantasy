package cloud.laboratory.n.micrafantasy.event;

import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;

/**
 * チャンク生成デバッグハンドラ
 * 新規チャンクがロードされたときに job_crystal_block の有無をログ出力する
 */
@EventBusSubscriber(modid = MicrafantasyMod.MODID)
public class ChunkDebugHandler {

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        // クライアント側は無視
        LevelAccessor level = event.getLevel();
        if (level.isClientSide()) return;

        // 新規生成チャンクのみ対象（既存チャンクは除外）
        if (!event.isNewChunk()) return;

        if (!(event.getChunk() instanceof LevelChunk chunk)) return;

        ChunkPos chunkPos = chunk.getPos();
        int count = 0;

        // チャンク内の全ブロックを走査してjob_crystal_blockを数える
        for (BlockPos pos : BlockPos.betweenClosed(
                chunkPos.getMinBlockX(), level.getMinY(), chunkPos.getMinBlockZ(),
                chunkPos.getMaxBlockX(), level.getMaxY(), chunkPos.getMaxBlockZ()
        )) {
            if (chunk.getBlockState(pos).is(ModBlocks.JOB_CRYSTAL_BLOCK.get())) {
                count++;
            }
        }

        if (count > 0) {
            MicrafantasyMod.LOGGER.info("[ChunkDebug] チャンク ({}, {}) に job_crystal_block が {}個 生成されました！",
                    chunkPos.x, chunkPos.z, count);
        } else {
            MicrafantasyMod.LOGGER.debug("[ChunkDebug] チャンク ({}, {}) に job_crystal_block は生成されませんでした",
                    chunkPos.x, chunkPos.z);
        }
    }
}

