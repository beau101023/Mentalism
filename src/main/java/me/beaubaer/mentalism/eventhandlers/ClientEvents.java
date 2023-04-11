package me.beaubaer.mentalism.eventhandlers;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.clientdata.FocusData;
import me.beaubaer.mentalism.clientdata.LingeringEffectData;
import me.beaubaer.mentalism.events.IValueUpdatedListener;
import me.beaubaer.mentalism.gui.RadialMenu;
import me.beaubaer.mentalism.networking.C2S.*;
import me.beaubaer.mentalism.networking.MentalismMessages;
import me.beaubaer.mentalism.keymappings.KeyMappings;
import me.beaubaer.mentalism.spells.strategies.lingeringeffects.AirWalk;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Mentalism.MOD_ID, value = Dist.CLIENT)
public class ClientEvents
{
    // keep track of previous states so we only update the server when something changes
    private static boolean previousFKeyState = false;

    // tick counter
    private static int tickCounter = 0;
    private static int numFPresses = 0;

    // doubleclicked state
    private static boolean doubleClicked = false;


    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent e)
    {
        Minecraft mc = Minecraft.getInstance();

        // gotta remember to only run tick events once
        if(e.phase != TickEvent.Phase.END)
            return;

        // make sure we're actually loaded into a world before we do anything with ticks
        if (mc.player == null)
            return;

        handleLingeringEffects(mc.player);

        checkFocusUpdated();

        handleFKeyStates();
    }

    private static void handleLingeringEffects(LocalPlayer p)
    {
        if(LingeringEffectData.effectIsActive(AirWalk.name))
        {
            AirWalk.clientSideUpdate(p);
        }
    }

    public static List<IValueUpdatedListener> focusUpdatedListeners = new ArrayList<>();
    private static void checkFocusUpdated()
    {
        // FIXME: must be called every tick, name may be misleading
        notifyFocusUpdatedListeners();
    }

    private static void notifyFocusUpdatedListeners() {
        for(IValueUpdatedListener listener : focusUpdatedListeners)
        {
            listener.onValueUpdated(FocusData.localFocus);
        }
    }

    private static void handleFKeyStates()
    {
        updateDoubleClickTimer();

        boolean currentFKeyState = KeyMappings.FOCUS_KEY.isDown();

        // if the F key state has changed
        if (currentFKeyState != previousFKeyState)
        {
            // if the F key state changes to true
            if(currentFKeyState)
            {
                // increment the number of presses
                numFPresses++;

                // check for a double click
                if (numFPresses == 2)
                {
                    // if we've doubleclicked, reset the tick counter and number of presses
                    tickCounter = 0;
                    numFPresses = 0;
                    doubleClicked = true;

                    // player is trying to cast, update the server
                    MentalismMessages.sendToServer(new CastingStateSyncC2SPacket(true));
                }
            }
            else // if the f key state changes to false
            {
                // if the player has doubleclicked, reset the doubleclick state and update the server
                if (doubleClicked)
                {
                    doubleClicked = false;
                    MentalismMessages.sendToServer(new CastingStateSyncC2SPacket(false));
                }
            }

            if(!doubleClicked)
            {
                RadialMenu.SPELL_MENU.setActive(currentFKeyState);
            }
            else
            {
                RadialMenu.SPELL_MENU.setActive(false);
            }

            MentalismMessages.sendToServer(new FocusKeySyncC2SPacket(currentFKeyState));

            previousFKeyState = currentFKeyState;
        }
    }

    private static void updateDoubleClickTimer()
    {
        // if the number of presses > 0, start the tick counter
        if (numFPresses > 0)
        {
            tickCounter++;

            // reset the presses after a half second
            if (tickCounter == 10)
            {
                numFPresses = 0;
                tickCounter = 0;
            }
        }
    }
}
