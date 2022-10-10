package me.cominixo.betterf3.modules;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Arrays;
import java.util.List;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.world.SpawnHelper;

/**
 * The Entity module.
 */
public class EntityModule extends BaseModule {

  /**
   * Total color.
   */
  public TextColor totalColor;

  /**
   * Default total color.
   */
  public final TextColor defaultTotalColor = TextColor.fromFormatting(Formatting.GOLD);

  /**
   * Instantiates a new Entity module.
   */
  public EntityModule() {
    this.defaultNameColor = TextColor.fromFormatting(Formatting.RED);
    this.defaultValueColor = TextColor.fromFormatting(Formatting.YELLOW);

    this.nameColor = defaultNameColor;
    this.valueColor = defaultValueColor;
    this.totalColor = this.defaultTotalColor;

    lines.add(new DebugLine("particles"));
    lines.add(new DebugLine("entities", "format.betterf3.total", true));

    // Monster, Creature, Ambient, Water Creature, Water Ambient, Misc
    for (final SpawnGroup spawnGroup : SpawnGroup.values()) {
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
  public void update(final MinecraftClient client) {

    assert client.worldRenderer.world != null;
    final List<Text> entityValues =
    Arrays.asList(Utils.styledText(I18n.translate("text.betterf3.line.rendered"), valueColor),
    Utils.styledText(I18n.translate("text.betterf3.line.total"), this.totalColor),
    Utils.styledText(client.worldRenderer.regularEntityCount, valueColor),
    Utils.styledText(client.worldRenderer.world.getRegularEntityCount(), this.totalColor));

    final IntegratedServer integratedServer = client.getServer();

    if (client.world != null) {
      final ServerWorld serverWorld = integratedServer != null ? integratedServer.getWorld(client.world.getRegistryKey()) : null;
      if (serverWorld != null) {
        final SpawnHelper.Info info = serverWorld.getChunkManager().getSpawnInfo();
        if (info != null) {
          final Object2IntMap<SpawnGroup> spawnGroupCount = info.getGroupToCount();
          // Entities (separated) (kinda bad)
          for (int i = 0; i < SpawnGroup.values().length; i++) {
            final SpawnGroup group = SpawnGroup.values()[i];
            lines.get(i + 2).value(spawnGroupCount.getInt(group));
          }
        }
      }
    }

    // Particles
    lines.get(0).value(client.particleManager.getDebugString());
    // Entities
    lines.get(1).value(entityValues);
  }
}
