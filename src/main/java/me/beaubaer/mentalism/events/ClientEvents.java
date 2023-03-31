package me.beaubaer.mentalism.events;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.gui.radialmenu.RadialMenu;
import me.beaubaer.mentalism.networking.C2S.CastingStateSyncC2SPacket;
import me.beaubaer.mentalism.networking.C2S.FocusKeySyncC2SPacket;
import me.beaubaer.mentalism.networking.MentalismMessages;
import me.beaubaer.mentalism.networking.C2S.SetCanFocusC2SPacket;
import me.beaubaer.mentalism.networking.C2S.SetFocusTimeC2SPacket;
import me.beaubaer.mentalism.keymappings.KeyMappings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Mentalism.MOD_ID, value = Dist.CLIENT)
public class ClientEvents
{
    // keep track of previous states so we only update the server when something changes
    private static boolean previousFKeyState = false;
    private static boolean previousCrouching = false;
    private static boolean previousMoving = false;

    // tick counter
    private static int tickCounter = 0;
    private static int numFPresses = 0;

    // doubleclicked state
    private static boolean doubleClicked = false;

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent e)
    {
        Minecraft mc = Minecraft.getInstance();

        // make sure we're actually loaded into a world before we do anything with ticks
        if (mc.player == null)
            return;

        updateMovementState(mc);

        handleFKeyStates();
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

                // check for a doubleclick
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

    private static void updateMovementState(Minecraft mc)
    {
        boolean moving = mc.options.keyLeft.isDown() || mc.options.keyRight.isDown() || mc.options.keyUp.isDown() || mc.options.keyDown.isDown();

        boolean crouching = mc.options.keyShift.isDown();

        // make sure to only update the server when changes are detected
        if (moving != previousMoving || crouching != previousCrouching)
        {
            if (!moving)
            {
                // here we are standing still, so we can focus
                MentalismMessages.sendToServer(new SetFocusTimeC2SPacket(1.0f));
                MentalismMessages.sendToServer(new SetCanFocusC2SPacket(true));
            }
            else if (crouching)
            {
                // here we are crouching and not moving, so we are standing still
                MentalismMessages.sendToServer(new SetFocusTimeC2SPacket(10.0f));
                MentalismMessages.sendToServer(new SetCanFocusC2SPacket(true));
            }
            else
            {
                // here we are not crouching or standing still, so we are walking normally or sprinting
                // player shouldn't be able to focus here
                MentalismMessages.sendToServer(new SetCanFocusC2SPacket(false));
            }
            previousMoving = moving;
            previousCrouching = crouching;
        }
    }
}
