package me.beaubaer.mentalism.networking;

import me.beaubaer.mentalism.Mentalism;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class MentalismMessages
{
    private static SimpleChannel INSTANCE;

    private static int packetID = 0;

    private static int id()
    {
        return packetID++;
    }

    public static void register() {
    SimpleChannel net = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Mentalism.MOD_ID, "messages"))
            .networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();

    INSTANCE = net;

    net.messageBuilder(FocusSyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(FocusSyncC2SPacket::new)
            .encoder(FocusSyncC2SPacket::toBytes)
            .consumer(FocusSyncC2SPacket::handle)
            .add();

    net.messageBuilder(OpenMeditationS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(OpenMeditationS2CPacket::new)
            .encoder(OpenMeditationS2CPacket::toBytes)
            .consumer(OpenMeditationS2CPacket::handle)
            .add();

    net.messageBuilder(SetFocusTimeC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(SetFocusTimeC2SPacket::new)
            .encoder(SetFocusTimeC2SPacket::toBytes)
            .consumer(SetFocusTimeC2SPacket::handle)
            .add();

    net.messageBuilder(SetCanFocusC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(SetCanFocusC2SPacket::new)
            .encoder(SetCanFocusC2SPacket::toBytes)
            .consumer(SetCanFocusC2SPacket::handle)
            .add();
}

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
