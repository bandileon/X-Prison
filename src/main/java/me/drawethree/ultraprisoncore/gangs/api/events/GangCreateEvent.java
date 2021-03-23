package me.drawethree.ultraprisoncore.gangs.api.events;

import me.drawethree.ultraprisoncore.gangs.models.Gang;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GangCreateEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private boolean cancelled;
    private CommandSender creator;
    private Gang gang;

    public GangCreateEvent(CommandSender creator, Gang gang) {
        this.creator = creator;
        this.gang = gang;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}