package me.cominixo.betterf3.mixin.chunk;

import java.util.concurrent.atomic.AtomicReferenceArray;
import me.cominixo.betterf3.ducks.ClientChunkMapAccess;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Mixin to access volatile "chunks" field in ClientChunkManager.
 */
@Mixin(ClientChunkManager.ClientChunkMap.class)
public class ClientChunkMapMixin implements ClientChunkMapAccess {

  @Final
  @Shadow AtomicReferenceArray<WorldChunk> chunks;

  @Override
  public AtomicReferenceArray<WorldChunk> getChunks() {
    return this.chunks;
  }
}
