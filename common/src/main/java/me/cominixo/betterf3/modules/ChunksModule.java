package me.cominixo.betterf3.modules;

import com.mojang.datafixers.DataFixUtils;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import me.cominixo.betterf3.mixin.chunk.ChunkBuilderAccessor;
import me.cominixo.betterf3.mixin.chunk.ClientChunkManagerAccessor;
import me.cominixo.betterf3.mixin.chunk.ClientChunkMapAccessor;
import me.cominixo.betterf3.mixin.chunk.WorldRendererAccessor;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The Chunks module.
 */
public class ChunksModule extends BaseModule {

    public final TextColor totalColor = TextColor.fromLegacyFormat(ChatFormatting.GOLD);

    /**
     * Instantiates a new Chunks module.
     */
    public ChunksModule() {


        this.defaultNameColor = TextColor.fromRgb(0x00aaff);
        this.defaultValueColor = TextColor.fromLegacyFormat(ChatFormatting.YELLOW);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("chunk_sections", "format.betterf3.total", true));
        lines.add(new DebugLine("chunk_culling"));
        lines.add(new DebugLine("pending_chunks"));
        lines.add(new DebugLine("pending_uploads"));
        lines.add(new DebugLine("available_buffers"));
        lines.add(new DebugLine("client_chunk_cache"));
        lines.add(new DebugLine("loaded_chunks"));
        lines.add(new DebugLine("loaded_chunks_server"));
        lines.add(new DebugLine("forceloaded_chunks"));
        lines.add(new DebugLine("spawn_chunks"));

        lines.get(0).inReducedDebug = true;
        lines.get(2).inReducedDebug = true;
        lines.get(3).inReducedDebug = true;
        lines.get(4).inReducedDebug = true;
        lines.get(5).inReducedDebug = true;

    }

    public void update(Minecraft client) {

        WorldRendererAccessor worldRendererMixin = (WorldRendererAccessor) client.levelRenderer;
        int totalChunks;
        if (worldRendererMixin.getViewArea() == null) {
            totalChunks = 0;
        } else {
            totalChunks = worldRendererMixin.getViewArea().chunks.length;
        }
        int renderedChunks = worldRendererMixin.callCountRenderedChunks();

        ChunkRenderDispatcher chunkBuilder = worldRendererMixin.getChunkRenderDispatcher();
        ChunkBuilderAccessor chunkBuilderAccessor = (ChunkBuilderAccessor) chunkBuilder;

        if (client.level != null) {
            ClientChunkCache clientChunkManager = client.level.getChunkSource();
            ClientChunkManagerAccessor clientChunkManagerMixin = (ClientChunkManagerAccessor) clientChunkManager;
            ClientChunkMapAccessor clientChunkMapMixin = (ClientChunkMapAccessor) (Object) clientChunkManagerMixin.getStorage();

            // Client Chunk Cache
            lines.get(5).setValue(clientChunkMapMixin.getChunks().length());
            // Loaded Chunks
            lines.get(6).setValue(clientChunkManager.getLoadedChunksCount());

        }

        Level world = DataFixUtils.orElse(Optional.ofNullable(client.getSingleplayerServer()).flatMap((integratedServer) -> Optional.ofNullable(integratedServer.getLevel(client.level.dimension()))), client.level);
        LongSet forceLoadedChunks = world instanceof ServerLevel ? ((ServerLevel)world).getForcedChunks() : LongSets.EMPTY_SET;

        IntegratedServer integratedServer = client.getSingleplayerServer();
        ServerLevel serverWorld = integratedServer != null ? integratedServer.getLevel(client.level.dimension()) : null;

        NaturalSpawner.SpawnState info = null;
        if (serverWorld != null) {
            info = serverWorld.getChunkSource().getLastSpawnState();
        }

        String chunkCulling = client.smartCull ? ChatFormatting.GREEN + I18n.get("text.betterf3.line.enabled")
                : ChatFormatting.RED + I18n.get("text.betterf3.line.disabled");

        List<Component> chunkValues = Arrays.asList(Utils.getStyledText(I18n.get("text.betterf3.line.rendered"), valueColor), Utils.getStyledText(I18n.get("text.betterf3.line.total"), totalColor),
                Utils.getStyledText(Integer.toString(renderedChunks), valueColor), Utils.getStyledText(Integer.toString(totalChunks), totalColor));

        // Chunk Sections
        lines.get(0).setValue(chunkValues);
        // Chunk Culling
        lines.get(1).setValue(chunkCulling);

        // TODO make this work properly with Canvas (chunkBuilderAccessor is null when using it)
        if (chunkBuilderAccessor != null) {
            // Pending Chunks
            lines.get(2).setValue(chunkBuilderAccessor.getToBatchCount());
            // Pending Uploads to GPU
            lines.get(3).setValue(chunkBuilderAccessor.getToUpload().size());
            // Available Buffers
            lines.get(4).setValue(chunkBuilderAccessor.getFreeBufferCount());
        }

        // Loaded Chunks (Server)
        if (serverWorld != null) {
            lines.get(7).setValue(serverWorld.getChunkSource().getLoadedChunksCount());
        }
        // Forceloaded Chunks
        lines.get(8).setValue(forceLoadedChunks.size());
        // Spawn Chunks
        if (info != null) {
            lines.get(9).setValue(info.getSpawnableChunkCount());
        }
    }
}
