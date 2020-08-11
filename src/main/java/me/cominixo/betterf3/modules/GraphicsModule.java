package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.mixin.chunk.WorldRendererAccessor;
import me.cominixo.betterf3.utils.DebugLine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.options.CloudRenderMode;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

public class GraphicsModule extends BaseModule {

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

    public void update(MinecraftClient client) {

        WorldRendererAccessor worldRendererMixin = (WorldRendererAccessor) client.worldRenderer;

        String cloudString = client.options.cloudRenderMode == CloudRenderMode.OFF ? "Off" : (client.options.cloudRenderMode == CloudRenderMode.FAST ? "Fast" : "Fancy");

        // Render Distance
        lines.get(0).setValue(worldRendererMixin.getRenderDistance());
        // Graphics
        lines.get(1).setValue(StringUtils.capitalize(client.options.graphicsMode.toString()));
        // Clouds
        lines.get(2).setValue(cloudString);
        // Biome Blend Radius
        lines.get(3).setValue(client.options.biomeBlendRadius);

        // Shader
        ShaderEffect shaderEffect = client.gameRenderer.getShader();
        if (shaderEffect != null) {
            lines.get(4).setValue(shaderEffect.getName());
        } else {
            lines.get(4).active = false;
        }

        lines.get(0).inReducedDebug = true;
        lines.get(3).inReducedDebug = true;
    }

}
