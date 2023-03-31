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
}
