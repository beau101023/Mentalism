package me.beaubaer.mentalism.eventhandlers;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.capabilities.focus.FocusProvider;
import me.beaubaer.mentalism.capabilities.focus.IFocus;
import me.beaubaer.mentalism.capabilities.spellmanager.SpellManager;
import me.beaubaer.mentalism.capabilities.spellmanager.SpellManagerProvider;
import me.beaubaer.mentalism.capabilities.unlocks.UnlockState;
import me.beaubaer.mentalism.capabilities.unlocks.UnlockStateProvider;
import me.beaubaer.mentalism.registries.SpellRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

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
                e.addCapability(new ResourceLocation(Mentalism.MOD_ID, "focus"), new FocusProvider());
            }
            if(!p.getCapability(SpellManagerProvider.SPELL_MANAGER).isPresent())
            {
                e.addCapability(new ResourceLocation(Mentalism.MOD_ID, "spellmanager"), new SpellManagerProvider());
            }
            if(!p.getCapability(UnlockStateProvider.UNLOCK_STATE).isPresent())
            {
                e.addCapability(new ResourceLocation(Mentalism.MOD_ID, "unlockmanager"), new UnlockStateProvider());
            }
        }
    }

    @SubscribeEvent
    public static void playerCloned(PlayerEvent.Clone e)
    {
        if(e.isWasDeath())
        {
            // focus should be copied to new player
            e.getOriginal().getCapability(FocusProvider.FOCUS).ifPresent(oldStore ->
                    e.getPlayer().getCapability(FocusProvider.FOCUS).ifPresent(newStore ->
                            newStore.copyFrom(oldStore)));

            // unlock state should be copied to new player as well
            e.getOriginal().getCapability(UnlockStateProvider.UNLOCK_STATE).ifPresent(oldStore ->
                    e.getPlayer().getCapability(UnlockStateProvider.UNLOCK_STATE).ifPresent(newStore ->
                            newStore.copyFrom(oldStore)));
        }
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        event.register(IFocus.class);
        event.register(SpellManager.class);
        event.register(UnlockState.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e)
    {
        if(e.side != LogicalSide.SERVER)
            return;

        if(e.phase == TickEvent.Phase.START)
            return;

        if(e.type != TickEvent.Type.PLAYER)
            return;

        ServerPlayer p = (ServerPlayer) e.player;

        p.getCapability(FocusProvider.FOCUS).ifPresent(f ->
        {
            f.update();
            f.sync(p);

            // if we go over 1.0 focus power, unlock biden blast spell
            if(f.getFocusPower() > 1.0f)
            {
                p.getCapability(UnlockStateProvider.UNLOCK_STATE).ifPresent(um ->
                {
                    SpellRegistry.BIDEN_BLAST.ifPresent(s ->
                    {
                        if(!um.isUnlocked(s)) um.unlock(s);
                    });
                });
            }
        });

        p.getCapability(SpellManagerProvider.SPELL_MANAGER).ifPresent(sm ->
                sm.update(p));
    }
}