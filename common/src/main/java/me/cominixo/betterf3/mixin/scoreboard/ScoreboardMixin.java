package me.cominixo.betterf3.mixin.scoreboard;

import me.cominixo.betterf3.config.GeneralOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to cancel sidebar rendering during F3.
 */
@Mixin(InGameHud.class)
public class ScoreboardMixin {

  @Shadow @Final private MinecraftClient client;

  /**
   * Cancels sidebar rendering during F3 if the {@link GeneralOptions#hideSidebar} setting is true.
   *
   * @param info Callback info
   */
  @Inject(at = @At("HEAD"), method = "renderScoreboardSidebar", cancellable = true)
  public void init(final CallbackInfo info) {
    if (GeneralOptions.hideSidebar) {
      if (this.client.options.debugEnabled) {
        info.cancel();
      }
    }
  }
}
