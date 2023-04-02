package me.beaubaer.mentalism.clientdata;

import me.beaubaer.mentalism.events.IValueUpdatedListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class FocusData
{
    public static float localFocus = 0f;
    public static void setLocalFocus(float focus)
    {
        localFocus = focus;
    }
}