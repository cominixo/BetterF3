package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.Option;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.Collections;

public class FpsModule extends BaseModule{

    public TextColor colorHigh;
    public TextColor colorMed;
    public TextColor colorLow;

    public TextColor defaultColorHigh = TextColor.fromFormatting(Formatting.GREEN);
    public TextColor defaultColorMed = TextColor.fromFormatting(Formatting.YELLOW);
    public TextColor defaultColorLow = TextColor.fromFormatting(Formatting.RED);

    public FpsModule() {

        lines.add(new DebugLine("fps", "format.betterf3.no_format", true));
        lines.get(0).inReducedDebug = true;

        colorHigh = defaultColorHigh;
        colorMed = defaultColorMed;
        colorLow = defaultColorLow;

    }

    public void update(MinecraftClient client) {

        int currentFps = Integer.parseInt(client.fpsDebugString.split(" ")[0].split("/")[0]);

        String fpsString = I18n.translate("format.betterf3.fps", currentFps, (double)client.options.maxFps == Option.FRAMERATE_LIMIT.getMax() ? I18n.translate("text.betterf3.line.fps.unlimited") : client.options.maxFps,
                client.options.enableVsync ? I18n.translate("text.betterf3.line.fps.vsync") : "").trim();

        TextColor color;

        switch (Utils.getFpsColor(currentFps)) {
            case 0:
                color = colorHigh;
                break;
            case 1:
                color = colorMed;
                break;
            case 2:
                color = colorLow;
                break;
            default:
                color = TextColor.fromFormatting(Formatting.RESET);
        }

        lines.get(0).setValue(Collections.singletonList(Utils.getStyledText(fpsString, color)));


    }


}
