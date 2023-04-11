package me.beaubaer.mentalism.eventhandlers;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.capabilities.focus.Focus;
import me.beaubaer.mentalism.capabilities.focus.FocusProvider;
import me.beaubaer.mentalism.capabilities.focus.ModifierPriority;
import me.beaubaer.mentalism.capabilities.focus.modifiers.Distraction;
import me.beaubaer.mentalism.networking.C2S.BellAntidistractionC2SPacket;
import me.beaubaer.mentalism.networking.C2S.SetCanFocusC2SPacket;
import me.beaubaer.mentalism.networking.C2S.SetFocusTimeC2SPacket;
import me.beaubaer.mentalism.networking.C2S.SoundDistractionC2SPacket;
import me.beaubaer.mentalism.networking.MentalismMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Mentalism.MOD_ID, value = Dist.CLIENT)
public class SituationalMalusEvents
{
    private static boolean previousMoving = false;
    private static boolean previousCrouching = false;

    @SubscribeEvent
    public static void updateMovementMaluses(TickEvent.ClientTickEvent e)
    {
        // ClientTickEvent should only run on the client, but just in case
        if(e.side != LogicalSide.CLIENT)
            return;

        // this call actually has almost zero overhead so might as well just use it every tick
        Minecraft mc = Minecraft.getInstance();

        // make sure we're actually loaded into a world before we do anything with ticks
        if (mc.player == null)
            return;

        boolean moving = mc.options.keyLeft.isDown() || mc.options.keyRight.isDown() || mc.options.keyUp.isDown() || mc.options.keyDown.isDown();
        boolean crouching = mc.options.keyShift.isDown();

        // make sure to only update the server when changes are detected
        if (moving != previousMoving || crouching != previousCrouching)
        {
            boolean standingStill = !moving;
            boolean slowWalking = moving && crouching;

            if (standingStill)
            {
                MentalismMessages.sendToServer(new SetFocusTimeC2SPacket(Focus.defaultFocusTime));
                MentalismMessages.sendToServer(new SetCanFocusC2SPacket(true));
            }
            else if (slowWalking)
            {
                MentalismMessages.sendToServer(new SetFocusTimeC2SPacket(Focus.defaultSlowedFocusTime));
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

    private static final Distraction RIGHT_CLICK_DISTRACTION = new Distraction(ModifierPriority.AFTER_LEVEL, 0.3f, 5f, "mentalism.distraction.rightclick");

    // add a distraction when player right-clicks a block
    @SubscribeEvent
    public static void playerRightClickedBlock(PlayerInteractEvent.RightClickBlock e)
    {
        // FIXME: this accesses server code on the client
        //  should network this instead
        e.getPlayer().getCapability(FocusProvider.FOCUS).ifPresent(f ->
                f.putModifier(RIGHT_CLICK_DISTRACTION));
    }

    @SubscribeEvent
    public static void soundPlayed(PlaySoundEvent e)
    {
        if(e.getSound().getLocation().getPath().equals("bell.block.use") || e.getSound().getLocation().getPath().equals("bell.block.resonate"))
            MentalismMessages.sendToServer(new BellAntidistractionC2SPacket());

        if(!Mentalism.SOUND_DISTRACTIONS)
            return;

        if(e.getSound().getSource() == SoundSource.MUSIC || e.getSound().getSource() == SoundSource.MASTER)
            return;

        SoundInstance eSound = e.getSound();

        eSound.resolve(e.getEngine().soundManager);

        MentalismMessages.sendToServer(new SoundDistractionC2SPacket(eSound.getVolume()));
    }
}
