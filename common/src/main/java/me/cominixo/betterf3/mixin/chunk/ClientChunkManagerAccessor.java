package me.cominixo.betterf3.mixin.chunk;

import net.minecraft.client.multiplayer.ClientChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * The Chunk Cache Accessor.
 */
@SuppressWarnings("checkstyle:MethodName")
@Mixin(ClientChunkCache.class)
public interface ClientChunkManagerAccessor {


    /**
     * Gets the chunk cache storage.
     *
     * @return The storage.
     */
    @Accessor
    ClientChunkCache.Storage getStorage();
}
