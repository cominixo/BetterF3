package me.cominixo.betterf3.modules;

import java.util.Collections;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

/**
 * The FPS module.
 */
public class FpsModule extends BaseModule {

  /**
   * The color for high fps.
   */
  public TextColor colorHigh;

  /**
   * The color for medium fps.
   */
  public TextColor colorMed;

  /**
   * The color for low fps.
   */
  public TextColor colorLow;

  /**
   * The default color for high fps.
   */
  public final TextColor defaultColorHigh = TextColor.fromFormatting(Formatting.GREEN);

  /**
   * The default color for medium fps.
   */
  public final TextColor defaultColorMed = TextColor.fromFormatting(Formatting.YELLOW);

  /**
   * The default color for low fps.
   */
  public final TextColor defaultColorLow = TextColor.fromFormatting(Formatting.RED);

  /**
   * Instantiates a new FPS module.
   */
  public FpsModule() {
    lines.add(new DebugLine("fps", "format.betterf3.no_format", true));
    lines.get(0).inReducedDebug = true;

    this.colorHigh = this.defaultColorHigh;
    this.colorMed = this.defaultColorMed;
    this.colorLow = this.defaultColorLow;
  }

  /**
   * Updates the FPS module.
   *
   * @param client the Minecraft client
   */
  public void update(final MinecraftClient client) {
    final int currentFps = Integer.parseInt(client.fpsDebugString.split(" ")[0].split("/")[0].trim());

    final String fpsString = I18n
      .translate("format.betterf3.fps", currentFps,
        (double) client.options.getMaxFps().getValue() == GameOptions.MAX_FRAMERATE ?
          I18n.translate("text.betterf3.line.fps.unlimited") :
          client.options.getMaxFps().getValue(),
        client.options.getEnableVsync().getValue() ?
          I18n.translate("text.betterf3.line.fps.vsync") : "")
      .trim();

    final TextColor color = switch (Utils.fpsColor(currentFps)) {
      case HIGH -> this.colorHigh;
      case MEDIUM -> this.colorMed;
      case LOW -> this.colorLow;
    };

    lines.get(0).value(Collections.singletonList(Utils.styledText(fpsString, color)));
  }
}
