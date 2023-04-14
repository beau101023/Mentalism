package me.beaubaer.mentalism.spells.strategies.activations;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlayLocalSound
{
    public static void activate(SoundEvent sound, float volume, float pitch)
    {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forLocalAmbience(sound, volume, pitch));
    }
}
