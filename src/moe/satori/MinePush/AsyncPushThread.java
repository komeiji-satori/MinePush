package moe.satori.MinePush;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.simple.parser.*;

import io.netty.util.internal.StringUtil;

import org.json.simple.JSONObject;
import redis.clients.jedis.Jedis;

public class AsyncPushThread {
	Plugin plugin;

	public void Run(Plugin plugin, HashMap<String, Object> ServerConfig) {
		this.plugin = plugin;

		Runnable rbq = new Runnable() {
			public void run() {
				System.out.println("MinePush Service Running..");
				Watch("message_queue", ServerConfig);
			}
		};
		Bukkit.getScheduler().runTaskAsynchronously(plugin, rbq);
	}

	public static void Watch(String queue, HashMap<String, Object> Config) {
		Jedis jedis = new Jedis(Config.get("host").toString(), (int) Config.get("port"));
		if (!StringUtil.isNullOrEmpty(Config.get("auth").toString())) {
			jedis.auth(Config.get("auth").toString());
		}
		while (true) {
			ArrayList<String> playerlist = new ArrayList<>();
			Bukkit.getOnlinePlayers().forEach((player) -> {
				playerlist.add(player.getName());
			});
			for (int i = 0; i < jedis.llen(queue); i++) {
				String jsons = jedis.lpop(queue);
				Object obj = null;
				try {
					obj = new JSONParser().parse(jsons);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				JSONObject pop_data = (JSONObject) obj;
				if (playerlist.contains(pop_data.get("username"))) {
					Player player = Bukkit.getPlayer(pop_data.get("username").toString());
					player.sendMessage(pop_data.get("msg").toString());
				} else {
					jedis.lpush(queue, jsons);
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
