package me.cominixo.betterf3.modules;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.light.LightingProvider;
import org.apache.commons.lang3.StringUtils;

/**
 * The Location module.
 */
public class LocationModule extends BaseModule {

  /**
   * Instantiates a new Location module.
   */
  public LocationModule() {
    this.defaultNameColor = TextColor.fromFormatting(Formatting.DARK_GREEN);
    this.defaultValueColor = TextColor.fromFormatting(Formatting.AQUA);

    this.nameColor = defaultNameColor;
    this.valueColor = defaultValueColor;

    lines.add(new DebugLine("dimension"));
    lines.add(new DebugLine("facing"));
    lines.add(new DebugLine("rotation"));
    lines.add(new DebugLine("light"));
    lines.add(new DebugLine("light_server"));
    lines.add(new DebugLine("highest_block"));
    lines.add(new DebugLine("highest_block_server"));
    lines.add(new DebugLine("biome"));
    lines.add(new DebugLine("local_difficulty"));
    lines.add(new DebugLine("day_ticks"));
    lines.add(new DebugLine("days_played"));
    lines.add(new DebugLine("slime_chunk"));
  }

  /**
   * Updates the Location module.
   *
   * @param client the Minecraft client
   */
  public void update(final MinecraftClient client) {
    final Entity cameraEntity = client.getCameraEntity();

    final IntegratedServer integratedServer = client.getServer();

    String chunkLightString = "";
    String chunkLightServerString = "";
    String localDifficultyString = "";
    String slimeChunkString = "";
    final StringBuilder highestBlock = new StringBuilder();
    final StringBuilder highestBlockServer = new StringBuilder();

    final World serverWorld;
    if (client.world != null) {
      assert cameraEntity != null;
      final BlockPos blockPos = cameraEntity.getBlockPos();
      final ChunkPos chunkPos = new ChunkPos(blockPos);

      // Biome
      lines.get(7).value(client.world.getRegistryManager().get(Registry.BIOME_KEY).getId(client.world.getBiome(blockPos).value()));

      serverWorld = integratedServer != null ? integratedServer.getWorld(client.world.getRegistryKey()) : client.world;
      if (client.world.isChunkLoaded(blockPos.getX(), blockPos.getZ())) {
        final WorldChunk clientChunk = client.world.getChunk(chunkPos.x, chunkPos.z);
        if (clientChunk.isEmpty()) {
          chunkLightString = I18n.translate("text.betterf3.line.waiting_chunk");
        } else if (serverWorld != null) {

          // Client Chunk Lights
          final int totalLight = client.world.getChunkManager().getLightingProvider().getLight(blockPos, 0);
          final int skyLight = client.world.getLightLevel(LightType.SKY, blockPos);
          final int blockLight = client.world.getLightLevel(LightType.BLOCK, blockPos);
          chunkLightString = I18n.translate("format.betterf3.chunklight", totalLight, skyLight, blockLight);

          // Server Chunk Lights
          final LightingProvider lightingProvider = serverWorld.getChunkManager().getLightingProvider();

          final int skyLightServer = lightingProvider.get(LightType.SKY).getLightLevel(blockPos);
          final int blockLightServer = lightingProvider.get(LightType.BLOCK).getLightLevel(blockPos);

          chunkLightServerString = I18n.translate("format.betterf3.chunklight_server", skyLightServer, blockLightServer);

          // Heightmap stuff (Find the highest block)
          final Heightmap.Type[] heightmapTypes = Heightmap.Type.values();

          WorldChunk serverChunk;

          if (serverWorld instanceof ServerWorld) {
            final CompletableFuture<WorldChunk> chunkCompletableFuture = ((ServerWorld) serverWorld).getChunkManager().getChunkFutureSyncOnMainThread(blockPos.getX(), blockPos.getZ(), ChunkStatus.FULL, false).thenApply(either -> either.map(chunk -> (WorldChunk) chunk, unloaded -> null));

            serverChunk = chunkCompletableFuture.getNow(null);
          } else {
            serverChunk = clientChunk;
          }

          for (final Heightmap.Type type : heightmapTypes) {

            // Client
            if (type.shouldSendToClient()) {
              final String typeString = StringUtils.capitalize(type.getName().replace("_", " "));
              final int blockY = clientChunk.sampleHeightmap(type, blockPos.getX(), blockPos.getZ());
              if (blockY > -1) {
                highestBlock.append("  ").append(typeString).append(": ").append(blockY);
              }
            }

            // Server
            if (type.isStoredServerSide() && serverWorld instanceof ServerWorld) {
              if (serverChunk == null) {
                serverChunk = clientChunk;
              }

              final String typeString = Utils.enumToString(type);

              final int blockY = serverChunk.sampleHeightmap(type, blockPos.getX(), blockPos.getZ());
              if (blockY > -1) {
                highestBlockServer.append("  ").append(typeString).append(": ").append(blockY);
              }
            }
          }

          // Local Difficulty
          if (blockPos.getY() >= 0 && blockPos.getY() < 256) {
            final float moonSize;
            final long inhabitedTime;

            moonSize = serverWorld.getMoonSize();

            inhabitedTime = Objects.requireNonNullElse(serverChunk, clientChunk).getInhabitedTime();

            final LocalDifficulty localDifficulty = new LocalDifficulty(serverWorld.getDifficulty(), serverWorld.getTimeOfDay(), inhabitedTime, moonSize);
            localDifficultyString = String.format("%.2f  " + I18n.translate("text.betterf3.line.clamped") + ": %.2f", localDifficulty.getLocalDifficulty(), localDifficulty.getClampedLocalDifficulty());
          }

          if (integratedServer != null) {
            final Random slimeChunk = ChunkRandom.getSlimeRandom(chunkPos.x, chunkPos.z, ((StructureWorldAccess) serverWorld).getSeed(), 0x3ad8025fL);
            slimeChunkString = String.format("%s", I18n.translate((slimeChunk.nextInt(10) == 0 ) ? "text.betterf3.line.slime_chunk.true" : "text.betterf3.line.slime_chunk.false"));
          } else {
            slimeChunkString = String.format("%s", I18n.translate("text.betterf3.line.slime_chunk.unknown"));
          }
        }
      }
    }

    // Dimension
    if (client.world != null) {
      lines.get(0).value(client.world.getRegistryKey().getValue());
    }

    if (cameraEntity != null) {
      final Direction facing = cameraEntity.getHorizontalFacing();

      final String facingString = Utils.facingString(facing);
      // Facing
      lines.get(1).value(String.format("%s (%s)", I18n.translate("text.betterf3.line." + facing.toString().toLowerCase()), facingString));
      // Rotation
      final String yaw = String.format("%.1f", MathHelper.wrapDegrees(cameraEntity.getYaw()));
      final String pitch = String.format("%.1f", MathHelper.wrapDegrees(cameraEntity.getPitch()));
      lines.get(2).value(I18n.translate("format.betterf3.rotation", yaw, pitch));
    }

    // Client Light
    lines.get(3).value(chunkLightString);
    // Server Light
    lines.get(4).value(chunkLightServerString);
    // Highest Block
    lines.get(5).value(highestBlock.toString().trim());
    // Highest Block (Server)
    lines.get(6).value(highestBlockServer.toString().trim());

    // Local Difficulty
    lines.get(8).value(localDifficultyString);
    // Ticks in the day
    lines.get(9).value(Long.valueOf(client.world.getTimeOfDay() % 24000L).intValue());
    // Days played
    lines.get(10).value(client.world.getTimeOfDay() / 24000L);

    // Slime chunk
    lines.get(11).value(slimeChunkString.trim());
  }
}
