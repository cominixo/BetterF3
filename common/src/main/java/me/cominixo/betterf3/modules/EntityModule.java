package me.cominixo.betterf3.modules;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.cominixo.betterf3.mixin.chunk.WorldRendererAccessor;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;

import java.util.Arrays;
import java.util.List;

/**
 * The Entity module.
 */
public class EntityModule extends BaseModule {

    public final TextColor totalColor = TextColor.fromLegacyFormat(ChatFormatting.GOLD);

    /**
     * Instantiates a new Entity module.
     */
    public EntityModule() {
        this.defaultNameColor = TextColor.fromLegacyFormat(ChatFormatting.RED);
        this.defaultValueColor = TextColor.fromLegacyFormat(ChatFormatting.YELLOW);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("particles"));
        lines.add(new DebugLine("entities", "format.betterf3.total", true));

        // Monster, Creature, Ambient, Water Creature, Water Ambient, Misc
        for (MobCategory spawnGroup : MobCategory.values()) {
            String name = spawnGroup.toString().toLowerCase();
            lines.add(new DebugLine(name));
        }

        lines.get(0).inReducedDebug = true;
        lines.get(1).inReducedDebug = true;
    }

    public void update(Minecraft client) {
        WorldRendererAccessor worldRendererMixin = (WorldRendererAccessor) client.levelRenderer;

        List<Component> entityValues = Arrays.asList(Utils.getStyledText(I18n.get("text.betterf3.line.rendered"), valueColor), Utils.getStyledText(I18n.get("text.betterf3.line.total"), totalColor),
                Utils.getStyledText(worldRendererMixin.getRenderedEntities(), valueColor),
                Utils.getStyledText(worldRendererMixin.getLevel().getEntityCount(), totalColor));

        IntegratedServer integratedServer = client.getSingleplayerServer();

        if (client.level != null) {
            ServerLevel serverWorld = integratedServer != null ? integratedServer.getLevel(client.level.dimension()) : null;
            if (serverWorld != null) {
                NaturalSpawner.SpawnState info = serverWorld.getChunkSource().getLastSpawnState();
                if (info != null) {
                    Object2IntMap<MobCategory> spawnGroupCount = info.getMobCategoryCounts();
                    // Entities (separated) (kinda bad)
                    for (int i = 0; i < MobCategory.values().length; i++) {
                        MobCategory group = MobCategory.values()[i];
                        lines.get(i+2).setValue(spawnGroupCount.getInt(group));
                    }
                }
            }
        }

        // Particles
        lines.get(0).setValue(client.particleEngine.countParticles());
        // Entities
        lines.get(1).setValue(entityValues);
    }
}
