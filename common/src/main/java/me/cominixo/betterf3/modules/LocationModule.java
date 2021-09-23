package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;


/**
 * The Location module.
 */
public class LocationModule extends BaseModule {

    /**
     * Instantiates a new Location module.
     */
    public LocationModule() {
        this.defaultNameColor = TextColor.fromLegacyFormat(ChatFormatting.DARK_GREEN);
        this.defaultValueColor = TextColor.fromLegacyFormat(ChatFormatting.AQUA);

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
        lines.add(new DebugLine("days_played"));
    }

    public void update(Minecraft client) {
        Entity cameraEntity = client.getCameraEntity();

        IntegratedServer integratedServer = client.getSingleplayerServer();

        String chunkLightString = "";
        String chunkLightServerString = "";
        String localDifficultyString= "";
        StringBuilder highestBlock = new StringBuilder();
        StringBuilder highestBlockServer = new StringBuilder();

        if (client.level != null) {
            assert cameraEntity != null;
            BlockPos blockPos = cameraEntity.blockPosition();
            ChunkPos chunkPos = new ChunkPos(blockPos);

            // Biome
            lines.get(7).setValue(client.level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(client.level.getBiome(blockPos)));

            Level serverWorld = integratedServer != null ? integratedServer.getLevel(client.level.dimension()) : client.level;
            if (client.level.hasChunkAt(blockPos)) {
                LevelChunk clientChunk = client.level.getChunk(chunkPos.x, chunkPos.z);
                if (clientChunk.isEmpty()) {
                    chunkLightString = I18n.get("text.betterf3.line.waiting_chunk");
                } else if (serverWorld != null) {

                    // Client Chunk Lights
                    int totalLight = client.level.getChunkSource().getLightEngine().getRawBrightness(blockPos, 0);
                    int skyLight = client.level.getBrightness(LightLayer.SKY, blockPos);
                    int blockLight = client.level.getBrightness(LightLayer.BLOCK, blockPos);
                    chunkLightString = I18n.get("format.betterf3.chunklight", totalLight, skyLight, blockLight);

                    // Server Chunk Lights
                    LevelLightEngine lightingProvider = serverWorld.getChunkSource().getLightEngine();

                    int skyLightServer = lightingProvider.getLayerListener(LightLayer.SKY).getLightValue(blockPos);
                    int blockLightServer = lightingProvider.getLayerListener(LightLayer.BLOCK).getLightValue(blockPos);

                    chunkLightServerString =  I18n.get("format.betterf3.chunklight_server", skyLightServer, blockLightServer);

                    // Heightmap stuff (Find highest block)
                    Heightmap.Types[] heightmapTypes = Heightmap.Types.values();

                    LevelChunk serverChunk;

                    if (serverWorld instanceof ServerLevel) {
                        CompletableFuture<LevelChunk> chunkCompletableFuture = ((ServerLevel)serverWorld).getChunkSource().getChunkFuture(blockPos.getX(), blockPos.getZ(), ChunkStatus.FULL, false)
                                .thenApply((either) -> either.map((chunk) -> (LevelChunk)chunk, (unloaded) -> null));

                        serverChunk = chunkCompletableFuture.getNow(null);
                    } else {
                        serverChunk = clientChunk;
                    }

                    for(Heightmap.Types type : heightmapTypes) {

                        // Client
                        if (type.sendToClient()) {
                            String typeString = WordUtils.capitalizeFully(type.getSerializationKey().replace("_", " "));
                            int blockY = clientChunk.getHeight(type, blockPos.getX(), blockPos.getZ());
                            if (blockY > -1) {
                                highestBlock.append("  ").append(typeString).append(": ").append(blockY);
                            }
                        }

                        // Server
                        if (type.keepAfterWorldgen() && serverWorld instanceof ServerLevel) {
                            if (serverChunk == null) {
                                serverChunk = clientChunk;
                            }

                            String typeString = Utils.enumToString(type);

                            int blockY = serverChunk.getHeight(type, blockPos.getX(), blockPos.getZ());
                            if (blockY > -1) {
                                highestBlockServer.append("  ").append(typeString).append(": ").append(blockY);
                            }
                        }
                    }

                    // Local Difficulty
                    if (blockPos.getY() >= 0 && blockPos.getY() < 256) {
                        float moonSize;
                        long inhabitedTime;

                        moonSize = serverWorld.getMoonBrightness();

                        inhabitedTime = Objects.requireNonNullElse(serverChunk, clientChunk).getInhabitedTime();

                        DifficultyInstance localDifficulty = new DifficultyInstance(serverWorld.getDifficulty(), serverWorld.getDayTime(), inhabitedTime, moonSize);
                        localDifficultyString = String.format("%.2f  " + I18n.get("text.betterf3.line.clamped") + ": %.2f", localDifficulty.getEffectiveDifficulty(), localDifficulty.getSpecialMultiplier());
                    }
                }
            }
        }

        // Dimension
        if (client.level != null) {
            lines.get(0).setValue(client.level.dimension().location());
        }

        if (cameraEntity != null) {
            Direction facing = cameraEntity.getDirection();

            String facingString = Utils.getFacingString(facing);
            // Facing
            lines.get(1).setValue(String.format("%s (%s)", I18n.get("text.betterf3.line." + facing.toString().toLowerCase()), facingString));
            // Rotation
            String yaw = String.format("%.1f", Mth.wrapDegrees(cameraEntity.getYRot()));
            String pitch = String.format("%.1f", Mth.wrapDegrees(cameraEntity.getXRot()));
            lines.get(2).setValue(I18n.get("format.betterf3.rotation", yaw, pitch));
        }

        // Client Light
        lines.get(3).setValue(chunkLightString);
        // Server Light
        lines.get(4).setValue(chunkLightServerString);
        // Highest Block
        lines.get(5).setValue(highestBlock.toString().trim());
        // Highest Block (Server)
        lines.get(6).setValue(highestBlockServer.toString().trim());

        // Local Difficulty
        lines.get(8).setValue(localDifficultyString);
        // Days played
        lines.get(9).setValue(client.level.getDayTime() / 24000L);
    }
}
