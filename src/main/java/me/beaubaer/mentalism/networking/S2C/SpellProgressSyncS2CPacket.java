package me.beaubaer.mentalism.networking.S2C;

import me.beaubaer.mentalism.clientdata.SpellCastData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpellProgressSyncS2CPacket
{
    final float spellProgress;

    public SpellProgressSyncS2CPacket(float spellProgress)
    {
        this.spellProgress = spellProgress;
    }

    public SpellProgressSyncS2CPacket(FriendlyByteBuf buf)
    {
        spellProgress = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeFloat(spellProgress);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            SpellCastData.localSpellProgress = spellProgress;
        });
        ctx.get().setPacketHandled(true);
    }
}
