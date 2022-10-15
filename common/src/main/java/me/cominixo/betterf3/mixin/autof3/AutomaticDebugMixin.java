package me.cominixo.betterf3.mixin.autof3;

import me.cominixo.betterf3.config.GeneralOptions;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.GameOptions;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin to automatically start the F3 menu.
 */

@Mixin(InGameHud.class)
public class AutomaticDebugMixin {

  @Redirect(method = "clear", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;debugEnabled:Z", opcode = Opcodes.PUTFIELD))
  private void automaticF3(final GameOptions instance, final boolean value) {
    if (!GeneralOptions.disableMod) {
      if (GeneralOptions.autoF3) {
        instance.debugEnabled = true;
        instance.debugProfilerEnabled = false;
        instance.debugTpsEnabled = false;
        return;
      }
    }
    instance.debugEnabled = false;
  }

}
