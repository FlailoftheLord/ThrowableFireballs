package me.flail.throwablefireballs.handlers;

import me.flail.throwablefireballs.ThrowableFireballs;
import me.flail.throwablefireballs.tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class FireballRecipe {

    private final ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final Tools tools = new Tools();

    public NamespacedKey getNamespace() {
        return new NamespacedKey(plugin, "ThrowableFireball");
    }

    public Recipe fireballRecipe() {
        FileConfiguration config = plugin.conf;

        int yield = config.getInt("CraftingRecipe.AmountGiven");

        String line1 = config.getString("CraftingRecipe.Pattern.1");
        String line2 = config.getString("CraftingRecipe.Pattern.2");
        String line3 = config.getString("CraftingRecipe.Pattern.3");

        String allLines = line1 + line2 + line3;

        String a = config.getString("CraftingRecipe.Materials.A");
        String b = config.getString("CraftingRecipe.Materials.B");
        String c = config.getString("CraftingRecipe.Materials.C");
        String d = config.getString("CraftingRecipe.Materials.D");
        String e = config.getString("CraftingRecipe.Materials.E");
        String f = config.getString("CraftingRecipe.Materials.F");
        String g = config.getString("CraftingRecipe.Materials.G");
        String h = config.getString("CraftingRecipe.Materials.H");
        String i = config.getString("CraftingRecipe.Materials.I");

        ItemStack fireball = new FireballItem().fireball();

        fireball.setAmount(yield);

        ShapedRecipe fbRecipe = new ShapedRecipe(getNamespace(), fireball);

        assert line1 != null;
        assert line2 != null;
        assert line3 != null;
        fbRecipe.shape(line1, line2, line3);

        if (allLines.contains("A")) {
            if (Material.matchMaterial(a) != null) {
                fbRecipe.setIngredient('A', Material.getMaterial(a));
            } else {
                console.sendMessage(tools.chat("%prefix% &4Invalid Material name for Recipe Material 'A'"));
            }
        }

        if (allLines.contains("B")) {
            if (Material.matchMaterial(b) != null) {
                fbRecipe.setIngredient('B', Material.getMaterial(b));
            } else {
                console.sendMessage(tools.chat("%prefix% &4Invalid Material name for Recipe Material 'B'"));
            }
        }

        if (allLines.contains("C")) {
            if (Material.matchMaterial(c) != null) {
                fbRecipe.setIngredient('C', Material.getMaterial(c));
            } else {
                console.sendMessage(tools.chat("%prefix% &4Invalid Material name for Recipe Material 'C'"));
            }
        }

        if (allLines.contains("D")) {
            if (Material.matchMaterial(d) != null) {
                fbRecipe.setIngredient('D', Material.getMaterial(d));
            } else {
                console.sendMessage(tools.chat("%prefix% &4Invalid Material name for Recipe Material 'D'"));
            }
        }

        if (allLines.contains("E")) {
            if (Material.matchMaterial(e) != null) {
                fbRecipe.setIngredient('E', Material.getMaterial(e));
            } else {
                console.sendMessage(tools.chat("%prefix% &4Invalid Material name for Recipe Material 'E'"));
            }
        }

        if (allLines.contains("F")) {
            if (Material.matchMaterial(f) != null) {
                fbRecipe.setIngredient('F', Material.getMaterial(f));
            } else {
                console.sendMessage(tools.chat("%prefix% &4Invalid Material name for Recipe Material 'F'"));
            }
        }

        if (allLines.contains("G")) {
            if (Material.matchMaterial(g) != null) {
                fbRecipe.setIngredient('G', Material.getMaterial(g));
            } else {
                console.sendMessage(tools.chat("%prefix% &4Invalid Material name for Recipe Material 'G'"));
            }
        }

        if (allLines.contains("H")) {
            if (Material.matchMaterial(h) != null) {
                fbRecipe.setIngredient('H', Material.getMaterial(h));
            } else {
                console.sendMessage(tools.chat("%prefix% &4Invalid Material name for Recipe Material 'H'"));
            }
        }

        if (allLines.contains("I")) {
            if (Material.matchMaterial(i) != null) {
                fbRecipe.setIngredient('I', Material.getMaterial(i));
            } else {
                console.sendMessage(tools.chat("%prefix% &4Invalid Material name for Recipe Material 'I'"));
            }
        }

        return fbRecipe;
    }
}
