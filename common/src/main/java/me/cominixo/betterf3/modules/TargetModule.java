package me.cominixo.betterf3.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        this.defaultValueColor = TextColor.fromFormatting(Formatting.YELLOW);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("targeted_block"));
        lines.add(new DebugLine("id_block"));
        lines.add(new DebugLineList("block_states"));
        lines.add(new DebugLineList("block_tags"));
        lines.add(new DebugLine("blank"));
        lines.add(new DebugLine("targeted_fluid"));
        lines.add(new DebugLine("id_fluid"));
        lines.add(new DebugLineList("fluid_states"));
        lines.add(new DebugLineList("fluid_tags"));
        lines.add(new DebugLine("blank2"));
        lines.add(new DebugLine("targeted_entity"));
    }

    /**
     * Updates the Target module.
     *
     * @param client the Minecraft client
     */
    public void update(final @NotNull MinecraftClient client) {
        final Entity cameraEntity = client.getCameraEntity();

        if (cameraEntity == null) {
            return;
        }
        final HitResult blockHit = cameraEntity.raycast(20.0D, 0.0F, false);
        final HitResult fluidHit = cameraEntity.raycast(20.0D, 0.0F, true);

        BlockPos blockPos;

        if (blockHit.getType() == HitResult.Type.BLOCK) {
            blockPos = ((BlockHitResult) blockHit).getBlockPos();
            assert client.world != null;
            final BlockState blockState = client.world.getBlockState(blockPos);

            lines.get(0).value(blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ());
            lines.get(1).value(String.valueOf(Registry.BLOCK.getId(blockState.getBlock())));

            final List<String> blockStates = new ArrayList<>();

            blockState.getEntries().entrySet().forEach(entry -> blockStates.add(Utils.propertyToString(entry)));

            ((DebugLineList) lines.get(2)).values(blockStates);

            final List<String> blockTags = new ArrayList<>();

            Objects.requireNonNull(client.getNetworkHandler()).getTagManager().getOrCreateTagGroup(Registry.BLOCK_KEY).getTagsFor(blockState.getBlock())
                    .forEach(blockTag -> blockTags.add("#" + blockTag));

            ((DebugLineList) lines.get(3)).values(blockTags);
        } else {
            for (int i = 0; i < 5; i++) {
                lines.get(i).active = false;
            }
        }

        if (fluidHit.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK) {
            blockPos = ((BlockHitResult) fluidHit).getBlockPos();
            assert client.world != null;
            final FluidState fluidState = client.world.getFluidState(blockPos);

            lines.get(5).value(blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ());
            lines.get(6).value(Registry.FLUID.getId(fluidState.getFluid()));

            final List<String> fluidStates = new ArrayList<>();

            fluidState.getEntries().entrySet().forEach(entry -> fluidStates.add(Utils.propertyToString(entry)));

            ((DebugLineList) lines.get(7)).values(fluidStates);

            final List<String> fluidTags = new ArrayList<>();

            Objects.requireNonNull(client.getNetworkHandler()).getTagManager().getOrCreateTagGroup(Registry.FLUID_KEY).getTagsFor(fluidState.getFluid())
                    .forEach(fluidTag -> fluidTags.add("#" + fluidTag));

            ((DebugLineList) lines.get(8)).values(fluidTags);
        } else {
            for (int i = 5; i < 10; i++) {
                lines.get(i).active = false;
            }
        }
        final Entity entity = client.targetedEntity;
        if (entity != null) {
            lines.get(10).value(Registry.ENTITY_TYPE.getId(entity.getType()));
        } else {
            lines.get(10).active = false;
        }
    }
}
