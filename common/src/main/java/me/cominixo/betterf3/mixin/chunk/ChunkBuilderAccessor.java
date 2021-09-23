package me.cominixo.betterf3.mixin.chunk;

import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Queue;

/**
 * Access the Chunk Builder
 */
@Mixin(ChunkRenderDispatcher.class)
public interface ChunkBuilderAccessor {

    /**
     * @return The chunk batch.
     */
    @Accessor
    int getToBatchCount();

    /**
     * @return The amount of chunks to upload.
     */
    @Accessor
    Queue<Runnable> getToUpload();

    /**
     * @return The free buffer count.
     */
    @Accessor
    int getFreeBufferCount();

}
