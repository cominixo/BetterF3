package me.cominixo.betterf3.modules;

import java.util.Arrays;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextColor;

/**
 * The Sound module.
 */
public class SoundModule extends BaseModule {

    /**
     * Total color.
     */
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

    /**
     * Updates the Sound module.
     *
     * @param client the Minecraft client
     */
    public void update(final Minecraft client) {

        /* SoundManagerAccessor soundManagerAccessor = (SoundManagerAccessor) client.getSoundManager();
        SoundSystemAccessor soundSystemAccessor = (SoundSystemAccessor) soundManagerAccessor.getSoundSystem();
        SoundEngineAccessor soundEngineAccessor = (SoundEngineAccessor) soundSystemAccessor.getSoundEngine();

        SoundEngine.SourceSet streamingSources = soundEngineAccessor.getStreamingSources();
        SoundEngine.SourceSet staticSources = soundEngineAccessor.getStaticSources(); */

        final String debugString = client.getSoundManager().getDebugString();
        final String[] staticHandlerList =
                debugString.substring(8).substring(0, debugString.indexOf(" ")).replace(" +", "").split("/");
        final String[] streamingHandlerList = debugString.substring(debugString.indexOf("+") + 1).replace(" ", "").split("/");

        final String playing = I18n.get("text.betterf3.line.playing");
        final String maximum = I18n.get("text.betterf3.line.maximum");

        /* // Sound
        lines.get(0).value(Arrays.asList(Utils.styledText(playing, valueColor), Utils.styledText(maximum, totalColor),
                Utils.styledText(streamingSources.getSourceCount(), valueColor), Utils.styledText(streamingSources.getMaxSourceCount(), totalColor)));
        // Ambient Sound
        lines.get(1).value(Arrays.asList(Utils.styledText(playing, valueColor), Utils.styledText(maximum, totalColor),
                Utils.styledText(staticSources.getSourceCount(), valueColor), Utils.styledText(staticSources
                .getMaxSourceCount(), totalColor))); */

        // Sound
        lines.get(0).value(Arrays.asList(Utils.styledText(playing, valueColor), Utils.styledText(maximum,
                this.totalColor), Utils.styledText(staticHandlerList[0], valueColor),
                Utils.styledText(staticHandlerList[1], this.totalColor)));

        // Ambient Sound
        lines.get(1).value(Arrays.asList(Utils.styledText(playing, valueColor), Utils.styledText(maximum,
                this.totalColor), Utils.styledText(streamingHandlerList[0], valueColor),
                Utils.styledText(streamingHandlerList[1], this.totalColor)));

    }

}
