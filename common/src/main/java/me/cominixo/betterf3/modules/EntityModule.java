package me.cominixo.betterf3.modules;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Arrays;
import java.util.List;
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

/**
 * The Entity module.
 */
public class EntityModule extends BaseModule {

    /**
     * Total color.
     */
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
        for (final MobCategory spawnGroup : MobCategory.values()) {
            final String name = spawnGroup.toString().toLowerCase();
            lines.add(new DebugLine(name));
        }

        lines.get(0).inReducedDebug = true;
        lines.get(1).inReducedDebug = true;
    }

    /**
     * Updates the Entity module.
     *
     * @param client the Minecraft client
     */
    public void update(final Minecraft client) {
        final WorldRendererAccessor worldRendererMixin = (WorldRendererAccessor) client.levelRenderer;

        final List<Component> entityValues =
                Arrays.asList(Utils.styledText(I18n.get("text.betterf3.line.rendered"), valueColor),
                        Utils.styledText(I18n.get("text.betterf3.line.total"), this.totalColor),
                Utils.styledText(worldRendererMixin.getRenderedEntities(), valueColor),
                Utils.styledText(worldRendererMixin.getLevel().getEntityCount(), this.totalColor));

        final IntegratedServer integratedServer = client.getSingleplayerServer();

        if (client.level != null) {
            final ServerLevel serverWorld = integratedServer != null ? integratedServer.getLevel(client.level.dimension()) : null;
            if (serverWorld != null) {
                final NaturalSpawner.SpawnState info = serverWorld.getChunkSource().getLastSpawnState();
                if (info != null) {
                    final Object2IntMap<MobCategory> spawnGroupCount = info.getMobCategoryCounts();
                    // Entities (separated) (kinda bad)
                    for (int i = 0; i < MobCategory.values().length; i++) {
                        final MobCategory group = MobCategory.values()[i];
                        lines.get(i + 2).value(spawnGroupCount.getInt(group));
                    }
                }
            }
        }

        // Particles
        lines.get(0).value(client.particleEngine.countParticles());
        // Entities
        lines.get(1).value(entityValues);
    }
}
