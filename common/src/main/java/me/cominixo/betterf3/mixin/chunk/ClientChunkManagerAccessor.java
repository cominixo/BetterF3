package me.cominixo.betterf3.mixin.chunk;

import net.minecraft.client.world.ClientChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * The Chunk Cache Accessor.
 */
@SuppressWarnings("checkstyle:MethodName")
@Mixin(ClientChunkManager.class)
public interface ClientChunkManagerAccessor {


    /**
     * Gets the chunk cache storage.
     *
     * @return The storage.
     */
    @Accessor
    ClientChunkManager.ClientChunkMap getChunks();
}
