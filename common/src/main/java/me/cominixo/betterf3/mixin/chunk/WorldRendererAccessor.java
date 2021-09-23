package me.cominixo.betterf3.mixin.chunk;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ViewArea;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Accesses the World Renderer.
 */
@SuppressWarnings("checkstyle:MethodName")
@Mixin(LevelRenderer.class)
public interface WorldRendererAccessor {


    /**
     * Gets the world renderer view area.
     *
     * @return the view area
     */
    @Accessor
    ViewArea getViewArea();

    /**
     * Gets rendered chunk count.
     *
     * @return Rendered chunks
     */
    @Invoker
    int callCountRenderedChunks();

    /**
     * Gets the view distance.
     *
     * @return the view distance
     */
    @Accessor
    int getLastViewDistance();

    /**
     * Gets the chunk render dispatcher.
     *
     * @return the chunk render dispatcher
     */
    @Accessor
    ChunkRenderDispatcher getChunkRenderDispatcher();

    /**
     * Gets the world (level).
     *
     * @return the world
     */
    @Accessor
    ClientLevel getLevel();

    /**
     * Gets the amount of rendered entities.
     *
     * @return the rendered entities
     */
    @Accessor
    int getRenderedEntities();
}
