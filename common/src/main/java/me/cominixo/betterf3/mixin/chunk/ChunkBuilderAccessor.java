package me.cominixo.betterf3.mixin.chunk;

import java.util.Queue;
import net.minecraft.client.render.chunk.ChunkBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Access the Chunk Builder.
 */
@SuppressWarnings("checkstyle:MethodName")
@Mixin(ChunkBuilder.class)
public interface ChunkBuilderAccessor {

    /**
     * Gets the chunk batch count.
     *
     * @return The chunk batch.
     */
    @Accessor
    int getQueuedTaskCount();

    /**
     * Gets the chunks to upload.
     *
     * @return The amount of chunks to upload.
     */
    @Accessor
    Queue<Runnable> getUploadQueue();

    /**
     * Gets the free buffer count.
     *
     * @return The free buffer count.
     */
    @Accessor
    int getBufferCount();

}
