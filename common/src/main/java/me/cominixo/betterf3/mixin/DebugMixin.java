package me.cominixo.betterf3.mixin;

import com.google.common.base.Strings;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.MiscLeftModule;
import me.cominixo.betterf3.modules.MiscRightModule;
import me.cominixo.betterf3.utils.PositionEnum;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.Text;
import net.minecraft.util.MetricsData;
import net.minecraft.util.Util;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.cominixo.betterf3.utils.Utils.START_X_POS;
import static me.cominixo.betterf3.utils.Utils.closingAnimation;
import static me.cominixo.betterf3.utils.Utils.lastAnimationUpdate;
import static me.cominixo.betterf3.utils.Utils.xPos;

/**
 * The Debug Screen Overlay.
 */
@Mixin(DebugHud.class)
public abstract class DebugMixin {

  @Shadow @Final private MinecraftClient client;
  @Shadow @Final private TextRenderer textRenderer;

  /**
   * Gets the information on the left side of the screen.
   *
   * @return the game information
   */
  @SuppressWarnings("checkstyle:MethodName")
  @Shadow protected abstract List<String> getLeftText();

  /**
   * Gets the information on the right side of the screen.
   *
   * @return the system information
   */
  @SuppressWarnings("checkstyle:MethodName")
  @Shadow protected abstract List<String> getRightText();

  @Shadow protected abstract void drawMetricsData(MatrixStack matrices, MetricsData metricsData, int x, int width,
                                                  boolean showFps);

  /**
   * Sets up modules on the left side of the screen.
   *
   * @return the left side modules
   */
  public List<Text> newLeftText() {

    final List<Text> list = new ArrayList<>();

    for (final BaseModule module : BaseModule.modules) {
      if (!module.enabled) {
        continue;
      }
      if (module instanceof MiscLeftModule) {
        ((MiscLeftModule) module).update(this.getLeftText());
      } else if (module instanceof MiscRightModule) {
        ((MiscRightModule) module).update(this.getRightText());
      } else {
        module.update(this.client);
      }

      list.addAll(module.linesFormatted(this.client.hasReducedDebugInfo()));
      if (GeneralOptions.spaceEveryModule) {
        list.add(Text.of(""));
      }
    }

    return list;

  }

  /**
   * Sets up modules on the right side of the screen.
   *
   * @return the right side modules
   */
  public List<Text> newRightText() {

    final List<Text> list = new ArrayList<>();

    for (final BaseModule module : BaseModule.modulesRight) {
      if (!module.enabled) {
        continue;
      }
      if (module instanceof MiscRightModule) {
        ((MiscRightModule) module).update(this.getRightText());
      } else if (module instanceof MiscLeftModule) {
        ((MiscLeftModule) module).update(this.getLeftText());
      } else {
        module.update(this.client);
      }

      list.addAll(module.linesFormatted(this.client.hasReducedDebugInfo()));
      if (GeneralOptions.spaceEveryModule) {
        list.add(Text.of(""));
      }
    }

    return list;
  }

  /**
   * Renders the text on the right side of the screen.
   *
   * @param matrixStack matrixStack
   * @param ci Callback info
   */
  @Inject(method = "renderRightText", at = @At("HEAD"), cancellable = true)
  public void renderRightText(final MatrixStack matrixStack, final CallbackInfo ci) {

    if (GeneralOptions.disableMod) {
      return;
    }

    final List<Text> list = this.newRightText();

    final VertexConsumerProvider.Immediate immediate = this.immediate(PositionEnum.RIGHT, list, matrixStack);

    for (int i = 0; i < list.size(); i++) {

      if (!Strings.isNullOrEmpty(list.get(i).getString())) {
        final int height = 9;
        final int width = this.textRenderer.getWidth(list.get(i).getString());
        int windowWidth =
        (int) (this.client.getWindow().getScaledWidth() / GeneralOptions.fontScale) - 2 - width;
        if (GeneralOptions.enableAnimations) {
          windowWidth += xPos;
        }
        final int y = 2 + height * i;

        this.textRenderer.draw(list.get(i), windowWidth, y, 0xE0E0E0, GeneralOptions.shadowText, matrixStack.peek().getPositionMatrix(), immediate, false, 0, 15728880);
      }
    }
    immediate.draw();

    ci.cancel();
  }

