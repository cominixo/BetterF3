package me.cominixo.betterf3.modules;

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
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.light.LightingProvider;
import org.apache.commons.lang3.text.WordUtils;

import java.util.concurrent.CompletableFuture;


public class LocationModule extends BaseModule{

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
        lines.add(new DebugLine("days_played"));
    }

    public void update(MinecraftClient client) {

        Entity cameraEntity = client.getCameraEntity();


        IntegratedServer integratedServer = client.getServer();



        String chunkLightString = "";
        String chunkLightServerString = "";
        String localDifficultyString= "";
        StringBuilder highestBlock = new StringBuilder();
        StringBuilder highestBlockServer = new StringBuilder();


        if (client.world != null) {

            BlockPos blockPos = cameraEntity.getBlockPos();
            ChunkPos chunkPos = new ChunkPos(blockPos);

            // Biome
            lines.get(7).setValue(client.world.getRegistryManager().get(Registry.BIOME_KEY).getId(client.world.getBiome(blockPos)));

            World serverWorld = integratedServer != null ? integratedServer.getWorld(client.world.getRegistryKey()) : client.world;
            if (client.world.isChunkLoaded(blockPos)) {
                WorldChunk clientChunk = client.world.getChunk(chunkPos.x, chunkPos.z);
                if (clientChunk.isEmpty()) {
                    chunkLightString = I18n.translate("text.betterf3.line.waiting_chunk");
                } else if (serverWorld != null) {


                    // Client Chunk Lights
                    int totalLight = client.world.getChunkManager().getLightingProvider().getLight(blockPos, 0);
                    int skyLight = client.world.getLightLevel(LightType.SKY, blockPos);
                    int blockLight = client.world.getLightLevel(LightType.BLOCK, blockPos);
                    chunkLightString = I18n.translate("format.betterf3.chunklight", totalLight, skyLight, blockLight);


                    // Server Chunk Lights
                    LightingProvider lightingProvider = serverWorld.getChunkManager().getLightingProvider();

                    int skyLightServer = lightingProvider.get(LightType.SKY).getLightLevel(blockPos);
                    int blockLightServer = lightingProvider.get(LightType.BLOCK).getLightLevel(blockPos);

                    chunkLightServerString =  I18n.translate("format.betterf3.chunklight_server", skyLightServer, blockLightServer);

                    // Heightmap stuff (Find highest block)
                    Heightmap.Type[] heightmapTypes = Heightmap.Type.values();

                    WorldChunk serverChunk;

                    if (serverWorld instanceof ServerWorld) {
                        CompletableFuture<WorldChunk> chunkCompletableFuture = ((ServerWorld)serverWorld).getChunkManager().getChunkFutureSyncOnMainThread(blockPos.getX(), blockPos.getZ(), ChunkStatus.FULL, false)
                                .thenApply((either) -> either.map((chunk) -> (WorldChunk)chunk, (unloaded) -> null));

                        serverChunk = chunkCompletableFuture.getNow(null);
                    } else {
                        serverChunk = clientChunk;
                    }



                    for(Heightmap.Type type : heightmapTypes) {

                        // Client
                        if (type.shouldSendToClient()) {
                            String typeString = WordUtils.capitalizeFully(type.getName().replace("_", " "));
                            int blockY = clientChunk.sampleHeightmap(type, blockPos.getX(), blockPos.getZ());
                            if (blockY > -1) {
                                highestBlock.append("  ").append(typeString).append(": ").append(blockY);
                            }
                        }


                        // Server
                        if (type.isStoredServerSide() && serverWorld instanceof ServerWorld) {



                            if (serverChunk == null) {
                                serverChunk = clientChunk;
                            }

                            String typeString = Utils.enumToString(type);

                            int blockY = serverChunk.sampleHeightmap(type, blockPos.getX(), blockPos.getZ());
                            if (blockY > -1) {
                                highestBlockServer.append("  ").append(typeString).append(": ").append(blockY);
                            }
                        }

                    }

                    // Local Difficulty
                    if (blockPos.getY() >= 0 && blockPos.getY() < 256) {
                        float moonSize;
                        long inhabitedTime;

                        moonSize = serverWorld.getMoonSize();

                        if (serverChunk != null) {
                            inhabitedTime = serverChunk.getInhabitedTime();
                        } else {
                            inhabitedTime = clientChunk.getInhabitedTime();
                        }

                        LocalDifficulty localDifficulty = new LocalDifficulty(serverWorld.getDifficulty(), serverWorld.getTimeOfDay(), inhabitedTime, moonSize);
                        localDifficultyString = String.format("%.2f  " + I18n.translate("text.betterf3.line.clamped") + ": %.2f", localDifficulty.getLocalDifficulty(), localDifficulty.getClampedLocalDifficulty());
                    }

                }
            }
        }



        // Dimension
        if (client.world != null) {
            lines.get(0).setValue(client.world.getRegistryKey().getValue());
        }


        if (cameraEntity != null) {
            Direction facing = cameraEntity.getHorizontalFacing();

            String facingString = Utils.getFacingString(facing);
            // Facing
            lines.get(1).setValue(String.format("%s (%s)", I18n.translate("text.betterf3.line." + facing.toString().toLowerCase()), facingString));
            // Rotation
            String yaw = String.format("%.1f", MathHelper.wrapDegrees(cameraEntity.getYaw()));
            String pitch = String.format("%.1f", MathHelper.wrapDegrees(cameraEntity.getPitch()));
            lines.get(2).setValue(I18n.translate("format.betterf3.rotation", yaw, pitch));
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
        lines.get(9).setValue(client.world.getTimeOfDay() / 24000L);


    }

}
