package me.treyruffy.betterf3.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Forge InGame GUI Mixin.
 */
@Mixin(ForgeGui.class)
public abstract class ForgeGuiMixin {

  /**
   * Minecraft Client.
   *
   * @return the minecraft client
   */
  @SuppressWarnings("checkstyle:MethodName")
  @Shadow
  public abstract MinecraftClient getMinecraft();

  /**
   * Modifies the F3 Menu from Forge's to BetterF3.
   *
   * @param width width
   * @param height width
   * @param mStack matrix stack
   * @param ci Callback info
   */
  @Inject(remap = false, method = "renderHUDText", at = @At(value = "INVOKE", opcode = Opcodes.PUTFIELD, target =
  "Lnet/minecraftforge/client/gui/overlay/ForgeGui$ForgeDebugScreenOverlay;update()V"), cancellable = true)
  public void customDebugMenu(final int width, final int height, final MatrixStack mStack, final CallbackInfo ci) {
    // Sets up BetterF3's debug screen
    new DebugHud(MinecraftClient.getInstance()).render(mStack);

    this.getMinecraft().getProfiler().pop();

    // Cancels the rest of the code from running which replaces Forge's debug screen
    ci.cancel();
  }

  /**
   * Modifies the F3 TPS Menu from Forge's to BetterF3.
   *
   * @param ci Callback info
   */
  @Inject(remap = false, method = "renderFPSGraph", at = @At(value = "HEAD"), cancellable = true)
  public void disableForgeTpsGraph(final CallbackInfo ci) {
    ci.cancel();
  }
}
