package me.cominixo.betterf3.mixin.chunk;

import net.minecraft.client.render.chunk.ChunkBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Queue;

@Mixin(ChunkBuilder.class)
public interface ChunkBuilderAccessor {

    @Accessor
    int getQueuedTaskCount();

    @Accessor
    Queue<Runnable> getUploadQueue();

    @Accessor
    int getBufferCount();

}
