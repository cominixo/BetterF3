package me.cominixo.betterf3.mixin.autof3;

import me.cominixo.betterf3.config.GeneralOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin to automatically start the F3 menu.
 */

@Mixin(MinecraftClient.class)
public class AutomaticDebugMixin {

  @Redirect(method = "openScreen", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;debugEnabled:Z", opcode = Opcodes.PUTFIELD))
  private void removeDebugCrosshair(final GameOptions instance, final boolean value) {
    if (!GeneralOptions.disableMod) {
      if (GeneralOptions.autoF3) {
        instance.debugEnabled = true;
        return;
      }
    }
    instance.debugEnabled = false;
  }

}
