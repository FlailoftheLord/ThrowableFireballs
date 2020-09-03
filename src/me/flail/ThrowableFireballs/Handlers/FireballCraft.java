package me.flail.ThrowableFireballs.Handlers;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import me.flail.ThrowableFireballs.Tools.Tools;

public class FireballCraft extends Tools implements Listener {

	@EventHandler
	public void onCraft(CraftItemEvent event) {
		HumanEntity e = event.getWhoClicked();
		if (!((Player) e).hasPermission("fireballs.craft")) {
			event.setResult(Result.DENY);
			e.closeInventory();
			e.sendMessage(chat(plugin.getConfig().getString("NoCraftPermission")));
		}

	}

}
