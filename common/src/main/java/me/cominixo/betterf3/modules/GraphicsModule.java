package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

/**
 * The Graphics module.
 */
public class GraphicsModule extends BaseModule {

  /**
   * Instantiates a new Graphics module.
   */
  public GraphicsModule() {
    this.defaultNameColor = TextColor.fromFormatting(Formatting.GOLD);
    this.defaultValueColor = TextColor.fromFormatting(Formatting.AQUA);

    this.nameColor = defaultNameColor;
    this.valueColor = defaultValueColor;

    lines.add(new DebugLine("render_distance"));
    lines.add(new DebugLine("graphics"));
    lines.add(new DebugLine("clouds"));
    lines.add(new DebugLine("biome_blend_radius"));
    lines.add(new DebugLine("shader"));
  }

  /**
   * Updates the Graphics module.
   *
   * @param client the Minecraft client
   */
  public void update(final MinecraftClient client) {

    final String cloudString = client.options.getCloudRenderMode().getValue() == CloudRenderMode.OFF ? I18n.translate("text" +
    ".betterf3.line.off")
    : (client.options.getCloudRenderMode().getValue() == CloudRenderMode.FAST ? I18n.translate("text.betterf3.line.fast") :
    I18n.translate("text" +
    ".betterf3.line.fancy") );

    // Render Distance
    lines.get(0).value(client.worldRenderer.viewDistance);
    // Graphics
    lines.get(1).value(StringUtils.capitalize(client.options.getGraphicsMode().getValue().toString()));
    // Clouds
    lines.get(2).value(cloudString);
    // Biome Blend Radius
    lines.get(3).value(client.options.getBiomeBlendRadius().getValue());

    // Shader
    final ShaderEffect shaderEffect = client.gameRenderer.getShader();
    if (shaderEffect != null) {
      lines.get(4).value(shaderEffect.getName());
    } else {
      lines.get(4).active = false;
    }

    lines.get(0).inReducedDebug = true;
    lines.get(3).inReducedDebug = true;
  }
}
