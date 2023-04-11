package me.beaubaer.mentalism.networking.C2S;

import me.beaubaer.mentalism.capabilities.spellmanager.SpellManagerProvider;
import me.beaubaer.mentalism.networking.IMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SelectedSpellSyncC2SPacket implements IMessage
{
    private final int spellNum;

    public SelectedSpellSyncC2SPacket(int spellNum)
    {
        this.spellNum = spellNum;
    }

    public SelectedSpellSyncC2SPacket(FriendlyByteBuf buf)
    {
        spellNum = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeInt(spellNum);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            ctx.get().getSender().getCapability(SpellManagerProvider.SPELL_MANAGER).ifPresent(sm ->
            {
                sm.updateSelectedSpell(spellNum);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
