package me.beaubaer.mentalism.networking.C2S;

import me.beaubaer.mentalism.capabilities.focus.FocusProvider;
import me.beaubaer.mentalism.capabilities.focus.modifiers.Distraction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SoundDistractionC2SPacket
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
                if (!f.hasModifier(Distraction.SOUND_DISTRACTION))
                {
                    f.putModifier(Distraction.SOUND_DISTRACTION);
                }
                else
                {
                    f.getModifier(Distraction.SOUND_DISTRACTION, Distraction.class).ifPresent(sd ->
                    {
                        sd.setAmount(sd.getAmount() + 0.1f * volume);
                    });
                }
            });

        });
        ctx.get().setPacketHandled(true);
    }
}
