package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.Option;
import net.minecraft.client.resource.language.I18n;

import java.util.Collections;

public class FpsModule extends BaseModule{

    public FpsModule() {

        lines.add(new DebugLine("fps", "format.betterf3.no_format", true));
        lines.get(0).inReducedDebug = true;

    }

    public void update(MinecraftClient client) {

        int currentFps = Integer.parseInt(client.fpsDebugString.split(" ")[0]);

        String fpsString = I18n.translate("format.betterf3.fps", currentFps, (double)client.options.maxFps == Option.FRAMERATE_LIMIT.getMax() ? I18n.translate("text.betterf3.line.fps.unlimited") : client.options.maxFps,
                client.options.enableVsync ? I18n.translate("text.betterf3.line.fps.vsync") : "").trim();

        lines.get(0).setValue(Collections.singletonList(Utils.getStyledText(fpsString, Utils.getFpsColor(currentFps))));


    }


}
