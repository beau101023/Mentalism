package me.beaubaer.mentalism.networking.C2S;

import me.beaubaer.mentalism.capabilities.focus.FocusProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FocusKeySyncC2SPacket
{
    private final boolean focusing;

    public FocusKeySyncC2SPacket(boolean focusing)
    {
        this.focusing = focusing;
    }

    public FocusKeySyncC2SPacket(FriendlyByteBuf buf)
    {
        focusing = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeBoolean(focusing);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() ->
        {
            ServerPlayer player = context.getSender();
            assert player != null;
            player.getCapability(FocusProvider.FOCUS).ifPresent(f ->
                    f.setFocusPressed(focusing));
        });
        context.setPacketHandled(true);
    }
}
