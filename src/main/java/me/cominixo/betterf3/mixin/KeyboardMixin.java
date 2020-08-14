package me.cominixo.betterf3.mixin;

import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.config.gui.ModConfigScreen;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.cominixo.betterf3.utils.Utils.*;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "processF3", at = @At("HEAD"))
    public void processF3(int key, CallbackInfoReturnable<Boolean> cir) {
        if (key == 77) {
            client.openScreen(new ModConfigScreen());
        }
    }

    @Inject(method="onKey", at=@At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "net/minecraft/client/options/GameOptions.debugEnabled : Z"), cancellable = true)
    public void onDebugActivate(long window, int key, int scancode, int i, int j, CallbackInfo ci) {

        if (GeneralOptions.enableAnimations) {
            if (this.client.options.debugEnabled) {
                closingAnimation = true;
                ci.cancel();
            } else {
                closingAnimation = false;
                xPos = START_X_POS;
            }
        }


    }

}
