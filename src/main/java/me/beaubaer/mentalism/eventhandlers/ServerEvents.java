package me.beaubaer.mentalism.eventhandlers;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.capabilities.lingeringeffects.LingeringEffectManager;
import me.beaubaer.mentalism.capabilities.lingeringeffects.LingeringEffectManagerProvider;
import me.beaubaer.mentalism.capabilities.spellmanager.SpellManager;
import me.beaubaer.mentalism.capabilities.spellmanager.SpellManagerProvider;
import me.beaubaer.mentalism.capabilities.unlocks.ProgressionState;
import me.beaubaer.mentalism.capabilities.unlocks.ProgressionStateProvider;
import me.beaubaer.mentalism.spells.SpellRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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
        if(e.getObject() instanceof ServerPlayer p)
        {
            if(!p.getCapability(FocusProvider.FOCUS).isPresent())
            {
                e.addCapability(new ResourceLocation(Mentalism.MOD_ID, "focus"), new FocusProvider());
            }
            if(!p.getCapability(SpellManagerProvider.SPELL_MANAGER).isPresent())
            {
                e.addCapability(new ResourceLocation(Mentalism.MOD_ID, "spellmanager"), new SpellManagerProvider(p));
            }
            if(!p.getCapability(ProgressionStateProvider.PROGRESSION_STATE).isPresent())
            {
                e.addCapability(new ResourceLocation(Mentalism.MOD_ID, "progressionstate"), new ProgressionStateProvider());
            }
            if(!p.getCapability(LingeringEffectManagerProvider.LINGERING_EFFECT_MANAGER).isPresent())
            {
                e.addCapability(new ResourceLocation(Mentalism.MOD_ID, "lingeringeffectmanager"), new LingeringEffectManagerProvider(p));

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
            e.getOriginal().getCapability(ProgressionStateProvider.PROGRESSION_STATE).ifPresent(oldStore ->
                    e.getPlayer().getCapability(ProgressionStateProvider.PROGRESSION_STATE).ifPresent(newStore ->
                            newStore.copyFrom(oldStore)));
        }
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        event.register(IFocus.class);
        event.register(SpellManager.class);
        event.register(ProgressionState.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e)
    {
        if(e.side != LogicalSide.SERVER || e.phase != TickEvent.Phase.END || e.type != TickEvent.Type.PLAYER)
            return;

        ServerPlayer p = (ServerPlayer) e.player;

        p.getCapability(FocusProvider.FOCUS).ifPresent(f ->
        {
            f.update();
            f.sync(p);

            // if we go over 1.0 focus power, unlock biden blast spell
            if(f.getFocusPower() > 1.0f)
            {
                p.getCapability(ProgressionStateProvider.PROGRESSION_STATE).ifPresent(ps ->
                {
                    SpellRegistry.BIDEN_BLAST.ifPresent(s ->
                    {
                        if(!ps.isUnlocked(s)) ps.unlock(s);
                    });
                });
            }
        });

        p.getCapability(SpellManagerProvider.SPELL_MANAGER).ifPresent(SpellManager::update);

        p.getCapability(LingeringEffectManagerProvider.LINGERING_EFFECT_MANAGER).ifPresent(LingeringEffectManager::update);
    }
}