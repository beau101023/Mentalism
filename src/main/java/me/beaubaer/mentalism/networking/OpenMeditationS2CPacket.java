package me.beaubaer.mentalism.networking;

import me.beaubaer.mentalism.screens.MeditationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenMeditationS2CPacket
{

    public OpenMeditationS2CPacket()
    {

    }

    public OpenMeditationS2CPacket(FriendlyByteBuf buf)
    {

    }

    public void toBytes(FriendlyByteBuf buf)
    {

    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            Minecraft mc = Minecraft.getInstance();

            Component c = new TranslatableComponent("title.mentalism.menu.void");
            MeditationScreen ms = new MeditationScreen(c);
            mc.setScreen(ms);
        });
        ctx.get().setPacketHandled(true);
    }
}
