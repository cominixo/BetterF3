package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.DebugLineList;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;


public class TargetModule extends BaseModule{

    public TargetModule() {
        this.defaultNameColor = TextColor.fromRgb(0x00aaff);
        this.defaultValueColor = TextColor.fromFormatting(Formatting.YELLOW);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("targeted_block"));
        lines.add(new DebugLine("id_block"));
        lines.add(new DebugLineList("block_states"));
        lines.add(new DebugLineList("block_tags"));
        lines.add(new DebugLine(""));
        lines.add(new DebugLine("targeted_fluid"));
        lines.add(new DebugLine("id_fluid"));
        lines.add(new DebugLineList("fluid_states"));
        lines.add(new DebugLineList("fluid_tags"));
        lines.add(new DebugLine(""));
        lines.add(new DebugLine("targeted_entity"));

    }

    @Override
    public void update(MinecraftClient client) {


        Entity cameraEntity = client.getCameraEntity();

        if (cameraEntity == null) {
            return;
        }

        HitResult blockHit = cameraEntity.rayTrace(20.0D, 0.0F, false);
        HitResult fluidHit = cameraEntity.rayTrace(20.0D, 0.0F, true);

        BlockPos blockPos;

        if (blockHit.getType() == HitResult.Type.BLOCK) {

            blockPos = ((BlockHitResult) blockHit).getBlockPos();
            BlockState blockState = client.world.getBlockState(blockPos);

            lines.get(0).setValue(blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ());
            lines.get(1).setValue(String.valueOf(Registry.BLOCK.getId(blockState.getBlock())));

            List<String> blockStates = new ArrayList<>();

            blockState.getEntries().entrySet().forEach((entry -> blockStates.add(Utils.propertyToString(entry))));

            ((DebugLineList)lines.get(2)).setValues(blockStates);

            List<String> blockTags = new ArrayList<>();

            client.getNetworkHandler().getTagManager().blocks().getTagsFor(blockState.getBlock())
                    .forEach((blockTag -> blockTags.add("#" + blockTag)));

            ((DebugLineList)lines.get(3)).setValues(blockTags);

        } else {
            for (int i = 0; i < 5; i++) {
                lines.get(i).active = false;
            }
        }


        if (fluidHit.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK) {
            blockPos = ((BlockHitResult) fluidHit).getBlockPos();
            FluidState fluidState = client.world.getFluidState(blockPos);

            lines.get(5).setValue(blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ());
            lines.get(6).setValue(Registry.FLUID.getId(fluidState.getFluid()));

            List<String> fluidStates = new ArrayList<>();

            fluidState.getEntries().entrySet().forEach((entry) -> fluidStates.add(Utils.propertyToString(entry)));

            ((DebugLineList)lines.get(7)).setValues(fluidStates);

            List<String> fluidTags = new ArrayList<>();

            client.getNetworkHandler().getTagManager().fluids().getTagsFor(fluidState.getFluid())
                    .forEach((fluidTag -> fluidTags.add("#" + fluidTag)));

            ((DebugLineList)lines.get(8)).setValues(fluidTags);

        } else {
            for (int i = 5; i < 10; i++) {
                lines.get(i).active = false;
            }
        }

        Entity entity = client.targetedEntity;
        if (entity != null) {
            lines.get(10).setValue(Registry.ENTITY_TYPE.getId(entity.getType()));
        } else {
            lines.get(10).active = false;
        }

    }
}
