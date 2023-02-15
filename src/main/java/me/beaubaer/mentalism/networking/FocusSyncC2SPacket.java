package me.beaubaer.mentalism.networking;

import me.beaubaer.mentalism.capabilities.FocusProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FocusSyncC2SPacket
{
    private final boolean focusing;

    public FocusSyncC2SPacket(boolean focusing)
    {
        this.focusing = focusing;
    }

    public FocusSyncC2SPacket(FriendlyByteBuf buf)
    {
        focusing = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeBoolean(focusing);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() ->
        {
            ServerPlayer player = context.getSender();
            player.getCapability(FocusProvider.FOCUS).ifPresent(f ->
                    f.setFocusing(focusing));
        });
        return true;
    }
}
