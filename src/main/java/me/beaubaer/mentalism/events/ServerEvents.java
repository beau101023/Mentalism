package me.beaubaer.mentalism.events;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.capabilities.focus.FocusProvider;
import me.beaubaer.mentalism.capabilities.focus.IFocus;
import me.beaubaer.mentalism.capabilities.focus.modifiers.AntiDistraction;
import me.beaubaer.mentalism.capabilities.focus.modifiers.Distraction;
import me.beaubaer.mentalism.capabilities.spellmanager.SpellManager;
import me.beaubaer.mentalism.capabilities.spellmanager.SpellManagerProvider;
import me.beaubaer.mentalism.capabilities.unlocks.UnlockState;
import me.beaubaer.mentalism.capabilities.unlocks.UnlockStateProvider;
import me.beaubaer.mentalism.registries.SpellRegistry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
            e.getOriginal().getCapability(FocusProvider.FOCUS).ifPresent(oldStore ->
                    e.getPlayer().getCapability(FocusProvider.FOCUS).ifPresent(newStore ->
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

        if(e.phase == TickEvent.Phase.END)
            return;

        ServerPlayer p = (ServerPlayer) e.player;

        p.getCapability(FocusProvider.FOCUS).ifPresent(f ->
        {
            f.update();
            f.sync(p);

            // see number of modifiers
            p.sendMessage(new TextComponent("Number of modifiers: " + f.getModifiers().size() ), p.getUUID());

            // see sound distraction amount
            if(f.hasModifier(Distraction.SOUND_DISTRACTION))
                p.sendMessage(new TextComponent("Sound distraction amount: " + f.getModifier(Distraction.SOUND_DISTRACTION, Distraction.class).getAmount() ), p.getUUID());

            // if we go over 1.0 focus power, unlock shootArrow spell
            if(f.getFocusPower() > 1.0f)
            {
                p.getCapability(UnlockStateProvider.UNLOCK_STATE).ifPresent(um ->
                {
                    if(!um.isUnlocked(SpellRegistry.BIDEN_BLAST.get()))
                    {
                        um.unlock(SpellRegistry.BIDEN_BLAST.get());
                    }
                });
            }
        });

        p.getCapability(SpellManagerProvider.SPELL_MANAGER).ifPresent(sm ->
                sm.update(p));
    }



    public static final float soundDistractionThreshold = 0.1f;
    @SubscribeEvent
    public static void entitySoundPlayed(PlaySoundAtEntityEvent e)
    {
        // if e.getEntity() isn't a ServerPlayer, return
        if(!(e.getEntity() instanceof ServerPlayer))
            return;

        ServerPlayer p = (ServerPlayer) e.getEntity();

        p.sendMessage(new TextComponent("Sound played: " + e.getSound().getLocation() ), p.getUUID());

        // if the sound is a villager bell sound, add an AntiDistraction modifier to the player's focus
        if(e.getSound().getLocation().compareTo(SoundEvents.BELL_BLOCK.getLocation()) == 0)
        {
            // make sure we don't add duplicate modifiers
            p.getCapability(FocusProvider.FOCUS).ifPresent(f -> {
                if(!f.hasModifier(AntiDistraction.BELL_ANTIDISTRACTION))
                {
                    f.putModifier(new AntiDistraction(f, 0, 0.1f, 10f, AntiDistraction.BELL_ANTIDISTRACTION));
                }
                else
                {
                    f.getModifier(AntiDistraction.BELL_ANTIDISTRACTION, AntiDistraction.class).setDuration(10f);
                }
            });
        }

        // otherwise, add a Distraction modifier
        else
        {
            if (e.getVolume() > soundDistractionThreshold)
            {
                p.getCapability(FocusProvider.FOCUS).ifPresent(f -> {
                    if(!f.hasModifier(Distraction.SOUND_DISTRACTION))
                    {
                        f.putModifier(new Distraction(f, 0, 0.1f, 10f, Distraction.SOUND_DISTRACTION));
                    }
                    else
                    {
                        f.getModifier(Distraction.SOUND_DISTRACTION, Distraction.class).setDuration(10f);
                    }
                });
            }
        }
    }

    // interrupt focus when player right clicks a block
    @SubscribeEvent
    public static void playerRightClickedBlock(PlayerInteractEvent.RightClickBlock e)
    {
        e.getPlayer().getCapability(FocusProvider.FOCUS).ifPresent(f ->
                f.setFocusPressed(false));
    }
}