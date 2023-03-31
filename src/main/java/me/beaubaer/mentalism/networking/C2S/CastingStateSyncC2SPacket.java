package me.beaubaer.mentalism.networking.C2S;

import me.beaubaer.mentalism.capabilities.focus.FocusProvider;
import me.beaubaer.mentalism.capabilities.spellmanager.SpellManagerProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CastingStateSyncC2SPacket
{
    private final boolean isCasting;

    public CastingStateSyncC2SPacket(boolean isCasting)
    {
        this.isCasting = isCasting;
    }

    public CastingStateSyncC2SPacket(FriendlyByteBuf buf)
    {
        this.isCasting = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeBoolean(isCasting);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            ctx.get().getSender().getCapability(SpellManagerProvider.SPELL_MANAGER).ifPresent(sm ->
            {
                sm.tryingToCast = isCasting;
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
