package it.frafol.luckpermsfix;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.nio.file.Path;

@Getter
@Plugin(
		id = "luckpermsvelocityfix",
		name = "LuckPermsVelocityFix",
		version = "@version@",
		dependencies = {@Dependency(id = "luckperms")},
		description = "TempFix for the LuckPerms Spam Exploit by disabling it",
		authors = { "frafol" })

public class LuckPermsFix {

	private final Logger logger;
	private final ProxyServer server;
	private final Path path;

	@Inject
	public LuckPermsFix(Logger logger, ProxyServer server, @DataDirectory Path path) {
		this.server = server;
		this.logger = logger;
		this.path = path;
	}

	@Inject
	public PluginContainer container;

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {

		server.getCommandManager().unregister("lpv");
		server.getCommandManager().unregister("luckpermsvelocity");

		logger.warn("Disabled LuckPerms commands to prevent an exploit.");

	}

}
