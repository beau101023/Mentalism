package me.beaubaer.mentalism.events;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.networking.FocusSyncC2SPacket;
import me.beaubaer.mentalism.networking.MentalismMessages;
import me.beaubaer.mentalism.networking.SetCanFocusC2SPacket;
import me.beaubaer.mentalism.networking.SetFocusTimeC2SPacket;
import me.beaubaer.mentalism.util.KeyMappings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = Mentalism.MOD_ID, value = Dist.CLIENT)
public class ClientEvents
{
    private static boolean previousFKeyState = false;
    private static boolean previousCrouching = false;

    private static boolean previousMoving = false;

    @SubscribeEvent
    public static void checkKeyOnTick(TickEvent.ClientTickEvent e)
    {
        Minecraft mc = Minecraft.getInstance();

        // make sure we're actually loaded into a world before we do anything with ticks
        if(mc.player == null)
            return;

        boolean currentFKeyState = KeyMappings.FOCUS_KEY.isDown();

        boolean moving = mc.options.keyLeft.isDown() || mc.options.keyRight.isDown() || mc.options.keyUp.isDown() || mc.options.keyDown.isDown();

        boolean crouching = mc.options.keyShift.isDown();

        // make sure to only update the server when changes are detected
        if (moving != previousMoving || crouching != previousCrouching)
        {
            if (!moving)
            {
                MentalismMessages.sendToServer(new SetFocusTimeC2SPacket(1.0f));
                MentalismMessages.sendToServer(new SetCanFocusC2SPacket(true));
            }
            else if (crouching)
            {
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

        if (currentFKeyState != previousFKeyState)
        {
            MentalismMessages.sendToServer(new FocusSyncC2SPacket(currentFKeyState));
            previousFKeyState = currentFKeyState;
        }
    }
}
