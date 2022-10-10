package me.cominixo.betterf3.modules;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.MutableText;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

/**
 * The Server module.
 */
public class ServerModule extends BaseModule {

  /**
   * Instantiates a new Server module.
   */
  public ServerModule() {
    this.defaultNameColor = TextColor.fromFormatting(Formatting.GRAY);
    this.defaultValueColor = TextColor.fromFormatting(Formatting.YELLOW);

    this.nameColor = defaultNameColor;
    this.valueColor = defaultValueColor;

    lines.add(new DebugLine("server_tick", "format.betterf3.server_tick", true));
    lines.add(new DebugLine("packets_sent"));
    lines.add(new DebugLine("packets_received"));

    for (final DebugLine line : lines) {
      line.inReducedDebug = true;
    }
  }

  /**
   * Updates the Server module.
   *
   * @param client the Minecraft client
   */
  public void update(final MinecraftClient client) {
    final IntegratedServer integratedServer = client.getServer();

    String serverString = "";
    if (integratedServer != null) {
      serverString = I18n.translate("text.betterf3.line.integrated_server");
    } else if (client.player != null) {
      serverString = client.player.getServerBrand();
    }

    if (client.getNetworkHandler() != null) {
      final ClientConnection clientConnection = client.getNetworkHandler().getConnection();
      final float packetsSent = clientConnection.getAveragePacketsSent();
      final float packetsReceived = clientConnection.getAveragePacketsReceived();

      lines.get(1).value(Math.round(packetsSent));
      lines.get(2).value(Math.round(packetsReceived));
    }
    String tickString = "";
    if (integratedServer != null) {
      tickString = Integer.toString(Math.round(integratedServer.getTickTime()));
    }

    final List<MutableText> serverStringList = new LinkedList<>(Arrays.asList(Utils.styledText(serverString, nameColor), Utils.styledText(tickString, nameColor)));

    if (tickString.isEmpty()) {
      lines.get(0).format("format.betterf3.no_format");
      serverStringList.remove(1);
    }

    lines.get(0).value(serverStringList);
  }
}
