package it.frafol.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.Player;
import it.frafol.CommandProtection;
import it.frafol.objects.PlayerCache;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class CommandListener {

    public final CommandProtection PLUGIN;

    public CommandListener(CommandProtection plugin) {
        this.PLUGIN = plugin;
    }

    @Subscribe(order = PostOrder.FIRST)
    private void onCommand(@NotNull CommandExecuteEvent event) {

        if (!(event.getCommandSource() instanceof Player)) {
            return;
        }

        final Player player = (Player) event.getCommandSource();

        if (player.hasPermission("commandprotection.bypass")) {
            return;
        }

        if (PlayerCache.getCommandExecuted().containsKey(player.getUniqueId()) && PlayerCache.getCommandExecuted().get(player.getUniqueId()) >= 10) {

            if (player.getProtocolVersion() == ProtocolVersion.MINECRAFT_1_19 ||
                    player.getProtocolVersion() == ProtocolVersion.MINECRAFT_1_19_1 ||
                    player.getProtocolVersion() == ProtocolVersion.MINECRAFT_1_19_3) {

                player.disconnect(Component.text("Sei stato cacciato per uno spam troppo eccessivo di comandi."));
                return;

            }

            event.setResult(CommandExecuteEvent.CommandResult.denied());
            player.disconnect(Component.text("Sei stato cacciato per uno spam troppo eccessivo di comandi."));
            return;

        }

        if (PlayerCache.getCommandExecuted().containsKey(player.getUniqueId())) {

            PlayerCache.getCommandExecuted().put(player.getUniqueId(), Math.addExact(PlayerCache.getCommandExecuted().get(player.getUniqueId()), 1));

            PLUGIN.getServer().getScheduler()
                    .buildTask(PLUGIN, scheduledTask ->
                            PlayerCache.getCommandExecuted().put(player.getUniqueId(), Math.subtractExact(PlayerCache.getCommandExecuted().get(player.getUniqueId()), 1)))
                    .delay(10, TimeUnit.SECONDS)
                    .schedule();

            return;

        }

        PlayerCache.getCommandExecuted().put(player.getUniqueId(), 1);

        PLUGIN.getServer().getScheduler()
                .buildTask(PLUGIN, scheduledTask ->
                        PlayerCache.getCommandExecuted().put(player.getUniqueId(), Math.subtractExact(PlayerCache.getCommandExecuted().get(player.getUniqueId()), 1)))
                .delay(10, TimeUnit.SECONDS)
                .schedule();

    }
}
