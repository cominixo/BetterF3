package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import me.cominixo.betterf3.mixin.sound.SoundEngineAccessor;
import me.cominixo.betterf3.mixin.sound.SoundManagerAccessor;
import me.cominixo.betterf3.mixin.sound.SoundSystemAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundEngine;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.Arrays;

public class SoundModule extends BaseModule {

    public final TextColor totalColor = TextColor.fromFormatting(Formatting.DARK_AQUA);

    public SoundModule() {

        this.defaultNameColor = TextColor.fromFormatting(Formatting.GOLD);
        this.defaultValueColor = TextColor.fromFormatting(Formatting.AQUA);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;


        lines.add(new DebugLine("sounds", "format.betterf3.total", true));
        lines.add(new DebugLine("ambient_sounds", "format.betterf3.total", true));
    }

    public void update(MinecraftClient client) {

        SoundManagerAccessor soundManagerAccessor = (SoundManagerAccessor) client.getSoundManager();
        SoundSystemAccessor soundSystemAccessor = (SoundSystemAccessor) soundManagerAccessor.getSoundSystem();
        SoundEngineAccessor soundEngineAccessor = (SoundEngineAccessor) soundSystemAccessor.getSoundEngine();

        SoundEngine.SourceSet streamingSources = soundEngineAccessor.getStreamingSources();
        SoundEngine.SourceSet staticSources = soundEngineAccessor.getStaticSources();

        // Sound
        lines.get(0).setValue(Arrays.asList(Utils.getStyledText("playing", valueColor), Utils.getStyledText("maximum", totalColor),
                Utils.getStyledText(streamingSources.getSourceCount(), valueColor), Utils.getStyledText(streamingSources.getMaxSourceCount(), totalColor)));
        // Ambient Sound
        lines.get(1).setValue(Arrays.asList(Utils.getStyledText("playing", valueColor), Utils.getStyledText("maximum", totalColor),
                Utils.getStyledText(staticSources.getSourceCount(), valueColor), Utils.getStyledText(staticSources.getMaxSourceCount(), totalColor)));


    }

}
