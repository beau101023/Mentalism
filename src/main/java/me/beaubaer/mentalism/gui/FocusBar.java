package me.beaubaer.mentalism.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.clientdata.FocusData;
import me.beaubaer.mentalism.eventhandlers.ClientEvents;
import me.beaubaer.mentalism.events.IValueUpdatedListener;
import me.beaubaer.mentalism.util.MentalMath;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import java.awt.*;

public class FocusBar implements IValueUpdatedListener
{
        private int barMaxLength;
        private float barThickness;
        private int x;
        private int y;

        private float foregroundBarLength = 0;

        public FocusBar(int barMaxLength, float barThickness, int x, int y)
        {
            this.barMaxLength = barMaxLength;
            this.barThickness = barThickness;
            this.x = x;
            this.y = y;

            ClientEvents.focusUpdatedListeners.add(this);
        }

        private static float previousTickFocus = 0f;
        private static float currentTickFocus = 0f;
        public void render(PoseStack poseStack, float partialTick)
        {
            Matrix4f pose = poseStack.last().pose().copy();

            // interpolation to smooth out the focus bar, see https://forums.minecraftforge.net/topic/58377-1112-guiscreen-partialticks/
            float smoothFocus = previousTickFocus + (currentTickFocus-previousTickFocus)*partialTick;

            // bar should look like multiple different colored bars stacked on top of each other
            // this is so we can visualize focus values above 1.0
            foregroundBarLength = Mth.map(smoothFocus, 0, 1, 0, barMaxLength)%barMaxLength;

            int foregroundBarNumber = (int) Math.floor(smoothFocus);

            // only change hue number for bar numbers above 5
            int foregroundHueNumber = Math.max(foregroundBarNumber-5, 0);
            // foreground saturation number shouldn't go above 1.0f
            int foregroundSaturationNumber = Math.min(foregroundBarNumber, 5);

            // each bar should be a different color
            Color foregroundColor = Color.getHSBColor(0.483f-foregroundHueNumber*0.1f, 0.2f*foregroundSaturationNumber, 1f);
            int ARGBforegroundColor = FastColor.ARGB32.color(255, foregroundColor.getRed(), foregroundColor.getGreen(), foregroundColor.getBlue());


            // only render the background bar if the foreground bar is looping
            if(foregroundBarNumber > 0)
            {
                int backgroundBarNumber = foregroundBarNumber - 1;
                int backgroundHueNumber = Math.max(backgroundBarNumber-5, 0);
                int backgroundSaturationNumber = Math.min(backgroundBarNumber, 5);

                Color backgroundColor = Color.getHSBColor(0.483f-backgroundHueNumber*0.1f, 0.2f*backgroundSaturationNumber, 1f);
                int ARGBbackgroundColor = FastColor.ARGB32.color(255, backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue());
                // background bar
                drawBar(pose, ARGBbackgroundColor, (barMaxLength-1), x, y);
            }

            // foreground bar, must draw after background bar so it's on top
            drawBar(pose, ARGBforegroundColor, (int)foregroundBarLength, x, y);
        }

    private void drawBar(Matrix4f pose, int color, int barLength, int x, int y)
    {
        GraphicsUtil.line(pose, barThickness, x, y, x+barLength, y, color);
    }

    @Override
    public void onValueUpdated(float focus)
    {
        previousTickFocus = currentTickFocus;
        currentTickFocus = Math.max(focus-0.001f, 0);
    }
}
