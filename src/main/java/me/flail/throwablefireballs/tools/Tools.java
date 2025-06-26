package me.flail.throwablefireballs.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.flail.throwablefireballs.ThrowableFireballs;
import me.flail.throwablefireballs.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class Tools {

    private static final char COLOR_CHAR = ChatColor.COLOR_CHAR;

    protected ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

    String prefix = Config.options().get("Prefix").toString();
    String version = plugin.getDescription().getVersion();

    public String chat(String s) {
        String result = "";

        try {
            result = ChatColor.translateAlternateColorCodes('&', s.replace("%prefix%", prefix).replaceAll("%version%", version));
        } catch (Throwable e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "ERROR with chat formatting! Send the above error to the plugin's author.");
        }
        return translateHex("&#", "", result);
    }

    public String translateHex(String startTag, String endTag, String message) {
        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + (4 * 8));
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x" + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1) + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3) + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5));
        }
        return matcher.appendTail(buffer).toString();
    }

    public void console(String msg) {
        plugin.console.sendMessage(chat("&7[ThrowableFireballs] " + msg));
    }

    /**
     * Knocks all entities within the target radius backwards as naturally as
     * possible.
     *
     * @param center
     *                   the target entity at the center of the fireball hit.
     * @param radius
     *                   at which to check for entities.
     * @return false if there are no entities nearby, true otherwise.
     */
    public boolean setKnockback(Entity center, double radius) {
        Location target = center.getLocation();

        int maxHeight = plugin.conf.getInt("MaxJumpHeight");

        List<Entity> nearbyEntities = center.getNearbyEntities(radius, radius, radius);

        if ((nearbyEntities == null) || nearbyEntities.isEmpty()) {
            return false;
        }

        List<LivingEntity> validEntities = new ArrayList<>();

        for (Entity entity : nearbyEntities) {
            if (entity.isValid() && (entity instanceof LivingEntity)) {
                validEntities.add((LivingEntity) entity);
            }
        }

        for (LivingEntity entity : validEntities) {
            if ((entity instanceof Player)) {
                Player player = (Player) entity;
                if (player.isConversing() || player.isGliding()) {
                    continue;
                }
            }

            /*
			Location ePos = entity.getLocation();
			double x = Math.abs(ePos.getX() - target.getX());
			double y = ePos.getY() - target.getY();
			double z = Math.abs(ePos.getZ() - target.getZ());
			*/

            double distance = (maxHeight - entity.getLocation().distance(target));

            Vector variantVel = entity.getLocation().getDirection().multiply(-1);

            variantVel = variantVel.setY(distance / (Math.PI * 1.67));

            entity.setVelocity(variantVel);
        }

        return true;
    }

    protected String removeChars(String message, String[] chars) {
        String modified = message;
        for (String c : chars) {
            modified = modified.replace(c, "");
        }

        return modified;
    }

    protected ItemStack addTag(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(plugin.namespace, PersistentDataType.STRING, "fireballxyz");
        item.setItemMeta(meta);

        return item;
    }

    protected boolean isFireball(ItemStack item) {
        if (!item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(plugin.namespace, PersistentDataType.STRING);
    }
}
