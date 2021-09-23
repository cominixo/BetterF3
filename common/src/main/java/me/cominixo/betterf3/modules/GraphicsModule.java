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

    /**
     * Updates the Graphics module.
     *
     * @param client the Minecraft client
     */
    public void update(final Minecraft client) {
        final WorldRendererAccessor worldRendererMixin = (WorldRendererAccessor) client.levelRenderer;

        final String cloudString = client.options.renderClouds == CloudStatus.OFF ? I18n.get("text.betterf3.line.off")
                : (client.options.renderClouds == CloudStatus.FAST ? I18n.get("text.betterf3.line.fast") : I18n.get("text.betterf3.line.fancy") );

        // Render Distance
        lines.get(0).value(worldRendererMixin.getLastViewDistance());
        // Graphics
        lines.get(1).value(StringUtils.capitalize(client.options.graphicsMode.toString()));
        // Clouds
        lines.get(2).value(cloudString);
        // Biome Blend Radius
        lines.get(3).value(client.options.biomeBlendRadius);

        // Shader
        final PostChain shaderEffect = client.gameRenderer.currentEffect();
        if (shaderEffect != null) {
            lines.get(4).value(shaderEffect.getName());
        } else {
            lines.get(4).active = false;
        }

        lines.get(0).inReducedDebug = true;
        lines.get(3).inReducedDebug = true;
    }
}
