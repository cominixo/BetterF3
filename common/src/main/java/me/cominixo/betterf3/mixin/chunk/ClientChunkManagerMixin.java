package me.cominixo.betterf3.mixin.chunk;

import me.cominixo.betterf3.ducks.ClientChunkManagerAccess;
import net.minecraft.client.world.ClientChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Mixin to access volatile "chunks" field in ClientChunkManager.
 */
@Mixin(ClientChunkManager.class)
public class ClientChunkManagerMixin implements ClientChunkManagerAccess {
    @Shadow private volatile ClientChunkManager.ClientChunkMap chunks;

    @Override
    public ClientChunkManager.ClientChunkMap getChunks() {
        return this.chunks;
    }
}
