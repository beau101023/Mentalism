package me.beaubaer.mentalism.networking.S2C;

import me.beaubaer.mentalism.networking.IMessage;
import me.beaubaer.mentalism.networking.clienthandlers.HandlePlaySourcelessSound;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlaySourcelessSoundS2CPacket implements IMessage
{
    SoundEvent sound;
    float volume;
    float pitch;

    public PlaySourcelessSoundS2CPacket(SoundEvent sound, float volume, float pitch)
    {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public PlaySourcelessSoundS2CPacket(FriendlyByteBuf buf)
    {
        sound = new SoundEvent(new ResourceLocation(buf.readUtf()));
        volume = buf.readFloat();
        pitch = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeUtf(sound.getLocation().toString());
        buf.writeFloat(volume);
        buf.writeFloat(pitch);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            HandlePlaySourcelessSound.handle(sound, volume, pitch);
        });
        ctx.get().setPacketHandled(true);
    }
}
