package me.cominixo.betterf3.mixin.chunk;

import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Queue;

@Mixin(ChunkRenderDispatcher.class)
public interface ChunkBuilderAccessor {

    @Accessor
    int getToBatchCount();

    @Accessor
    Queue<Runnable> getToUpload();

    @Accessor
    int getFreeBufferCount();

}
