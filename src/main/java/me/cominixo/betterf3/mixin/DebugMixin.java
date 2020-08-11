package me.cominixo.betterf3.mixin;

import com.google.common.base.Strings;
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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.client.gui.DrawableHelper.fill;


@Mixin(DebugHud.class)
public abstract class DebugMixin {

    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private TextRenderer fontRenderer;

    @Shadow protected abstract List<String> getLeftText();

    @Shadow protected abstract List<String> getRightText();

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

        for (int i = 0; i < list.size(); i++) {

            if (!Strings.isNullOrEmpty(list.get(i).getString())) {
                int height = 9;
                int width = this.fontRenderer.getWidth(list.get(i).getString());
                int windowWidth = this.client.getWindow().getScaledWidth() - 2 - width;
                int y = 2 + height * i;
                fill(matrixStack, windowWidth - 1, y - 1, windowWidth + width + 1, y + height - 1, GeneralOptions.backgroundColor);
                if (GeneralOptions.shadowText) {
                    this.fontRenderer.drawWithShadow(matrixStack, list.get(i), windowWidth, (float)y, 0xE0E0E0);
                } else {
                    this.fontRenderer.draw(matrixStack, list.get(i), windowWidth, (float)y, 0xE0E0E0);
                }

            }
        }

        ci.cancel();

    }


    @Inject(method = "renderLeftText", at = @At("HEAD"), cancellable = true)
    public void renderLeftText(MatrixStack matrixStack, CallbackInfo ci) {

        if (GeneralOptions.disableMod) {
            return;
        }

        List<Text> list = getNewLeftText();

        for (int i = 0; i < list.size(); i++) {

            if (!Strings.isNullOrEmpty(list.get(i).getString())) {
                int height = 9;
                int width = this.fontRenderer.getWidth(list.get(i).getString());
                int y = 2 + height * i;
                fill(matrixStack, 1, y - 1, width + 3, y + height - 1, GeneralOptions.backgroundColor);
                if (GeneralOptions.shadowText) {
                    this.fontRenderer.drawWithShadow(matrixStack, list.get(i), 2.0F, (float)y, 0xE0E0E0);
                } else {
                    this.fontRenderer.draw(matrixStack, list.get(i), 2.0F, (float)y, 0xE0E0E0);
                }

            }
        }

        ci.cancel();

    }

}
