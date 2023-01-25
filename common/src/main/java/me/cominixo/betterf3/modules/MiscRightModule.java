package me.cominixo.betterf3.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.cominixo.betterf3.utils.DebugLineList;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

/**
 * The Misc right module.
 */
public class MiscRightModule extends BaseModule {

  private static final List<String> VANILLA_DEBUG_RIGHT = Arrays.asList("Java:", "Mem:", "Allocated:", "CPU:", "Display:", "Allocation rate:");
  private final String version;

  /**
   * Instantiates a new Misc right module.
   */
  public MiscRightModule() {
    this.version = "BetterF3 Version: " + Utils.modVersion();

    this.defaultNameColor = TextColor.fromRgb(0xfdfd96);
    this.defaultValueColor = TextColor.fromFormatting(Formatting.AQUA);

    this.nameColor = defaultNameColor;
    this.valueColor = defaultValueColor;

    final DebugLineList rightDebugLines = new DebugLineList("misc_right");
    rightDebugLines.inReducedDebug = true;

    lines.add(rightDebugLines);
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

    lines.add(0, this.version);

    // boolean indicating if we're on the "targeted" section, we have no other way of knowing when it starts/ends.
    boolean inTargeted = false;

    // copy of list for .remove()
    final List<String> listCopy = new ArrayList<>(lines);

    for (final String s : listCopy) {
      if (s.isEmpty()) {
        inTargeted = false;
        lines.remove(s);
        continue;
      }
      if (s.startsWith(Formatting.UNDERLINE + "Targeted Block") || s.startsWith( Formatting.UNDERLINE + "Targeted Fluid") || s.startsWith(Formatting.UNDERLINE + "Targeted Entity")) {
        inTargeted = true;
        lines.remove(s);
        continue;
      }
      if (inTargeted) {
        lines.remove(s);
        continue;
      }

      for (final String vanilla : VANILLA_DEBUG_RIGHT) {
        final int index = listCopy.indexOf(s);

        if (s.startsWith(vanilla)) {
          lines.remove(s);
        } else if (index > 1) {
          // pretty bad, this is to include GPU info in the vanilla list.
          if (listCopy.get(index - 1).startsWith("Display:") || listCopy.get(index - 2).startsWith(
          "Display:")) {
            lines.remove(s);
          }
        }
      }
    }
    ((DebugLineList) this.lines.get(0)).values(lines);
  }
}
