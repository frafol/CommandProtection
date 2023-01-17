package it.frafol.bungeecord;

import it.frafol.bungeecord.listeners.CommandListener;
import net.md_5.bungee.api.plugin.Plugin;

public class CommandProtection extends Plugin {

	@Override
	public void onEnable() {

		getProxy().getPluginManager().registerListener(this, new CommandListener(this));

	}

}