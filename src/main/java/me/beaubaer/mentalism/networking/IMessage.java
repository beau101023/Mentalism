package me.beaubaer.mentalism.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IMessage
{
    void toBytes(FriendlyByteBuf buf);
    void handle(Supplier<NetworkEvent.Context> ctx);
}
