package me.beaubaer.mentalism.networking.C2S;

import me.beaubaer.mentalism.networking.IMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetCanFocusC2SPacket implements IMessage
{
    private final boolean canFocus;

    public SetCanFocusC2SPacket(boolean canFocus)
    {
        this.canFocus = canFocus;
    }

    public SetCanFocusC2SPacket(FriendlyByteBuf buf)
    {
        this.canFocus = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeBoolean(canFocus);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            ctx.get().getSender().getCapability(FocusProvider.FOCUS).ifPresent(f ->
            {
                f.setCanFocus(canFocus);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
