package me.cominixo.betterf3.mixin;

import com.google.common.base.Strings;
import com.mojang.blaze3d.systems.RenderSystem;
import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.MiscLeftModule;
import me.cominixo.betterf3.modules.MiscRightModule;
import me.cominixo.betterf3.utils.PositionEnum;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static me.cominixo.betterf3.utils.Utils.*;


@Mixin(DebugHud.class)
public abstract class DebugMixin {

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract List<String> getLeftText();

    @Shadow protected abstract List<String> getRightText();

    @Shadow @Final private TextRenderer textRenderer;


    public List<Text> getNewLeftText() {

        List<Text> list = new ArrayList<>();

        for (BaseModule module : BaseModule.modules) {
            if (!module.enabled) {
                continue;
            }
            if (module instanceof MiscLeftModule) {
              ((MiscLeftModule) module).update(getLeftText());
            } else {
                module.update(client);
            }

            list.addAll(module.getLinesFormatted(client.hasReducedDebugInfo()));
            if (GeneralOptions.spaceEveryModule) {
                list.add(new LiteralText(""));
            }
        }

        return list;

    }

    public List<Text> getNewRightText() {

        List<Text> list = new ArrayList<>();

        for (BaseModule module : BaseModule.modulesRight) {
            if (!module.enabled) {
                continue;
            }
            if (module instanceof MiscRightModule) {
                ((MiscRightModule) module).update(getRightText());
            } else {
                module.update(client);
            }

            list.addAll(module.getLinesFormatted(client.hasReducedDebugInfo()));
            if (GeneralOptions.spaceEveryModule) {
                list.add(new LiteralText(""));
            }
        }

        return list;

    }

    @Inject(method = "renderRightText", at = @At("HEAD"), cancellable = true)
    public void renderRightText(MatrixStack matrixStack, CallbackInfo ci) {

        if (GeneralOptions.disableMod) {
            return;
        }

        List<Text> list = getNewRightText();

        VertexConsumerProvider.Immediate immediate = getImmediate(PositionEnum.RIGHT, list, matrixStack);

        for (int i = 0; i < list.size(); i++) {

            if (!Strings.isNullOrEmpty(list.get(i).getString())) {
                int height = 9;
                int width = this.textRenderer.getWidth(list.get(i).getString());
                int windowWidth = (int)(this.client.getWindow().getScaledWidth() / GeneralOptions.fontScale) - 2 - width;
                if (GeneralOptions.enableAnimations) {
                    windowWidth += xPos;
                }
                int y = 2 + height * i;

                this.textRenderer.draw(list.get(i), windowWidth, y, 0xE0E0E0, GeneralOptions.shadowText, matrixStack.peek().getPositionMatrix(), immediate, false, 0, 15728880);



            }
        }

        immediate.draw();

        ci.cancel();

    }

    public VertexConsumerProvider.Immediate getImmediate(PositionEnum pos, List<Text> list, MatrixStack matrixStack) {


        float f = (float)(GeneralOptions.backgroundColor >> 24 & 255) / 255.0F;
        float g = (float)(GeneralOptions.backgroundColor >> 16 & 255) / 255.0F;
        float h = (float)(GeneralOptions.backgroundColor >> 8 & 255) / 255.0F;
        float k = (float)(GeneralOptions.backgroundColor & 255) / 255.0F;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        for (int i = 0; i < list.size(); i++) {
            int height = 9;
            int width = this.textRenderer.getWidth(list.get(i).getString());
            int y = 2 + height * i;

            int x1, x2, y1, y2, j;

            if (pos == PositionEnum.RIGHT) {
                int windowWidth = (int) (this.client.getWindow().getScaledWidth() / GeneralOptions.fontScale) - 2 - width;
                if (GeneralOptions.enableAnimations) {
                    windowWidth += xPos;
                }

                x1 = windowWidth - 1;
                x2 = windowWidth + width + 1;
                y1 = y - 1;
                y2 = y + height - 1;
            } else {
                int xPosLeft = 2;

                if (GeneralOptions.enableAnimations) {
                    xPosLeft -= xPos;
                }
                x1 = 1 + xPosLeft;
                x2 = width + 3 + xPosLeft;
                y1 = y - 1;
                y2 = y + height - 1;
            }

            Matrix4f matrix = matrixStack.peek().getPositionMatrix();

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


            bufferBuilder.vertex(matrix, (float)x1, (float)y2, 0.0F).color(g, h, k, f).next();
            bufferBuilder.vertex(matrix, (float)x2, (float)y2, 0.0F).color(g, h, k, f).next();
            bufferBuilder.vertex(matrix, (float)x2, (float)y1, 0.0F).color(g, h, k, f).next();
            bufferBuilder.vertex(matrix, (float)x1, (float)y1, 0.0F).color(g, h, k, f).next();

        }
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

        return VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());


    }


    @Inject(method = "renderLeftText", at = @At("HEAD"), cancellable = true)
    public void renderLeftText(MatrixStack matrixStack, CallbackInfo ci) {

        if (GeneralOptions.disableMod) {
            return;
        }
        List<Text> list = getNewLeftText();
        VertexConsumerProvider.Immediate immediate = getImmediate(PositionEnum.LEFT, list, matrixStack);

        for (int i = 0; i < list.size(); i++) {

            if (!Strings.isNullOrEmpty(list.get(i).getString())) {

                int height = 9;
                int y = 2 + height * i;
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

    @Inject(method = "render", at = @At("HEAD"))
    public void renderFontScaleBefore(MatrixStack matrices, CallbackInfo ci) {
        matrices.push();
        matrices.scale((float) GeneralOptions.fontScale, (float) GeneralOptions.fontScale, 1F);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void renderFontScaleAfter(MatrixStack matrices, CallbackInfo ci) {
        matrices.pop();
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void renderAnimation(MatrixStack matrices, CallbackInfo ci) {

        if (!GeneralOptions.enableAnimations) {
            return;
        }

        long time = Util.getMeasuringTimeMs();
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
                    this.client.options.debugEnabled = false;
                    closingAnimation = false;
                }

            }

            lastAnimationUpdate = time;
        }
    }

}
