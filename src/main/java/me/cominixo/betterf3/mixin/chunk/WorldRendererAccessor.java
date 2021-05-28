package me.cominixo.betterf3.mixin.chunk;

import net.minecraft.client.render.BuiltChunkStorage;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {


    @Accessor
    BuiltChunkStorage getChunks();

    @Invoker
    int callGetCompletedChunkCount();

    @Accessor
    int getViewDistance();

    @Accessor
    ChunkBuilder getChunkBuilder();

    @Accessor
    ClientWorld getWorld();

    @Accessor
    int getRegularEntityCount();



}
