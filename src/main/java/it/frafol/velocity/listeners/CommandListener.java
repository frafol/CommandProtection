package it.frafol.velocity.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.Player;
import it.frafol.velocity.CommandProtection;
import it.frafol.velocity.objects.PlayerCache;
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

            if (player.getProtocolVersion().getProtocol() >= ProtocolVersion.MINECRAFT_1_19.getProtocol()) {

                player.disconnect(Component.text(""));
                return;

            }

            event.setResult(CommandExecuteEvent.CommandResult.denied());
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
