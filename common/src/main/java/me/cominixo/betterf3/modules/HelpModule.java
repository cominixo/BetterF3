package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextColor;


/**
 * The Help module.
 */
public class HelpModule extends BaseModule {

    /**
     * Instantiates a new Help module.
     */
    public HelpModule() {
        this.defaultNameColor = TextColor.fromRgb(0xfdfd96);
        this.defaultValueColor = TextColor.fromLegacyFormat(ChatFormatting.AQUA);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("pie_graph"));
        lines.add(new DebugLine("fps_tps"));
        lines.add(new DebugLine("help"));

        for (DebugLine line : lines) {
            line.inReducedDebug = true;
        }
    }

    public void update(Minecraft client) {

        String visible = I18n.get("text.betterf3.line.visible");
        String hidden = I18n.get("text.betterf3.line.hidden");

        // Pie Graph (Shift+F3)
        lines.get(0).setValue(client.options.renderDebugCharts ? Utils.getStyledText(visible, TextColor.fromLegacyFormat(ChatFormatting.GREEN))
                :  Utils.getStyledText(hidden, TextColor.fromLegacyFormat(ChatFormatting.RED)));

        // FPS / TPS (Alt+F3)
        lines.get(1).setValue(client.options.renderFpsChart ? Utils.getStyledText(visible, TextColor.fromLegacyFormat(ChatFormatting.GREEN))
                :  Utils.getStyledText(hidden, TextColor.fromLegacyFormat(ChatFormatting.RED)));

        // For help
        lines.get(2).setValue(I18n.get("text.betterf3.line.help_press"));
    }
}
