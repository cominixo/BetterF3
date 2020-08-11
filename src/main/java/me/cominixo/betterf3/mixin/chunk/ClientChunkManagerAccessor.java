package me.cominixo.betterf3.mixin.chunk;

import net.minecraft.client.world.ClientChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientChunkManager.class)
public interface ClientChunkManagerAccessor {
    @Accessor
    ClientChunkManager.ClientChunkMap getChunks();
}
