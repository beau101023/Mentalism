package me.beaubaer.mentalism.spells.strategies.activations;

import me.beaubaer.mentalism.networking.MentalismMessages;
import me.beaubaer.mentalism.networking.S2C.PlaySourcelessSoundS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;

public class PlaySourcelessSound
{
    public static void activate(SoundEvent sound, float volume, float pitch, ServerPlayer p)
    {
        MentalismMessages.sendToPlayer(new PlaySourcelessSoundS2CPacket(sound, volume, pitch), p);
    }
}
