package me.cominixo.betterf3.mixin.autof3;

import java.io.File;
import me.cominixo.betterf3.config.GeneralOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Sets automatic debug in the options.
 */
@Mixin(GameOptions.class)
public class DebugOptionMixin {

  @Shadow
  public boolean debugEnabled;

  /**
   * Sets the debug option to true.
   *
   * @param client the client
   * @param optionsFile the file
   * @param ci the callback info
   */
  @Inject(method = "<init>", at = @At("RETURN"))
  public void addAutomaticDebugOption(final MinecraftClient client, final File optionsFile, final CallbackInfo ci) {
    if (GeneralOptions.autoF3) this.debugEnabled = true;
  }

}
