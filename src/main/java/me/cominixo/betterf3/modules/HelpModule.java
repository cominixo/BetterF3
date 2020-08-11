package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;


public class HelpModule extends BaseModule {


    public HelpModule() {
        this.defaultNameColor = TextColor.fromRgb(0xfdfd96);
        this.defaultValueColor = TextColor.fromFormatting(Formatting.AQUA);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;


        lines.add(new DebugLine("pie_graph"));
        lines.add(new DebugLine("fps_tps"));
        lines.add(new DebugLine("help"));

        for (DebugLine line : lines) {
            line.inReducedDebug = true;
        }

    }

    public void update(MinecraftClient client) {

        // Pie Graph (Shift+F3)
        lines.get(0).setValue(client.options.debugProfilerEnabled ? Utils.getStyledText("Visible", TextColor.fromFormatting(Formatting.GREEN))
                :  Utils.getStyledText("Hidden", TextColor.fromFormatting(Formatting.RED)));

        // FPS / TPS (Alt+F3)
        lines.get(1).setValue(client.options.debugTpsEnabled ? Utils.getStyledText("Visible", TextColor.fromFormatting(Formatting.GREEN))
                :  Utils.getStyledText("Hidden", TextColor.fromFormatting(Formatting.RED)));

        // For help
        lines.get(2).setValue("Press F3 + Q");

    }
}
