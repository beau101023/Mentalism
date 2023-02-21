package me.beaubaer.mentalism.networking;

import me.beaubaer.mentalism.capabilities.FocusProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetFocusTimeC2SPacket
{

    private final float focusTime;
    public SetFocusTimeC2SPacket(float focusTime) { this.focusTime = focusTime; }

    public SetFocusTimeC2SPacket(FriendlyByteBuf buf)
    {
        focusTime = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeFloat(focusTime);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            ServerPlayer sp = ctx.get().getSender();

            sp.getCapability(FocusProvider.FOCUS).ifPresent(f ->
            {
                f.setFocusTime(focusTime);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
