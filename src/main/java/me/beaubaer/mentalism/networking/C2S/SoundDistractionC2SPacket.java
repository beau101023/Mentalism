package me.beaubaer.mentalism.networking.C2S;

import me.beaubaer.mentalism.capabilities.focus.FocusProvider;
import me.beaubaer.mentalism.capabilities.focus.modifiers.Distraction;
import me.beaubaer.mentalism.networking.IMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class SoundDistractionC2SPacket implements IMessage
{
    private final float volume;

    public SoundDistractionC2SPacket(float volume)
    {
        this.volume = volume;
    }

    public SoundDistractionC2SPacket(FriendlyByteBuf buf)
    {
        volume = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeFloat(volume);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            ServerPlayer p = ctx.get().getSender();

            assert p != null;
            p.getCapability(FocusProvider.FOCUS).ifPresent(f ->
            {
                // If the player already has a sound distraction, update it. Otherwise, create a new one.
                Optional<Distraction> soundDistraction = f.getModifier(Distraction.SOUND_DISTRACTION);
                if(soundDistraction.isPresent())
                {
                    Distraction d = soundDistraction.get();
                    d.setMaxAmount(d.getMaxAmount() + volume);
                    d.reset();
                }
                else
                {
                    Distraction customSoundDistraction = Distraction.SOUND_DISTRACTION.copy();
                    customSoundDistraction.setAmount(volume);
                    f.addModifier(customSoundDistraction);
                }
            });

        });
        ctx.get().setPacketHandled(true);
    }
}
