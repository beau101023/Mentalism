package me.beaubaer.mentalism.events;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.capabilities.Focus;
import me.beaubaer.mentalism.capabilities.FocusProvider;
import me.beaubaer.mentalism.capabilities.modifiers.AntiDistraction;
import me.beaubaer.mentalism.networking.MentalismMessages;
import me.beaubaer.mentalism.networking.OpenMeditationS2CPacket;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.VanillaGameEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = Mentalism.MOD_ID)
public class ServerEvents
{
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> e)
    {
        if(e.getObject() instanceof Player p)
        {
            if(!p.getCapability(FocusProvider.FOCUS).isPresent())
            {
                e.addCapability(new ResourceLocation(Mentalism.MOD_ID, "properties"), new FocusProvider());
            }
        }
    }

    @SubscribeEvent
    public static void playerCloned(PlayerEvent.Clone e)
    {
        if(e.isWasDeath())
        {
            e.getOriginal().getCapability(FocusProvider.FOCUS).ifPresent(oldStore ->
                    e.getPlayer().getCapability(FocusProvider.FOCUS).ifPresent(newStore ->
                            newStore.copyFrom(oldStore)));
        }
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        event.register(Focus.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e)
    {
        if(e.side != LogicalSide.SERVER)
            return;

        if(e.phase == TickEvent.Phase.END)
            return;

        ServerPlayer p = (ServerPlayer) e.player;

        p.getCapability(FocusProvider.FOCUS).ifPresent(f ->
        {
            f.updateFocus();

            if(f.getFocusPower() > 1.1f)
            {
                MentalismMessages.sendToPlayer(new OpenMeditationS2CPacket(), p);
            }

            p.sendMessage( new TextComponent("Focusing?: " + f.getFocusing()), Util.NIL_UUID);
            p.sendMessage( new TextComponent("Focus is " + f.getFocusPower() ), Util.NIL_UUID);
            p.sendMessage( new TextComponent("Number of focus modifiers is " + f.getModifiers().size()), Util.NIL_UUID);
        });
    }

    @SubscribeEvent
    public static void entitySoundPlayed(VanillaGameEvent e)
    {
        if(e.getLevel().isClientSide)
            return;

        if(e.getVanillaEvent() != GameEvent.RING_BELL)
            return;

        for(ServerPlayer p : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers())
        {
            if(e.getEventPosition().closerToCenterThan(p.position(), 32f))
            {
                p.getCapability(FocusProvider.FOCUS).ifPresent(f ->
                {
                    f.putModifier(new AntiDistraction(f, 4, 0.2f, 3.5f, AntiDistraction.BELL_ANTIDISTRACTION));
                });
            }
        }
    }
}