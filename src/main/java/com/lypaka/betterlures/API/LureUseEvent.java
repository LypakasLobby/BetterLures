package com.lypaka.betterlures.API;

import com.lypaka.betterlures.Lures.Lure;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Called when a player activates a lure
 * Cancelling the event will prevent the lure from activating
 */
@Cancelable
public class LureUseEvent extends Event {

    private final ServerPlayerEntity player;
    private final Lure lure;
    private int timer; // not final for PixelBoosters support, boosting the time that the lure stays active

    public LureUseEvent (ServerPlayerEntity player, Lure lure, int timer) {

        this.player = player;
        this.lure = lure;
        this.timer = timer;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public Lure getLure() {

        return this.lure;

    }

    public int getTimer() {

        return this.timer;

    }

    public void setTimer (int timer) {

        this.timer = timer;

    }

}
