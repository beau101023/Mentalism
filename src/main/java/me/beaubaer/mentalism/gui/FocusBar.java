package me.beaubaer.mentalism.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.clientdata.FocusData;
import me.beaubaer.mentalism.util.MentalMath;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import java.awt.*;

public class FocusBar
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
        }
        public void render(PoseStack poseStack)
        {
            Matrix4f pose = poseStack.last().pose().copy();

            // bar should look like multiple different colored bars stacked on top of each other
            // this is so we can visualize focus values above 1.0
            float targetForegroundBarLength = Mth.map(FocusData.localFocus, 0, 1, 0, barMaxLength)
                    %barMaxLength;
            foregroundBarLength = MentalMath.smoothToTarget(foregroundBarLength, targetForegroundBarLength);

            Mentalism.LOGGER.debug(String.valueOf(FocusData.localFocus));
            Mentalism.LOGGER.debug(String.valueOf(targetForegroundBarLength));

            int foregroundBarNumber = (int) Math.floor(FocusData.localFocus);

            // each bar should be a different color
            // ie. the 0 to 1 focus bar should be red, the 1 to 2 focus bar should be orange, etc.
            Color foregroundColor = Color.getHSBColor(foregroundBarNumber * 0.1f, 1.0f, 1.0f);
            int ARGBforegroundColor = FastColor.ARGB32.color(255, foregroundColor.getRed(), foregroundColor.getGreen(), foregroundColor.getBlue());

            // foreground bar
            GraphicsUtil.line(pose, barThickness, x, y, x+(int)foregroundBarLength, y, ARGBforegroundColor);

            // only render the background bar if the foreground bar is looping
            if(foregroundBarNumber > 0)
            {
                int backgroundBarNumber = foregroundBarNumber - 1;
                Color backgroundColor = Color.getHSBColor(backgroundBarNumber * 0.1f, 1.0f, 1.0f);
                int ARGBbackgroundColor = FastColor.ARGB32.color(255, backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue());
                // background bar
                GraphicsUtil.line(pose, barThickness, x, y, x+barMaxLength, y, ARGBbackgroundColor);
            }
        }
}
