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

@Mixin(ForgeIngameGui.class)
public abstract class ForgeIngameGuiMixin {

	@Inject(remap = false, method = "renderHUDText", at = @At(value = "INVOKE", opcode = Opcodes.PUTFIELD, target =
			"net/minecraftforge/client/gui/ForgeIngameGui$GuiOverlayDebugForge.update()V"), cancellable = true)
	public void customDebugMenu(int width, int height, PoseStack mStack, CallbackInfo ci) {
		new DebugScreenOverlay(Minecraft.getInstance()).render(mStack);
		ci.cancel();
	}
}
