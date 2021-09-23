package me.cominixo.betterf3.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.cominixo.betterf3.utils.DebugLineList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextColor;

/**
 * The Misc right module.
 */
public class MiscRightModule extends BaseModule {

    private static final List<String> VANILLA_DEBUG_RIGHT = Arrays.asList("Java:", "Mem:", "Allocated:", "CPU:", "Display:");

    /**
     * Instantiates a new Misc right module.
     */
    public MiscRightModule() {
        this.defaultNameColor = TextColor.fromRgb(0xfdfd96);
        this.defaultValueColor = TextColor.fromLegacyFormat(ChatFormatting.AQUA);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        final DebugLineList rightDebugLines = new DebugLineList("misc_right");
        rightDebugLines.inReducedDebug = true;

        lines.add(rightDebugLines);
    }

    /**
     * Does nothing.
     *
     * @param client the Minecraft client
     */
    public void update(final Minecraft client) {
        // Do nothing
    }

    /**
     * Updates the lines.
     *
     * @param lines the lines
     */
    public void update(final List<String> lines) {

        // Parse lines to find non-vanilla lines, it's a mess
        final int rightSideSize = 0;
        if (lines.size() != rightSideSize) {

            // boolean that indicates if we're on the "targeted" section, we have no other way of knowing when it starts/ends
            boolean inTargeted = false;

            // copy of list for .remove()
            final List<String> listCopy = new ArrayList<>(lines);

            for (final String s : listCopy) {
                if (s.isEmpty()) {
                    inTargeted = false;
                    lines.remove(s);
                    continue;
                }
                if (s.startsWith(ChatFormatting.UNDERLINE + "Targeted Block") || s.startsWith( ChatFormatting.UNDERLINE + "Targeted Fluid") || s.startsWith(ChatFormatting.UNDERLINE + "Targeted Entity")) {
                    inTargeted = true;
                    lines.remove(s);
                    continue;
                }
                if (inTargeted) {
                    lines.remove(s);
                    continue;
                }
                for (final String vanilla : VANILLA_DEBUG_RIGHT) {
                    final int index = listCopy.indexOf(s);

                    if (s.startsWith(vanilla)) {
                        lines.remove(s);
                    } else if (index > 1) {
                        // pretty bad, this is to include GPU info in the vanilla list
                        if (listCopy.get(index - 1).startsWith("Display:") || listCopy.get(index - 2).startsWith(
                        "Display:")) {
                            lines.remove(s);
                        }
                    }
                }
            }
        }
        ((DebugLineList) this.lines.get(0)).values(lines);
    }
}
