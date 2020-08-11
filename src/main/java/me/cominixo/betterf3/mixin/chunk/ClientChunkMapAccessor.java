package me.cominixo.betterf3.mixin.chunk;

import net.minecraft.client.world.ClientChunkManager.ClientChunkMap;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.concurrent.atomic.AtomicReferenceArray;

@Mixin(ClientChunkMap.class)
public interface ClientChunkMapAccessor {

    @Accessor
    AtomicReferenceArray<WorldChunk> getChunks();

}
