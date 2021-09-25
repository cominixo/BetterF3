package me.cominixo.betterf3.modules;

import java.util.Arrays;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

/**
 * The Coordinates module.
 */
public class CoordsModule extends BaseModule {

    /**
     * The color for the x position.
     */
    public TextColor colorX;
    /**
     * The color for the y position.
     */
    public TextColor colorY;
    /**
     * The color for the z position.
     */
    public TextColor colorZ;

    /**
     * The default color for the x position.
     */
    public final TextColor defaultColorX = TextColor.fromFormatting(Formatting.RED);
    /**
     * The default color for the y position.
     */
    public final TextColor defaultColorY = TextColor.fromFormatting(Formatting.GREEN);
    /**
     * The default color for the z position.
     */
    public final TextColor defaultColorZ = TextColor.fromFormatting(Formatting.AQUA);

    /**
     * Instantiates a new Coordinates module.
     */
    public CoordsModule() {
        this.defaultNameColor = TextColor.fromFormatting(Formatting.RED);

        this.nameColor = defaultNameColor;
        this.colorX = this.defaultColorX;
        this.colorY = this.defaultColorY;
        this.colorZ = this.defaultColorZ;

        lines.add(new DebugLine("player_coords", "format.betterf3.coords", true));
        lines.add(new DebugLine("block_coords", "format.betterf3.coords", true));
        lines.add(new DebugLine("chunk_relative_coords", "format.betterf3.coords", true));
        lines.add(new DebugLine("chunk_coords", "format.betterf3.coords", true));

        lines.get(2).inReducedDebug = true;
    }

    /**
     * Updates the Coordinates module.
     *
     * @param client the Minecraft client
     */
    public void update(final MinecraftClient client) {

        final Entity cameraEntity = client.getCameraEntity();

        final Text xyz =
        Utils.styledText("X", this.colorX).append(Utils.styledText("Y", this.colorY)).append(Utils.styledText("Z",
                this.colorZ));

        if (cameraEntity != null) {
            final String cameraX = String.format("%.3f", cameraEntity.getX());
            final String cameraY = String.format("%.5f", cameraEntity.getY());
            final String cameraZ = String.format("%.3f", cameraEntity.getZ());

            // Player coords
            lines.get(0).value(Arrays.asList(xyz, Utils.styledText(cameraX, this.colorX),
                    Utils.styledText(cameraY, this.colorY), Utils.styledText(cameraZ, this.colorZ)));

            final BlockPos blockPos = cameraEntity.getBlockPos();
            // Block coords
            lines.get(1).value(Arrays.asList(Utils.styledText(blockPos.getX(), this.colorX),
                    Utils.styledText(blockPos.getY(), this.colorY), Utils.styledText(blockPos.getZ(), this.colorZ)));
            // Chunk Relative coords
            lines.get(2).value(Arrays.asList(Utils.styledText(blockPos.getX() & 15, this.colorX),
                    Utils.styledText(blockPos.getY() & 15, this.colorY), Utils.styledText(blockPos.getZ() & 15, this.colorZ)));
            // Chunk coords
            lines.get(3).value(Arrays.asList(Utils.styledText(blockPos.getX() >> 4, this.colorX),
                    Utils.styledText(blockPos.getY() >> 4, this.colorY), Utils.styledText(blockPos.getZ() >> 4, this.colorZ)));
        }
    }
}
