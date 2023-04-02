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

    // when called every frame, smoothly moves value towards target
    public static float smoothToTarget(float value, float target)
    {
        if(value == target)
            return value;

        return value + (target - value)*0.1f;

        /*if(target - value >= 0)
        {
            return value + (float) Math.log((target - value)*0.1f + 1.0f);
        }
        else if(target - value < 0)
        {
            return value - (float) Math.log(-(target - value)*0.1f + 1.0f);
        }*/
        //value = Math.min(value, 1.0f);
        //value = Math.max(value, 0.0f);
    }
}
