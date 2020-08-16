package me.cominixo.betterf3.modules;

import com.mojang.datafixers.DataFixUtils;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import me.cominixo.betterf3.mixin.chunk.ChunkBuilderAccessor;
import me.cominixo.betterf3.mixin.chunk.ClientChunkManagerAccessor;
import me.cominixo.betterf3.mixin.chunk.ClientChunkMapAccessor;
import me.cominixo.betterf3.mixin.chunk.WorldRendererAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ChunksModule extends BaseModule{

    public final TextColor totalColor = TextColor.fromFormatting(Formatting.GOLD);

    public ChunksModule() {


        this.defaultNameColor = TextColor.fromRgb(0x00aaff);
        this.defaultValueColor = TextColor.fromFormatting(Formatting.YELLOW);

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

    public void update(MinecraftClient client) {



        WorldRendererAccessor worldRendererMixin = (WorldRendererAccessor) client.worldRenderer;
        int totalChunks = worldRendererMixin.getChunks().chunks.length;
        int renderedChunks = worldRendererMixin.callGetCompletedChunkCount();

        ChunkBuilder chunkBuilder = worldRendererMixin.getChunkBuilder();
        ChunkBuilderAccessor chunkBuilderAccessor = (ChunkBuilderAccessor) chunkBuilder;

        if (client.world != null) {
            ClientChunkManager clientChunkManager = client.world.getChunkManager();
            ClientChunkManagerAccessor clientChunkManagerMixin = (ClientChunkManagerAccessor) clientChunkManager;
            ClientChunkMapAccessor clientChunkMapMixin = (ClientChunkMapAccessor) (Object) clientChunkManagerMixin.getChunks();

            // Client Chunk Cache
            lines.get(5).setValue(clientChunkMapMixin.getChunks().length());
            // Loaded Chunks
            lines.get(6).setValue(clientChunkManager.getLoadedChunkCount());

        }


        World world = DataFixUtils.orElse(Optional.ofNullable(client.getServer()).flatMap((integratedServer) -> Optional.ofNullable(integratedServer.getWorld(client.world.getRegistryKey()))), client.world);
        LongSet forceLoadedChunks = world instanceof ServerWorld ? ((ServerWorld)world).getForcedChunks() : LongSets.EMPTY_SET;

        IntegratedServer integratedServer = client.getServer();
        ServerWorld serverWorld = integratedServer != null ? integratedServer.getWorld(client.world.getRegistryKey()) : null;

        SpawnHelper.Info info = null;
        if (serverWorld != null) {
             info = serverWorld.getChunkManager().getSpawnInfo();
        }


        String chunkCulling = client.chunkCullingEnabled ? Formatting.GREEN + I18n.translate("text.betterf3.line.enabled")
                                                         : Formatting.RED + I18n.translate("text.betterf3.line.disabled");

        List<Text> chunkValues = Arrays.asList(Utils.getStyledText(I18n.translate("text.betterf3.line.rendered"), valueColor), Utils.getStyledText(I18n.translate("text.betterf3.line.total"), totalColor),
                Utils.getStyledText(Integer.toString(renderedChunks), valueColor), Utils.getStyledText(Integer.toString(totalChunks), totalColor));

        // Chunk Sections
        lines.get(0).setValue(chunkValues);
        // Chunk Culling
        lines.get(1).setValue(chunkCulling);
        // Pending Chunks
        lines.get(2).setValue(chunkBuilderAccessor.getQueuedTaskCount());
        // Pending Uploads to GPU
        lines.get(3).setValue(chunkBuilderAccessor.getUploadQueue().size());
        // Available Buffers
        lines.get(4).setValue(chunkBuilderAccessor.getBufferCount());

        // Loaded Chunks (Server)
        if (serverWorld != null) {
            lines.get(7).setValue(serverWorld.getChunkManager().getLoadedChunkCount());
        }
        // Forceloaded Chunks
        lines.get(8).setValue(forceLoadedChunks.size());
        // Spawn Chunks
        if (info != null) {
            lines.get(9).setValue(info.getSpawningChunkCount());
        }


        
    }
}
