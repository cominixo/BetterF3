package me.cominixo.betterf3.mixin;

import me.cominixo.betterf3.config.gui.ModConfigScreen;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "processF3", at = @At("HEAD"))
    public void processF3(int key, CallbackInfoReturnable<Boolean> cir) {
        if (key == 77) {
            client.openScreen(new ModConfigScreen());
        }
    }
}
