package me.cominixo.betterf3.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.cominixo.betterf3.utils.DebugLineList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

/**
 * The Misc left module.
 */
public class MiscLeftModule extends BaseModule {

  private static final List<String> VANILLA_DEBUG_LEFT = Arrays.asList("Minecraft", "Integrated", "C:", "E:", "P:", "Client", "ServerChunkCache:", "minecraft:overworld", "XYZ:", "Block:", "Chunk:", "Facing:", "Waiting", "SC:", "Sounds:", "CH", "SH", "Server Light:", "Biome:", "Local Difficulty:", "Chunks[");
  private int leftSideSize = 0;

  /**
   * Instantiates a new Misc left module.
   */
  public MiscLeftModule() {
    this.defaultNameColor = TextColor.fromRgb(0xfdfd96);
    this.defaultValueColor = TextColor.fromFormatting(Formatting.AQUA);

    this.nameColor = defaultNameColor;
    this.valueColor = defaultValueColor;

    final DebugLineList leftDebugLines = new DebugLineList("misc_left");
    leftDebugLines.inReducedDebug = true;

    lines.add(leftDebugLines);
  }

  /**
   * Does nothing.
   *
   * @param client the Minecraft client
   */
  public void update(final MinecraftClient client) {
    // Do nothing
  }

  /**
   * Updates the lines.
   *
   * @param lines the lines
   */
  public void update(final List<String> lines) {

    // Parse lines to find non-vanilla lines, it's a mess
    if (lines.size() != this.leftSideSize) {

      // copy of list for .remove()
      final List<String> listCopy = new ArrayList<>(lines);

      for (final String s : listCopy) {
        if (s != null) {
          if (s.isEmpty()) {
            lines.remove(s);
          }
          final String[] stringSplit = s.split(" ");

          for (final String vanilla : VANILLA_DEBUG_LEFT) {

            if (s.startsWith(vanilla)) {
              lines.remove(s);
            } else if (stringSplit.length > 1) {

              if (stringSplit[1].equals("fps")) {
                lines.remove(s);
              }
            }
            if (s.contains("tx") && s.contains("rx")) {
              // Have to do this to check if it is a vanilla server info line.
              lines.remove(s);
            }
          }
        }
      }
      this.leftSideSize = lines.size();
    }
    ((DebugLineList) this.lines.get(0)).values(lines);
  }
}
