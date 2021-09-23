package me.cominixo.betterf3.mixin;

import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.config.gui.ModConfigScreen;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.cominixo.betterf3.utils.Utils.*;

@Mixin(KeyboardHandler.class)
public class KeyboardMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "handleDebugKeys", at = @At("HEAD"))
    public void processF3(int key, CallbackInfoReturnable<Boolean> cir) {
        if (key == 77) {
            minecraft.setScreen(new ModConfigScreen(null));
        }
    }

    @Inject(method="keyPress", at=@At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "net/minecraft/client/Options.renderDebug : Z"), cancellable = true)
    public void onDebugActivate(long window, int key, int scancode, int i, int j, CallbackInfo ci) {

        if (GeneralOptions.enableAnimations) {
            if (this.minecraft.options.renderDebug) {
                closingAnimation = true;
                ci.cancel();
            } else {
                closingAnimation = false;
                xPos = START_X_POS;
            }
        }


    }

}
