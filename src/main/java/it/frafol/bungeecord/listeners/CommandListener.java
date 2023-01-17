package it.frafol.bungeecord.listeners;

import it.frafol.bungeecord.CommandProtection;
import it.frafol.bungeecord.objects.PlayerCache;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class CommandListener implements Listener {

    public final CommandProtection PLUGIN;

    public CommandListener(CommandProtection plugin) {
        this.PLUGIN = plugin;
    }

    @EventHandler
    private void onCommand(@NotNull ChatEvent event) {

        if (!(event.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        if (!event.getMessage().startsWith("/")) {
            return;
        }

        if (player.hasPermission("commandprotection.bypass")) {
            return;
        }

        if (PlayerCache.getCommandExecuted().containsKey(player.getUniqueId()) && PlayerCache.getCommandExecuted().get(player.getUniqueId()) >= 10) {
            event.setCancelled(true);
            return;
        }

        if (PlayerCache.getCommandExecuted().containsKey(player.getUniqueId())) {

            PlayerCache.getCommandExecuted().put(player.getUniqueId(), Math.addExact(PlayerCache.getCommandExecuted().get(player.getUniqueId()), 1));

            PLUGIN.getProxy().getScheduler().schedule(PLUGIN, () ->
                    PlayerCache.getCommandExecuted().put(player.getUniqueId(), Math.subtractExact(PlayerCache.getCommandExecuted().get(player.getUniqueId()), 1)),
                    10, TimeUnit.SECONDS);

        }

        PlayerCache.getCommandExecuted().put(player.getUniqueId(), 1);

        PLUGIN.getProxy().getScheduler().schedule(PLUGIN, () ->
                PlayerCache.getCommandExecuted().put(player.getUniqueId(), Math.subtractExact(PlayerCache.getCommandExecuted().get(player.getUniqueId()), 1)),
                10, TimeUnit.SECONDS);

    }
}
