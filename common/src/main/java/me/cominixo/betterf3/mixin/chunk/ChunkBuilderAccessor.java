package me.cominixo.betterf3.mixin.chunk;

import java.util.Queue;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Access the Chunk Builder.
 */
@SuppressWarnings("checkstyle:MethodName")
@Mixin(ChunkRenderDispatcher.class)
public interface ChunkBuilderAccessor {

    /**
     * Gets the chunk batch count.
     *
     * @return The chunk batch.
     */
    @Accessor
    int getToBatchCount();

    /**
     * Gets the chunks to upload.
     *
     * @return The amount of chunks to upload.
     */
    @Accessor
    Queue<Runnable> getToUpload();

    /**
     * Gets the free buffer count.
     *
     * @return The free buffer count.
     */
    @Accessor
    int getFreeBufferCount();

}
