package me.beaubaer.mentalism.spells.strategies.lingeringeffects;

import me.beaubaer.mentalism.capabilities.lingeringeffects.LingeringEffectManager;
import me.beaubaer.mentalism.clientdata.LingeringEffectData;
import me.beaubaer.mentalism.networking.MentalismMessages;
import me.beaubaer.mentalism.networking.S2C.AddLingeringEffectS2CPacket;
import me.beaubaer.mentalism.networking.S2C.RemoveLingeringEffectS2CPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;

public class AirWalk implements ILingeringEffect
{
    ServerPlayer p;
    private LingeringEffectManager manager;
    private final double distanceAllowed;

    private double distanceMoved = 0;
    private double prevMoveDist = 0;

    public static final String name = "airwalk";

    public AirWalk(double distanceAllowed)
    {
        this.distanceAllowed = distanceAllowed;
    }

    @Override
    public void update()
    {
        // get the distance moved since the last tick
        distanceMoved += p.moveDist - prevMoveDist;
        prevMoveDist = p.moveDist;

        if(distanceMoved >= distanceAllowed || p.isInWater() || p.isInLava())
        {
            manager.remove(this);
        }
    }
    public static void clientSideUpdate(LocalPlayer p)
    {
        if(Minecraft.getInstance().options.keyJump.isDown())
        {
            LingeringEffectData.removeEffect(name);
            return;
        }

        p.setDeltaMovement(p.getDeltaMovement().x, 0, p.getDeltaMovement().z);
        p.setOnGround(true);
    }

    @Override
    public void intialize(ServerPlayer p, LingeringEffectManager manager)
    {
        this.p = p;
        this.manager = manager;
        prevMoveDist = p.moveDist;

        MentalismMessages.sendToPlayer(new AddLingeringEffectS2CPacket(this.getName()), p);
    }

    @Override
    public void onRemoved()
    {
        MentalismMessages.sendToPlayer(new RemoveLingeringEffectS2CPacket(this.getName()), p);
    }

    @Override
    public String getName()
    {
        return name;
    }
}
