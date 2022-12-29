package it.frafol;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import it.frafol.listeners.CommandListener;
import lombok.Getter;

@Getter
@Plugin(
		id = "commandprotection",
		name = "CommandProtection",
		version = "1.0",
		description = "Add Cooldown to proxy commands to prevent proxy exploits.",
		authors = { "frafol" })

public class CommandProtection {

	private final ProxyServer server;

	@Inject
	public CommandProtection(ProxyServer server) {
		this.server = server;
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {

		server.getEventManager().register(this, new CommandListener(this));

	}

}