package me.cominixo.betterf3.modules;

import com.mojang.datafixers.DataFixUtils;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import me.cominixo.betterf3.ducks.ChunkBuilderAccess;
import me.cominixo.betterf3.ducks.ClientChunkManagerAccess;
import me.cominixo.betterf3.ducks.ClientChunkMapAccess;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;

/**
 * The Chunks module.
 */
public class ChunksModule extends BaseModule {

  /**
   * The total color.
   */
  public TextColor totalColor;

  /**
   * The default total color.
   */
  public final TextColor defaultTotalColor = TextColor.fromFormatting(Formatting.GOLD);

  /**
   * Default enabled color.
   */
  public final TextColor defaultEnabledColor = TextColor.fromFormatting(Formatting.GREEN);

  /**
   * Default disabled color.
   */
  public final TextColor defaultDisabledColor = TextColor.fromFormatting(Formatting.RED);

  /**
   * Enabled color.
   */
  public TextColor enabledColor;

  /**
   * Disabled color.
   */
  public TextColor disabledColor;

  /**
   * Instantiates a new Chunks module.
   */
  public ChunksModule() {

    this.defaultNameColor = TextColor.fromRgb(0x00aaff);
    this.defaultValueColor = TextColor.fromFormatting(Formatting.YELLOW);

    this.nameColor = defaultNameColor;
    this.valueColor = defaultValueColor;
    this.totalColor = this.defaultTotalColor;
    this.enabledColor = this.defaultEnabledColor;
    this.disabledColor = this.defaultDisabledColor;

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
    lines.add(new DebugLine("chunk_file"));

    lines.get(0).inReducedDebug = true;
    lines.get(2).inReducedDebug = true;
    lines.get(3).inReducedDebug = true;
    lines.get(4).inReducedDebug = true;
    lines.get(5).inReducedDebug = true;

  }

  /**
   * Updates the chunk module.
   *
   * @param client the Minecraft client
   */
  public void update(final MinecraftClient client) {

    final int totalChunks;
    if (client.worldRenderer.chunks == null) {
      totalChunks = 0;
    } else {
      totalChunks = client.worldRenderer.chunks.chunks.length;
    }
    final int renderedChunks = client.worldRenderer.getCompletedChunkCount();

    final ChunkBuilder chunkBuilder = client.worldRenderer.chunkBuilder;
    final ChunkBuilderAccess chunkBuilderDuck = (ChunkBuilderAccess) chunkBuilder;

    if (client.world != null) {
      final ClientChunkManager clientChunkManager = client.world.getChunkManager();
      final ClientChunkManagerAccess clientChunkManagerMixin = (ClientChunkManagerAccess) clientChunkManager;
      final ClientChunkMapAccess clientChunkMapMixin = (ClientChunkMapAccess) (Object) clientChunkManagerMixin.getChunks();

      // Client Chunk Cache
      lines.get(5).value(clientChunkMapMixin.getChunks().length());
      // Loaded Chunks
      lines.get(6).value(clientChunkManager.getLoadedChunkCount());

    }

    final World world = DataFixUtils.orElse(Optional.ofNullable(client.getServer()).flatMap(integratedServer -> Optional.ofNullable(integratedServer.getWorld(client.world.getRegistryKey()))), client.world);
    final LongSet forceLoadedChunks = world instanceof ServerWorld ? ((ServerWorld) world).getForcedChunks() :
    LongSets.EMPTY_SET;

    final IntegratedServer integratedServer = client.getServer();
    final ServerWorld serverWorld = integratedServer != null ? integratedServer.getWorld(client.world.getRegistryKey()) : null;

    SpawnHelper.Info info = null;
    if (serverWorld != null) {
      info = serverWorld.getChunkManager().getSpawnInfo();
    }

    final String chunkCulling = client.chunkCullingEnabled ? I18n.translate("text.betterf3.line.enabled")
      : I18n.translate("text.betterf3.line.disabled");

    final List<Text> chunkValues = Arrays.asList(Utils.styledText(I18n.translate("text.betterf3.line.rendered"), valueColor), Utils.styledText(I18n.translate("text.betterf3.line.total"), this.totalColor),
    Utils.styledText(Integer.toString(renderedChunks), valueColor),
    Utils.styledText(Integer.toString(totalChunks), this.totalColor));

    // Chunk Sections
    lines.get(0).value(chunkValues);
    // Chunk Culling
    lines.get(1).value(Utils.styledText(chunkCulling, client.chunkCullingEnabled ? this.enabledColor :
    this.disabledColor));

    // TODO make this work properly with Canvas (chunkBuilderAccessor is null when using it)
    if (chunkBuilderDuck != null) {
      // Pending chunk uploads
      lines.get(2).value(client.worldRenderer.getChunkBuilder().getToBatchCount());

      lines.get(3).value(client.worldRenderer.getChunkBuilder().getChunksToUpload());

      lines.get(4).value(client.worldRenderer.getChunkBuilder().getFreeBufferCount());
    }

    // Loaded Chunks (Server)
    if (serverWorld != null) {
      lines.get(7).value(serverWorld.getChunkManager().getLoadedChunkCount());
    }
    // Forceloaded Chunks
    lines.get(8).value(forceLoadedChunks.size());
    // Spawn Chunks
    if (info != null) {
      lines.get(9).value(info.getSpawningChunkCount());
    }
    final BlockPos blockPos = Objects.requireNonNull(client.getCameraEntity()).getBlockPos();
    final ChunkPos chunkPos = new ChunkPos(blockPos);
    final String regionFile = "r.%d.%d.mca (%d, %d)".formatted(chunkPos.getRegionX(), chunkPos.getRegionZ(),
      chunkPos.getRegionRelativeX(), chunkPos.getRegionRelativeZ());
    lines.get(10).value(regionFile);
  }
}
