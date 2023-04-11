package me.beaubaer.mentalism.networking.C2S;

import me.beaubaer.mentalism.capabilities.focus.FocusProvider;
import me.beaubaer.mentalism.capabilities.focus.modifiers.AntiDistraction;
import me.beaubaer.mentalism.networking.IMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BellAntidistractionC2SPacket implements IMessage
{
    public BellAntidistractionC2SPacket()
    {
    }

    public BellAntidistractionC2SPacket(FriendlyByteBuf buf)
    {
    }

    public void toBytes(FriendlyByteBuf buf)
    {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            ctx.get().getSender().getCapability(FocusProvider.FOCUS).ifPresent(f ->
            {
                f.putModifier(AntiDistraction.BELL_ANTIDISTRACTION);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
