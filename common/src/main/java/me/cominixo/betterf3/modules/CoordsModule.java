package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;

import java.util.Arrays;

public class CoordsModule extends BaseModule{


    public TextColor colorX;
    public TextColor colorY;
    public TextColor colorZ;

    public TextColor defaultColorX = TextColor.fromLegacyFormat(ChatFormatting.RED);
    public TextColor defaultColorY = TextColor.fromLegacyFormat(ChatFormatting.GREEN);
    public TextColor defaultColorZ = TextColor.fromLegacyFormat(ChatFormatting.AQUA);

    public CoordsModule() {

        this.defaultNameColor = TextColor.fromLegacyFormat(ChatFormatting.RED);

        this.nameColor = defaultNameColor;
        this.colorX = defaultColorX;
        this.colorY = defaultColorY;
        this.colorZ = defaultColorZ;


        lines.add(new DebugLine("player_coords", "format.betterf3.coords", true));
        lines.add(new DebugLine("block_coords", "format.betterf3.coords", true));
        lines.add(new DebugLine("chunk_relative_coords", "format.betterf3.coords", true));
        lines.add(new DebugLine("chunk_coords", "format.betterf3.coords", true));

        lines.get(2).inReducedDebug = true;

    }

    public void update(Minecraft client) {

        Entity cameraEntity = client.getCameraEntity();

        Component xyz = Utils.getStyledText("X", colorX).append(Utils.getStyledText("Y", colorY)).append(Utils.getStyledText("Z", colorZ));

        if (cameraEntity != null) {


            String cameraX = String.format("%.3f", cameraEntity.getX());
            String cameraY = String.format("%.5f", cameraEntity.getY());
            String cameraZ = String.format("%.3f", cameraEntity.getZ());

            // Player coords
            lines.get(0).setValue(Arrays.asList(xyz, Utils.getStyledText(cameraX, colorX),
                    Utils.getStyledText(cameraY, colorY), Utils.getStyledText(cameraZ, colorZ)));

            BlockPos blockPos = cameraEntity.blockPosition();
            // Block coords
            lines.get(1).setValue(Arrays.asList(Utils.getStyledText(blockPos.getX(), colorX),
                    Utils.getStyledText(blockPos.getY(), colorY), Utils.getStyledText(blockPos.getZ(), colorZ)));
            // Chunk Relative coords
            lines.get(2).setValue(Arrays.asList(Utils.getStyledText(blockPos.getX() & 15, colorX),
                    Utils.getStyledText(blockPos.getY() & 15, colorY), Utils.getStyledText(blockPos.getZ() & 15, colorZ)));
            // Chunk coords
            lines.get(3).setValue(Arrays.asList(Utils.getStyledText(blockPos.getX() >> 4, colorX),
                    Utils.getStyledText(blockPos.getY() >> 4, colorY), Utils.getStyledText(blockPos.getZ() >> 4, colorZ)));
        }



    }

}
