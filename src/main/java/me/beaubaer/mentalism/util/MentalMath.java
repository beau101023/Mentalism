package me.beaubaer.mentalism.util;

public class MentalMath
{

    /**
     * @param seconds Desired time in seconds to reach 1f
     * @return A float which, when used to increment a value once per Minecraft tick, will cause that value to increase by 1f in time 'seconds'.
     */
    public static float secondsToTickIncrements(float seconds)
    {
        return 1f/(20f*seconds);
    }


    /**
     * This method is used to smooth the motion of a target value which is updated 20 times a second (standard minecraft tickrate)
     * to achieve a smooth motion when rendered 60 times a second (standard minecraft render framerate).
     * Should only be used in render methods, and called once per render.
     * @param value Render value
     * @param target Target value
     * @return Smoothed render value
     */
    public static float smoothTickedToRender(float value, float target)
    {
        if(value == target)
            return value;

        // the displacement between value and target is multipled by 1/3 because the render framerate is 3x the tickrate
        return value + (target - value)*0.333f;
    }
}
