package me.cominixo.betterf3.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.DebugLineList;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

/**
 * The Target module.
 */
public class TargetModule extends BaseModule {

    /**
     * Instantiates a new Target module.
     */
    public TargetModule() {
        this.defaultNameColor = TextColor.fromRgb(0x00aaff);
        this.defaultValueColor = TextColor.fromLegacyFormat(ChatFormatting.YELLOW);

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

    /**
     * Updates the Target module.
     *
     * @param client the Minecraft client
     */
    public void update(final @NotNull Minecraft client) {
        final Entity cameraEntity = client.getCameraEntity();

        if (cameraEntity == null) {
            return;
        }
        final HitResult blockHit = cameraEntity.pick(20.0D, 0.0F, false);
        final HitResult fluidHit = cameraEntity.pick(20.0D, 0.0F, true);

        BlockPos blockPos;

        if (blockHit.getType() == HitResult.Type.BLOCK) {
            blockPos = ((BlockHitResult) blockHit).getBlockPos();
            assert client.level != null;
            final BlockState blockState = client.level.getBlockState(blockPos);

            lines.get(0).value(blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ());
            lines.get(1).value(String.valueOf(Registry.BLOCK.getKey(blockState.getBlock())));

            final List<String> blockStates = new ArrayList<>();

            blockState.getValues().entrySet().forEach(entry -> blockStates.add(Utils.propertyToString(entry)));

            ((DebugLineList) lines.get(2)).values(blockStates);

            final List<String> blockTags = new ArrayList<>();

            Objects.requireNonNull(client.getConnection()).getTags().getOrEmpty(Registry.BLOCK_REGISTRY).getMatchingTags(blockState.getBlock())
                    .forEach(blockTag -> blockTags.add("#" + blockTag));

            ((DebugLineList) lines.get(3)).values(blockTags);
        } else {
            for (int i = 0; i < 5; i++) {
                lines.get(i).active = false;
            }
        }

        if (fluidHit.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
            blockPos = ((BlockHitResult) fluidHit).getBlockPos();
            assert client.level != null;
            final FluidState fluidState = client.level.getFluidState(blockPos);

            lines.get(5).value(blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ());
            lines.get(6).value(Registry.FLUID.getKey(fluidState.getType()));

            final List<String> fluidStates = new ArrayList<>();

            fluidState.getValues().entrySet().forEach(entry -> fluidStates.add(Utils.propertyToString(entry)));

            ((DebugLineList) lines.get(7)).values(fluidStates);

            final List<String> fluidTags = new ArrayList<>();

            Objects.requireNonNull(client.getConnection()).getTags().getOrEmpty(Registry.FLUID_REGISTRY).getMatchingTags(fluidState.getType())
                    .forEach(fluidTag -> fluidTags.add("#" + fluidTag));

            ((DebugLineList) lines.get(8)).values(fluidTags);
        } else {
            for (int i = 5; i < 10; i++) {
                lines.get(i).active = false;
            }
        }
        final Entity entity = client.crosshairPickEntity;
        if (entity != null) {
            lines.get(10).value(Registry.ENTITY_TYPE.getKey(entity.getType()));
        } else {
            lines.get(10).active = false;
        }
    }
}
