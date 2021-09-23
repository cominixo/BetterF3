package me.cominixo.betterf3.mixin;

import com.google.common.base.Strings;
import com.mojang.blaze3d.vertex.PoseStack;
import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.MiscLeftModule;
import me.cominixo.betterf3.modules.MiscRightModule;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static me.cominixo.betterf3.utils.Utils.*;
import static net.minecraft.client.gui.GuiComponent.fill;

/**
 * The Debug Screen Overlay
 */
@Mixin(DebugScreenOverlay.class)
public abstract class DebugMixin {

    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private Font font;

    /**
     * Gets the information on the left side of the screen
     *
     * @return the game information
     */
    @Shadow protected abstract List<String> getGameInformation();

    /**
     * Gets the information on the right side of the screen
     *
     * @return the system information
     */
    @Shadow protected abstract List<String> getSystemInformation();


    /**
     * Sets up modules on the left side of the screen
     *
     * @return the left side modules
     */
    public List<Component> getNewLeftText() {

        List<Component> list = new ArrayList<>();

        for (BaseModule module : BaseModule.modules) {
            if (!module.enabled) {
                continue;
            }
            if (module instanceof MiscLeftModule) {
                ((MiscLeftModule) module).update(getGameInformation());
            } else {
                module.update(minecraft);
            }

            list.addAll(module.getLinesFormatted(minecraft.showOnlyReducedInfo()));
            if (GeneralOptions.spaceEveryModule) {
                list.add(new TextComponent(""));
            }
        }

        return list;

    }

    /**
     * Sets up modules on the right side of the screen
     *
     * @return the right side modules
     */
    public List<Component> getNewRightText() {

        List<Component> list = new ArrayList<>();

        for (BaseModule module : BaseModule.modulesRight) {
            if (!module.enabled) {
                continue;
            }
            if (module instanceof MiscRightModule) {
                ((MiscRightModule) module).update(getSystemInformation());
            } else {
                module.update(minecraft);
            }

            list.addAll(module.getLinesFormatted(minecraft.showOnlyReducedInfo()));
            if (GeneralOptions.spaceEveryModule) {
                list.add(new TextComponent(""));
            }
        }

        return list;

    }

    /**
     * Renders the text on the right side of the screen
     */
    @Inject(method = "drawSystemInformation", at = @At("HEAD"), cancellable = true)
    public void renderRightText(PoseStack matrixStack, CallbackInfo ci) {

        if (GeneralOptions.disableMod) {
            return;
        }

        List<Component> list = getNewRightText();

        for (int i = 0; i < list.size(); i++) {

            if (!Strings.isNullOrEmpty(list.get(i).getString())) {
                int height = 9;
                int width = this.font.width(list.get(i).getString());
                int windowWidth = (int)(this.minecraft.getWindow().getGuiScaledWidth() / GeneralOptions.fontScale) - 2 - width;
                if (GeneralOptions.enableAnimations) {
                    windowWidth += xPos;
                }
                int y = 2 + height * i;

                fill(matrixStack, windowWidth - 1, y - 1, windowWidth + width + 1, y + height - 1, GeneralOptions.backgroundColor);

                if (GeneralOptions.shadowText) {
                    this.font.drawShadow(matrixStack, list.get(i), windowWidth, (float)y, 0xE0E0E0);
                } else {
                    this.font.draw(matrixStack, list.get(i), windowWidth, (float)y, 0xE0E0E0);
                }


            }
        }

        ci.cancel();

    }


    /**
     * Renders the text on the left side of the screen
     */
    @Inject(method = "drawGameInformation", at = @At("HEAD"), cancellable = true)
    public void renderLeftText(PoseStack matrixStack, CallbackInfo ci) {

        if (GeneralOptions.disableMod) {
            return;
        }

        List<Component> list = getNewLeftText();

        for (int i = 0; i < list.size(); i++) {

            if (!Strings.isNullOrEmpty(list.get(i).getString())) {

                int height = 9;
                int width = this.font.width(list.get(i).getString());
                int y = 2 + height * i;
                int xPosLeft = 2;

                if (GeneralOptions.enableAnimations) {
                    xPosLeft -= xPos;
                }

                fill(matrixStack, 1 + xPosLeft, y - 1, width + 3 + xPosLeft, y + height - 1, GeneralOptions.backgroundColor);

                if (GeneralOptions.shadowText) {
                    this.font.drawShadow(matrixStack, list.get(i), xPosLeft, (float)y, 0xE0E0E0);
                } else {
                    this.font.draw(matrixStack, list.get(i), xPosLeft, (float) y, 0xE0E0E0);
                }


            }
        }

        ci.cancel();

    }

    /**
     * Modifies the font scale
     */
    @Inject(method = "render", at = @At("HEAD"))
    public void renderFontScaleBefore(PoseStack matrices, CallbackInfo ci) {
        matrices.pushPose();
        matrices.scale((float) GeneralOptions.fontScale, (float) GeneralOptions.fontScale, 1F);
    }

    /**
     * Pops the MatrixStack for render font
     */
    @Inject(method = "render", at = @At("TAIL"))
    public void renderFontScaleAfter(PoseStack matrices, CallbackInfo ci) {
        matrices.popPose();
    }

    /**
     * Renders the animation
     */
    @Inject(method = "render", at = @At("HEAD"))
    public void renderAnimation(PoseStack matrices, CallbackInfo ci) {

        if (!GeneralOptions.enableAnimations) {
            return;
        } // Only displays the animation if set to true

        long time = Util.getMillis();
        if (time - lastAnimationUpdate >= 10 && (xPos != 0 || closingAnimation)) {


            int i = ((START_X_POS/2 + xPos) / 10)-9;

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
                    this.minecraft.options.renderDebug = false;
                    closingAnimation = false;
                }

            }

            lastAnimationUpdate = time;
        }
    }

}
