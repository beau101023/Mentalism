package me.beaubaer.mentalism.networking;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.networking.C2S.*;
import me.beaubaer.mentalism.networking.S2C.*;
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

    net.messageBuilder(FocusKeySyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(FocusKeySyncC2SPacket::new)
            .encoder(FocusKeySyncC2SPacket::toBytes)
            .consumer(FocusKeySyncC2SPacket::handle)
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

    net.messageBuilder(FocusValueSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(FocusValueSyncS2CPacket::new)
            .encoder(FocusValueSyncS2CPacket::toBytes)
            .consumer(FocusValueSyncS2CPacket::handle)
            .add();

    net.messageBuilder(AvailableSpellsSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(AvailableSpellsSyncS2CPacket::new)
            .encoder(AvailableSpellsSyncS2CPacket::toBytes)
            .consumer(AvailableSpellsSyncS2CPacket::handle)
            .add();

    net.messageBuilder(CanCastSpellsSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(CanCastSpellsSyncS2CPacket::new)
            .encoder(CanCastSpellsSyncS2CPacket::toBytes)
            .consumer(CanCastSpellsSyncS2CPacket::handle)
            .add();

    net.messageBuilder(SpellProgressSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SpellProgressSyncS2CPacket::new)
            .encoder(SpellProgressSyncS2CPacket::toBytes)
            .consumer(SpellProgressSyncS2CPacket::handle)
            .add();

    net.messageBuilder(SelectedSpellSyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(SelectedSpellSyncC2SPacket::new)
            .encoder(SelectedSpellSyncC2SPacket::toBytes)
            .consumer(SelectedSpellSyncC2SPacket::handle)
            .add();

    net.messageBuilder(CastingStateSyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(CastingStateSyncC2SPacket::new)
            .encoder(CastingStateSyncC2SPacket::toBytes)
            .consumer(CastingStateSyncC2SPacket::handle)
            .add();

    net.messageBuilder(SoundDistractionC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(SoundDistractionC2SPacket::new)
            .encoder(SoundDistractionC2SPacket::toBytes)
            .consumer(SoundDistractionC2SPacket::handle)
            .add();

    net.messageBuilder(BellAntidistractionC2SPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(BellAntidistractionC2SPacket::new)
            .encoder(BellAntidistractionC2SPacket::toBytes)
            .consumer(BellAntidistractionC2SPacket::handle)
            .add();
}

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    /*private static <MSG> void buildMessage(Class<MSG> clazz, int id, NetworkDirection direction)
    {
        // TODO: Look at this again later

        // build message as in register()

        INSTANCE.messageBuilder(clazz, id, direction)
                .decoder(MSG::new)
                .encoder(clazz::toBytes)
                .consumer(clazz::handle)
                .add();
    }*/
}
