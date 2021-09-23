package me.cominixo.betterf3.mixin.chunk;

import java.util.concurrent.atomic.AtomicReferenceArray;
import net.minecraft.client.multiplayer.ClientChunkCache.Storage;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * The Client Chunk Map Accessor.
 */
@SuppressWarnings("checkstyle:MethodName")
@Mixin(Storage.class)
public interface ClientChunkMapAccessor {

    /**
     * Gets the chunk array.
     *
     * @return Gets Chunks
     */
    @Accessor
    AtomicReferenceArray<LevelChunk> getChunks();

}
