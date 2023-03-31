package me.beaubaer.mentalism.networking.S2C;

import me.beaubaer.mentalism.clientdata.FocusData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FocusValueSyncS2CPacket
{
    private float focusVal;

    public FocusValueSyncS2CPacket(float focusVal)
    {
        this.focusVal = focusVal;
    }

    public FocusValueSyncS2CPacket(FriendlyByteBuf buf)
    {
        focusVal = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeFloat(focusVal);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            FocusData.localFocus = focusVal;
        });
        ctx.get().setPacketHandled(true);
    }
}
