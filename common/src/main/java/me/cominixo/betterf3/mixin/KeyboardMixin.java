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

import static me.cominixo.betterf3.utils.Utils.START_X_POS;
import static me.cominixo.betterf3.utils.Utils.closingAnimation;
import static me.cominixo.betterf3.utils.Utils.xPos;

/**
 * Modifies the debug keys (f3 / f3 + m).
 */
@Mixin(KeyboardHandler.class)
public class KeyboardMixin {
    @Shadow @Final private Minecraft minecraft;

    /**
     * Adds the config menu by pressing f3 + m.
     *
     * @param key key pressed with f3
     * @param cir Callback info
     */
    @Inject(method = "handleDebugKeys", at = @At("HEAD"))
    public void processF3(final int key, final CallbackInfoReturnable<Boolean> cir) {
        if (key == 77) { // Key m
            this.minecraft.setScreen(new ModConfigScreen(null));
        }
    }

    /**
     * Plays the animation on f3 keypress.
     *
     * @param window window
     * @param key key
     * @param scancode scancode
     * @param i i
     * @param j j
     * @param ci Callback info
     */
    @Inject(method = "keyPress", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "net/minecraft/client" +
            "/Options.renderDebug : Z"), cancellable = true)
    public void onDebugActivate(final long window, final int key, final int scancode, final int i, final int j, final CallbackInfo ci) {

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
