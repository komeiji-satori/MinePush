package moe.satori.MinePush;

import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;
import moe.satori.MinePush.AsyncPushThread;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		saveDefaultConfig();
		HashMap<String, Object> ServerConfig = new HashMap<>();
		ServerConfig.put("host", this.getConfig().getString("host"));
		ServerConfig.put("port", this.getConfig().getInt("port"));
		ServerConfig.put("auth", this.getConfig().getString("auth"));
		AsyncPushThread APT = new AsyncPushThread();
		APT.Run(this, ServerConfig);
	}

	@Override
	public void onDisable() {

	}
}
