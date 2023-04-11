package me.beaubaer.mentalism.networking.S2C;

import me.beaubaer.mentalism.clientdata.LingeringEffectData;
import me.beaubaer.mentalism.networking.IMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AddLingeringEffectS2CPacket implements IMessage
{
    private final String effectID;

    public AddLingeringEffectS2CPacket(String effectID)
    {
        this.effectID = effectID;
    }

    public AddLingeringEffectS2CPacket(FriendlyByteBuf buf)
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
            // add the effect to the client's list of active effects
            LingeringEffectData.addEffect(effectID);
        });
        ctx.get().setPacketHandled(true);
    }
}
