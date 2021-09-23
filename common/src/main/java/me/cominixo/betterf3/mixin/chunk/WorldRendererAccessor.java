package me.cominixo.betterf3.mixin.chunk;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ViewArea;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Accesses the World Renderer
 */
@Mixin(LevelRenderer.class)
public interface WorldRendererAccessor {


    /**
     * @return the view area
     */
    @Accessor
    ViewArea getViewArea();

    /**
     * @return Rendered chunks
     */
    @Invoker
    int callCountRenderedChunks();

    /**
     * @return the view distance
     */
    @Accessor
    int getLastViewDistance();

    /**
     * @return the chunk render dispatcher
     */
    @Accessor
    ChunkRenderDispatcher getChunkRenderDispatcher();

    /**
     * @return the world
     */
    @Accessor
    ClientLevel getLevel();

    /**
     * @return the rendered entities
     */
    @Accessor
    int getRenderedEntities();
}
