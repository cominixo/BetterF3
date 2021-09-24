package me.cominixo.betterf3.modules;

import java.util.Collections;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextColor;

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
    public final TextColor defaultColorHigh = TextColor.fromLegacyFormat(ChatFormatting.GREEN);

    /**
     * The default color for medium fps.
     */
    public final TextColor defaultColorMed = TextColor.fromLegacyFormat(ChatFormatting.YELLOW);

    /**
     * The default color for low fps.
     */
    public final TextColor defaultColorLow = TextColor.fromLegacyFormat(ChatFormatting.RED);

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
    public void update(final Minecraft client) {
        final int currentFps = Integer.parseInt(client.fpsString.split(" ")[0].split("/")[0]);

        final String fpsString = I18n.get("format.betterf3.fps", currentFps,
                (double) client.options.framerateLimit == Option.FRAMERATE_LIMIT.getMaxValue() ? I18n.get("text.betterf3.line.fps.unlimited") : client.options.framerateLimit,
                client.options.enableVsync ? I18n.get("text.betterf3.line.fps.vsync") : "").trim();

        final TextColor color = switch (Utils.fpsColor(currentFps)) {
            case HIGH -> this.colorHigh;
            case MEDIUM -> this.colorMed;
            case LOW -> this.colorLow;
        };

        lines.get(0).value(Collections.singletonList(Utils.styledText(fpsString, color)));
    }
}
