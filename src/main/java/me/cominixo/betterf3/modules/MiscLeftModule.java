package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLineList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MiscLeftModule extends BaseModule{

    private static final List<String> VANILLA_DEBUG_LEFT = Arrays.asList("Minecraft", "Integrated", "C:", "E:", "P:", "Client", "ServerChunkCache:", "minecraft:overworld", "XYZ:", "Block:", "Chunk:", "Facing:", "Waiting", "SC:", "Sounds:", "CH", "SH", "Server Light:", "Biome:", "Local Difficulty:");
    private int leftSideSize = 0;


    public MiscLeftModule() {

        this.defaultNameColor = TextColor.fromRgb(0xfdfd96);
        this.defaultValueColor = TextColor.fromFormatting(Formatting.AQUA);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        DebugLineList leftDebugLines = new DebugLineList("misc_left");
        leftDebugLines.inReducedDebug = true;

        lines.add(leftDebugLines);

    }

    public void update(MinecraftClient client) {
        // Do nothing
    }

    public void update(List<String> lines) {

        // Parse lines to find non-vanilla lines, it's a mess
        if (lines.size() != leftSideSize) {

            // copy of list for .remove()
            List<String> listCopy = new ArrayList<>(lines);

            for (String s : listCopy) {

                if (s.isEmpty()) {
                    lines.remove(s);
                }
                String[] stringSplit = s.split(" ");

                for (String vanilla : VANILLA_DEBUG_LEFT) {

                    if (s.startsWith(vanilla)) {
                        lines.remove(s);
                    } else if (stringSplit.length > 1) {

                        if (stringSplit[1].equals("fps")) {
                            lines.remove(s);
                        }
                    }
                    if (s.contains("tx") && s.contains("rx")) {
                        // Have to do this to check if it's a vanilla server info line
                        lines.remove(s);
                    }
                }
            }
            leftSideSize = lines.size();
        }

        ((DebugLineList) this.lines.get(0)).setValues(lines);

    }


}
