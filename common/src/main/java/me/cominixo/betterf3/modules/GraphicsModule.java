package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.mixin.chunk.WorldRendererAccessor;
import me.cominixo.betterf3.utils.DebugLine;
import net.minecraft.ChatFormatting;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextColor;
import org.apache.commons.lang3.StringUtils;

/**
 * The Graphics module.
 */
public class GraphicsModule extends BaseModule {

    /**
     * Instantiates a new Graphics module.
     */
    public GraphicsModule() {
        this.defaultNameColor = TextColor.fromLegacyFormat(ChatFormatting.GOLD);
        this.defaultValueColor = TextColor.fromLegacyFormat(ChatFormatting.AQUA);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("render_distance"));
        lines.add(new DebugLine("graphics"));
        lines.add(new DebugLine("clouds"));
        lines.add(new DebugLine("biome_blend_radius"));
        lines.add(new DebugLine("shader"));
    }

    public void update(Minecraft client) {
        WorldRendererAccessor worldRendererMixin = (WorldRendererAccessor) client.levelRenderer;

        String cloudString = client.options.renderClouds == CloudStatus.OFF ? I18n.get("text.betterf3.line.off")
                : (client.options.renderClouds == CloudStatus.FAST ? I18n.get("text.betterf3.line.fast") : I18n.get("text.betterf3.line.fancy") );

        // Render Distance
        lines.get(0).setValue(worldRendererMixin.getLastViewDistance());
        // Graphics
        lines.get(1).setValue(StringUtils.capitalize(client.options.graphicsMode.toString()));
        // Clouds
        lines.get(2).setValue(cloudString);
        // Biome Blend Radius
        lines.get(3).setValue(client.options.biomeBlendRadius);

        // Shader
        PostChain shaderEffect = client.gameRenderer.currentEffect();
        if (shaderEffect != null) {
            lines.get(4).setValue(shaderEffect.getName());
        } else {
            lines.get(4).active = false;
        }

        lines.get(0).inReducedDebug = true;
        lines.get(3).inReducedDebug = true;
    }
}
