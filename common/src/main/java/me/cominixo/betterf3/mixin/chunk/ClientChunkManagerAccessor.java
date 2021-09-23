package me.cominixo.betterf3.mixin.chunk;

import net.minecraft.client.multiplayer.ClientChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientChunkCache.class)
public interface ClientChunkManagerAccessor {
    @Accessor
    ClientChunkCache.Storage getStorage();
}
