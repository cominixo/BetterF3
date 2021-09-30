package me.cominixo.betterf3.modules;

import java.util.Arrays;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

/**
 * The Sound module.
 */
public class SoundModule extends BaseModule {

    /**
     * Total color.
     */
    public final TextColor totalColor = TextColor.fromFormatting(Formatting.DARK_AQUA);

    /**
     * Instantiates a new Sound module.
     */
    public SoundModule() {
        this.defaultNameColor = TextColor.fromFormatting(Formatting.GOLD);
        this.defaultValueColor = TextColor.fromFormatting(Formatting.AQUA);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("sounds", "format.betterf3.total", true));
        lines.add(new DebugLine("ambient_sounds", "format.betterf3.total", true));
        lines.add(new DebugLine("mood"));
    }

    /**
     * Updates the Sound module.
     *
     * @param client the Minecraft client
     */
    public void update(final MinecraftClient client) {

        /* SoundManagerAccessor soundManagerAccessor = (SoundManagerAccessor) client.getSoundManager();
        SoundSystemAccessor soundSystemAccessor = (SoundSystemAccessor) soundManagerAccessor.getSoundSystem();
        SoundEngineAccessor soundEngineAccessor = (SoundEngineAccessor) soundSystemAccessor.getSoundEngine();

        SoundEngine.SourceSet streamingSources = soundEngineAccessor.getStreamingSources();
        SoundEngine.SourceSet staticSources = soundEngineAccessor.getStaticSources(); */

        final String debugString = client.getSoundManager().getDebugString();
        final String[] staticHandlerList =
                debugString.substring(8).substring(0, debugString.indexOf(" ")).replace(" +", "").split("/");
        final String[] streamingHandlerList = debugString.substring(debugString.indexOf("+") + 1).replace(" ", "").split("/");

        final String playing = I18n.translate("text.betterf3.line.playing");
        final String maximum = I18n.translate("text.betterf3.line.maximum");

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

        // Mood
        assert client.player != null;
        lines.get(2).value(Math.round(client.player.getMoodPercentage() * 100.0F) + "%");

    }

}
