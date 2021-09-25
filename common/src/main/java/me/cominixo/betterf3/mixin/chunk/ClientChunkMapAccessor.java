package me.cominixo.betterf3.mixin.chunk;

import java.util.concurrent.atomic.AtomicReferenceArray;
import net.minecraft.client.world.ClientChunkManager.ClientChunkMap;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * The Client Chunk Map Accessor.
 */
@SuppressWarnings("checkstyle:MethodName")
@Mixin(ClientChunkMap.class)
public interface ClientChunkMapAccessor {

    /**
     * Gets the chunk array.
     *
     * @return Gets Chunks
     */
    @Accessor
    AtomicReferenceArray<WorldChunk> getChunks();

}
