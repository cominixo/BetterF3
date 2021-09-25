package me.cominixo.betterf3.mixin.chunk;

import net.minecraft.client.render.BuiltChunkStorage;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Accesses the World Renderer.
 */
@SuppressWarnings("checkstyle:MethodName")
@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {


    /**
     * Gets the world renderer view area.
     *
     * @return the view area
     */
    @Accessor
    BuiltChunkStorage getChunks();

    /**
     * Gets rendered chunk count.
     *
     * @return Rendered chunks
     */
    @Invoker
    int callGetCompletedChunkCount();

    /**
     * Gets the view distance.
     *
     * @return the view distance
     */
    @Accessor
    int getViewDistance();

    /**
     * Gets the chunk render dispatcher.
     *
     * @return the chunk render dispatcher
     */
    @Accessor
    ChunkBuilder getChunkBuilder();

    /**
     * Gets the world (level).
     *
     * @return the world
     */
    @Accessor
    ClientWorld getWorld();

    /**
     * Gets the amount of rendered entities.
     *
     * @return the rendered entities
     */
    @Accessor
    int getRegularEntityCount();
}
