package me.beaubaer.mentalism.networking.S2C;

import me.beaubaer.mentalism.clientdata.LingeringEffectData;
import me.beaubaer.mentalism.networking.IMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RemoveLingeringEffectS2CPacket implements IMessage
{
    private final String effectID;

    public RemoveLingeringEffectS2CPacket(String effectID)
    {
        this.effectID = effectID;
    }

    public RemoveLingeringEffectS2CPacket(FriendlyByteBuf buf)
    {
        this.effectID = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeUtf(effectID);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            // remove the effect from the client's list of active effects
            LingeringEffectData.removeEffect(effectID);
        });
        ctx.get().setPacketHandled(true);
    }
}
