package me.cominixo.betterf3.mixin;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.MiscLeftModule;
import me.cominixo.betterf3.modules.MiscRightModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.objectweb.asm.Opcodes;
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
import static net.minecraft.client.gui.DrawableHelper.fill;

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
            } else {
                module.update(this.client);
            }

            list.addAll(module.linesFormatted(this.client.hasReducedDebugInfo()));
            if (GeneralOptions.spaceEveryModule) {
                list.add(new LiteralText(""));
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
            } else {
                module.update(this.client);
            }

            list.addAll(module.linesFormatted(this.client.hasReducedDebugInfo()));
            if (GeneralOptions.spaceEveryModule) {
                list.add(new LiteralText(""));
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

                fill(matrixStack, windowWidth - 1, y - 1, windowWidth + width + 1, y + height - 1, GeneralOptions.backgroundColor);

                if (GeneralOptions.shadowText) {
                    this.textRenderer.drawWithShadow(matrixStack, list.get(i), windowWidth, (float) y, 0xE0E0E0);
                } else {
                    this.textRenderer.draw(matrixStack, list.get(i), windowWidth, (float) y, 0xE0E0E0);
                }
            }
        }
        ci.cancel();
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

        for (int i = 0; i < list.size(); i++) {

            if (!Strings.isNullOrEmpty(list.get(i).getString())) {

                final int height = 9;
                final int width = this.textRenderer.getWidth(list.get(i).getString());
                final int y = 2 + height * i;
                int xPosLeft = 2;

                if (GeneralOptions.enableAnimations) {
                    xPosLeft -= xPos;
                }

                fill(matrixStack, 1 + xPosLeft, y - 1, width + 3 + xPosLeft, y + height - 1, GeneralOptions.backgroundColor);

                if (GeneralOptions.shadowText) {
                    this.textRenderer.drawWithShadow(matrixStack, list.get(i), xPosLeft, (float) y, 0xE0E0E0);
                } else {
                    this.textRenderer.draw(matrixStack, list.get(i), xPosLeft, (float) y, 0xE0E0E0);
                }
            }
        }

        ci.cancel();

    }

    /**
     * Modifies the font scale.
     *
     * @param matrices matrixStack
     * @param ci Callback info
     */
    @Inject(method = "render", at = @At("HEAD"))
    public void renderFontScaleBefore(final MatrixStack matrices, final CallbackInfo ci) {
        matrices.push();
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
    @Inject(method = "render", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/option/GameOptions;debugTpsEnabled:Z"))
    public void renderFontScaleRightAfter(final MatrixStack matrices, final CallbackInfo ci) {
        if (!GeneralOptions.disableMod) {
            matrices.scale(1F, 1F, 1F);
        }
    }

    /**
     * Pops the MatrixStack for render font.
     *
     * @param matrices matrixStack
     * @param ci Callback info
     */
    @Inject(method = "render", at = @At("TAIL"))
    public void renderFontScaleAfter(final MatrixStack matrices, final CallbackInfo ci) {
        matrices.pop();
    }

    /**
     * Renders the animation.
     *
     * @param matrices matrixStack
     * @param ci Callback info
     */
    @Inject(method = "render", at = @At("HEAD"))
    public void renderAnimation(final MatrixStack matrices, final CallbackInfo ci) {

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