  /**
   * Lets us draw in batches.
   *
   * @param pos The position
   * @param list The list of Text
   * @param matrixStack The MatrixStack
   * @return VertexConsumerProvider
   */
  public VertexConsumerProvider.Immediate immediate(final PositionEnum pos, final List<Text> list,
                                                    final MatrixStack matrixStack) {

    final float f = (float) (GeneralOptions.backgroundColor >> 24 & 255) / 255.0F;
    final float g = (float) (GeneralOptions.backgroundColor >> 16 & 255) / 255.0F;
    final float h = (float) (GeneralOptions.backgroundColor >> 8 & 255) / 255.0F;
    final float k = (float) (GeneralOptions.backgroundColor & 255) / 255.0F;
    final BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
    RenderSystem.enableBlend();
    RenderSystem.disableTexture();
    RenderSystem.defaultBlendFunc();
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

    for (int i = 0; i < list.size(); i++) {
      final int height = 9;
      final int width = this.textRenderer.getWidth(list.get(i).getString());
      if (width == 0) {
        continue;
      }
      final int y = 2 + height * i;

      int x1;
      int x2;
      int y1;
      int y2;
      int j;

      int windowWidth;
      if (pos == PositionEnum.RIGHT) {
        windowWidth = (int) (this.client.getWindow().getScaledWidth() / GeneralOptions.fontScale) - 2 - width;
        if (GeneralOptions.enableAnimations) {
          windowWidth += xPos;
        }

        x1 = 1 + windowWidth;
        x2 = windowWidth + width;
      } else {
        windowWidth = 2;

        if (GeneralOptions.enableAnimations) {
          windowWidth -= xPos;
        }
        x1 = windowWidth - 1;
        x2 = width + 3 + windowWidth;
      }
      y1 = y - 1;
      y2 = y + height - 1;

      final Matrix4f matrix = matrixStack.peek().getPositionMatrix();

      if (x1 < x2) {
        j = x1;
        x1 = x2;
        x2 = j;
      }

      if (y1 < y2) {
        j = y1;
        y1 = y2;
        y2 = j;
      }

      bufferBuilder.vertex(matrix, (float) x1, (float) y2, 0.0F).color(g, h, k, f).next();
      bufferBuilder.vertex(matrix, (float) x2, (float) y2, 0.0F).color(g, h, k, f).next();
      bufferBuilder.vertex(matrix, (float) x2, (float) y1, 0.0F).color(g, h, k, f).next();
      bufferBuilder.vertex(matrix, (float) x1, (float) y1, 0.0F).color(g, h, k, f).next();

    }
    BufferRenderer.drawWithShader(bufferBuilder.end());
    RenderSystem.enableTexture();
    RenderSystem.disableBlend();

    return VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());

  }

  /**
   * Renders the text on the left side of the screen.
   *
   * @param matrixStack matrixStack
   * @param ci Callback info
   */
  @Inject(method = "renderLeftText", at = @At("HEAD"), cancellable = true)
  public void renderLeftText(final MatrixStack matrixStack, final CallbackInfo ci) {

    if (GeneralOptions.disableMod) {
      return;
    }

    final List<Text> list = this.newLeftText();
    final VertexConsumerProvider.Immediate immediate = this.immediate(PositionEnum.LEFT, list, matrixStack);

    for (int i = 0; i < list.size(); i++) {

      if (!Strings.isNullOrEmpty(list.get(i).getString())) {

        final int height = 9;
        final int y = 2 + height * i;
        int xPosLeft = 2;

        if (GeneralOptions.enableAnimations) {
          xPosLeft -= xPos;
        }

        this.textRenderer.draw(list.get(i), xPosLeft, y, 0xE0E0E0, GeneralOptions.shadowText, matrixStack.peek().getPositionMatrix(), immediate, false, 0, 15728880);
      }
    }
    immediate.draw();

    ci.cancel();
  }

  /**
   * Ensures that the TPS graph works.
   *
   * @param matrices matrixStack
   * @param ci Callback info
   */
  @Inject(method = "render", at = @At(value = "HEAD"))
  public void renderBefore(final MatrixStack matrices, final CallbackInfo ci) {
    matrices.push();
    if (this.client.options.debugTpsEnabled) {
      final int scaledWidth = this.client.getWindow().getScaledWidth();
      this.drawMetricsData(matrices, this.client.getMetricsData(), 0, scaledWidth / 2, true);
      final IntegratedServer integratedServer = this.client.getServer();
      if (integratedServer != null) {
        this.drawMetricsData(matrices, integratedServer.getMetricsData(), scaledWidth - Math.min(scaledWidth / 2, 240), scaledWidth / 2, false);
      }
    }
  }

  /**
   * Modifies the font scale.
   *
   * @param matrices matrixStack
   * @param ci Callback info
   */
  @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;" +
  "renderLeftText(Lnet/minecraft/client/util/math/MatrixStack;)V"))
  public void renderFontScaleBefore(final MatrixStack matrices, final CallbackInfo ci) {
    if (!GeneralOptions.disableMod) {
      matrices.scale((float) GeneralOptions.fontScale, (float) GeneralOptions.fontScale, 1F);
    }
  }

  /**
   * Fixes the font scale.
   *
   * @param matrices matrixStack
   * @param ci CallbackInfo
   */
  @Inject(method = "render", at = @At(value = "FIELD",
  target = "Lnet/minecraft/client/option/GameOptions;debugTpsEnabled:Z"), cancellable = true)
  public void renderFontScaleRightAfter(final MatrixStack matrices, final CallbackInfo ci) {
    if (GeneralOptions.disableMod) {
      return;
    }
    matrices.pop();
    this.client.getProfiler().pop();
    ci.cancel();
  }

  /**
   * Renders the animation.
   *
   * @param matrices matrixStack
   * @param ci Callback info
   */
  @Inject(method = "render", at = @At("HEAD"))
  public void renderAnimation(final MatrixStack matrices, final CallbackInfo ci) {

    if (GeneralOptions.disableMod) {
      return;
    }
    if (!GeneralOptions.enableAnimations) {
      return;
    } // Only displays the animation if set to true

    final long time = Util.getMeasuringTimeMs();
    if (time - lastAnimationUpdate >= 10 && (xPos != 0 || closingAnimation)) {

      int i = ((START_X_POS / 2 + xPos) / 10) - 9;

      if (xPos != 0 && !closingAnimation) {
        xPos /= GeneralOptions.animationSpeed;
        xPos -= i;
      }

      if (i == 0) {
        i = 1;
      }

      if (closingAnimation) {

        xPos += i;
        xPos *= GeneralOptions.animationSpeed;

        if (xPos >= 300) {
          this.client.options.debugEnabled = false;
          closingAnimation = false;
        }

      }

      lastAnimationUpdate = time;
    }
  }

}
