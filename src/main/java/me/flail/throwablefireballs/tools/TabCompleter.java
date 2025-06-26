package me.flail.throwablefireballs.tools;

import java.io.Serial;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class TabCompleter extends ArrayList<String> {

    private final Command command;

    /**
     * UID
     */
    @Serial
    private static final long serialVersionUID = 8202587163765471021L;

    public TabCompleter(Command command) {
        this.command = command;
    }

    public TabCompleter construct(String[] args) {
        String[] mainArgs = { "reload", "updateconfig", "give", "get", "info", "help" };

        try {
            if (command.getName().equalsIgnoreCase("throwablefireballs")) {
                String arg;
                switch (args.length) {
                    case 1:
                        arg = args[0].toLowerCase();
                        for (String s : mainArgs) {
                            if (s.startsWith(arg)) {
                                this.add(s);
                            }
                        }

                        break;
                    case 2:
                        arg = args[0].toLowerCase();
                        switch (arg) {
                            case "get":
                                for (int n = 1; n < 65; n++) {
                                    this.add(n + "");
                                }

                                break;
                            case "give":
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    this.add(p.getName());
                                }
                        }

                        break;
                    case 3:
                        arg = args[0].toLowerCase();
                        if (arg.contains("give")) {
                            for (int n = 1; n < 65; n++) {
                                this.add(n + "");
                            }
                        }
                }
            }
        } catch (Throwable ignored) {}

        return this;
    }
}
