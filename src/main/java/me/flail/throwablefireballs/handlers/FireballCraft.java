package me.flail.throwablefireballs.handlers;

import me.flail.throwablefireballs.tools.Tools;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class FireballCraft extends Tools implements Listener {

	@EventHandler
	public void onCraft(CraftItemEvent event) {
		HumanEntity e = event.getWhoClicked();
		if (!e.hasPermission("fireballs.craft") && event.getRecipe().getResult().equals(new FireballItem().fireball())) {
			event.setResult(Result.DENY);
			e.closeInventory();
			e.sendMessage(chat(plugin.conf.getString("NoCraftPermission")));
		}

	}

}
