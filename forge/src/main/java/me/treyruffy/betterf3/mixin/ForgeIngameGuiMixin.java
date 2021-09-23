package me.treyruffy.betterf3.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Forge InGame GUI Mixin
 */
@Mixin(ForgeIngameGui.class)
public abstract class ForgeIngameGuiMixin {

	/**
	 * Modifies the F3 Menu from Forge's to BetterF3
	 */
	@Inject(remap = false, method = "renderHUDText", at = @At(value = "INVOKE", opcode = Opcodes.PUTFIELD, target =
			"net/minecraftforge/client/gui/ForgeIngameGui$GuiOverlayDebugForge.update()V"), cancellable = true)
	public void customDebugMenu(int width, int height, PoseStack mStack, CallbackInfo ci) {
		// Sets up BetterF3's debug screen
		new DebugScreenOverlay(Minecraft.getInstance()).render(mStack);

		// Cancels the rest of the code from running which replaces Forge's debug screen
		ci.cancel();
	}
}
