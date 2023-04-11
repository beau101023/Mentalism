package me.beaubaer.mentalism.networking.S2C;

import me.beaubaer.mentalism.clientdata.SpellCastData;
import me.beaubaer.mentalism.networking.IMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.function.Supplier;

public class CanCastSpellsSyncS2CPacket implements IMessage
{
    private final int messageLength;
    private final Integer[] spellNums;

    public CanCastSpellsSyncS2CPacket(Integer[] spellNums)
    {
        messageLength = spellNums.length;
        this.spellNums = spellNums;
    }

    public CanCastSpellsSyncS2CPacket(FriendlyByteBuf buf)
    {
        messageLength = buf.readInt();

        spellNums = new Integer[messageLength];
        for(int i = 0; i < messageLength; i++)
        {
            spellNums[i] = buf.readInt();
        }
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeInt(messageLength);

        for(int i = 0; i<messageLength; i++)
        {
            buf.writeInt(spellNums[i]);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            SpellCastData.canCastNums = Arrays.asList(spellNums);
        });
        ctx.get().setPacketHandled(true);
    }
}
