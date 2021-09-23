package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextColor;

import java.util.Arrays;

/**
 * The Sound module.
 */
public class SoundModule extends BaseModule {

    public final TextColor totalColor = TextColor.fromLegacyFormat(ChatFormatting.DARK_AQUA);

    /**
     * Instantiates a new Sound module.
     */
    public SoundModule() {
        this.defaultNameColor = TextColor.fromLegacyFormat(ChatFormatting.GOLD);
        this.defaultValueColor = TextColor.fromLegacyFormat(ChatFormatting.AQUA);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("sounds", "format.betterf3.total", true));
        lines.add(new DebugLine("ambient_sounds", "format.betterf3.total", true));
    }

    public void update(Minecraft client) {

        /* SoundManagerAccessor soundManagerAccessor = (SoundManagerAccessor) client.getSoundManager();
        SoundSystemAccessor soundSystemAccessor = (SoundSystemAccessor) soundManagerAccessor.getSoundSystem();
        SoundEngineAccessor soundEngineAccessor = (SoundEngineAccessor) soundSystemAccessor.getSoundEngine();

        SoundEngine.SourceSet streamingSources = soundEngineAccessor.getStreamingSources();
        SoundEngine.SourceSet staticSources = soundEngineAccessor.getStaticSources(); */

        String debugString = client.getSoundManager().getDebugString();
        String[] staticHandlerList =
                debugString.substring(8).substring(0, debugString.indexOf(" ")).replace(" +", "").split("/");
        String[] streamingHandlerList = debugString.substring(debugString.indexOf("+") + 1).replace(" ", "").split("/");

        String playing = I18n.get("text.betterf3.line.playing");
        String maximum = I18n.get("text.betterf3.line.maximum");

        /* // Sound
        lines.get(0).setValue(Arrays.asList(Utils.getStyledText(playing, valueColor), Utils.getStyledText(maximum, totalColor),
                Utils.getStyledText(streamingSources.getSourceCount(), valueColor), Utils.getStyledText(streamingSources.getMaxSourceCount(), totalColor)));
        // Ambient Sound
        lines.get(1).setValue(Arrays.asList(Utils.getStyledText(playing, valueColor), Utils.getStyledText(maximum, totalColor),
                Utils.getStyledText(staticSources.getSourceCount(), valueColor), Utils.getStyledText(staticSources
                .getMaxSourceCount(), totalColor))); */

        // Sound
        lines.get(0).setValue(Arrays.asList(Utils.getStyledText(playing, valueColor), Utils.getStyledText(maximum,
                        totalColor), Utils.getStyledText(staticHandlerList[0], valueColor),
                Utils.getStyledText(staticHandlerList[1], totalColor)));

        // Ambient Sound
        lines.get(1).setValue(Arrays.asList(Utils.getStyledText(playing, valueColor), Utils.getStyledText(maximum,
                        totalColor), Utils.getStyledText(streamingHandlerList[0], valueColor),
                Utils.getStyledText(streamingHandlerList[1], totalColor)));

    }

}
