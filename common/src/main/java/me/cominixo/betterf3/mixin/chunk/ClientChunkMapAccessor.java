package me.cominixo.betterf3.mixin.chunk;

import net.minecraft.client.multiplayer.ClientChunkCache.Storage;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * The Client Chunk Map Accessor
 */
@Mixin(Storage.class)
public interface ClientChunkMapAccessor {

    /**
     * @return Gets Chunks
     */
    @Accessor
    AtomicReferenceArray<LevelChunk> getChunks();

}
