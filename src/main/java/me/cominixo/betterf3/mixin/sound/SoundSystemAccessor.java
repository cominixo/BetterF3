package me.cominixo.betterf3.mixin.sound;

import net.minecraft.client.sound.SoundEngine;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SoundSystem.class)
public interface SoundSystemAccessor {
    @Accessor
    SoundEngine getSoundEngine();

}