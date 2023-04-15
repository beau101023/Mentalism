package me.beaubaer.mentalism.networking;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.networking.C2S.*;
import me.beaubaer.mentalism.networking.S2C.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class MentalismMessages
{
    private static SimpleChannel INSTANCE;

    private static int packetID = 0;

    private static int id()
    {
        return packetID++;
    }

    public static void register()
    {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Mentalism.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        buildMessage(FocusKeySyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER);
        buildMessage(SetFocusTimeC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER);
        buildMessage(SetCanFocusC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER);
        buildMessage(FocusValueSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT);
        buildMessage(AvailableSpellsSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT);
        buildMessage(CanCastSpellsSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT);
        buildMessage(SpellProgressSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT);
        buildMessage(SelectedSpellSyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER);
        buildMessage(CastingStateSyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER);
        buildMessage(SoundDistractionC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER);
        buildMessage(BellAntidistractionC2SPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT);
        buildMessage(AddLingeringEffectS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT);
        buildMessage(RemoveLingeringEffectS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <MSG> void sendToServer(MSG message)
    {
        INSTANCE.sendToServer(message);
    }
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player)
    {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    private static <MSG extends IMessage> void buildMessage(Class<MSG> clazz, int id, NetworkDirection direction)
    {
        INSTANCE.messageBuilder(clazz, id, direction)
                .decoder(buf ->
                {
                    try
                    {
                        return clazz.getConstructor(FriendlyByteBuf.class).newInstance(buf);
                    }
                    catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                           IllegalAccessException e)
                    {
                        throw new RuntimeException(e);
                    }
                })
                .encoder((msg, buf) ->
                {
                    try
                    {
                        clazz.getMethod("toBytes", FriendlyByteBuf.class).invoke(msg, buf);
                    }
                    catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
                    {
                        throw new RuntimeException(e);
                    }
                })
                .consumer((msg, ctx) ->
                {
                    try
                    {
                        clazz.getMethod("handle", Supplier.class).invoke(msg, ctx);
                    }
                    catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
                    {
                        throw new RuntimeException(e);
                    }
                }).add();
    }
}
