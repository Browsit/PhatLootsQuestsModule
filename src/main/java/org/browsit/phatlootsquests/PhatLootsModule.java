package org.browsit.phatlootsquests;

import com.codisimus.plugins.phatloots.PhatLoot;
import com.codisimus.plugins.phatloots.PhatLootsAPI;
import me.pikamug.quests.BukkitQuestsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PhatLootsModule extends JavaPlugin {
    private static final BukkitQuestsPlugin quests = (BukkitQuestsPlugin) Bukkit.getServer().getPluginManager().getPlugin("Quests");
    private static final String moduleName = "PhatLoots Quests Module";
    private static final Map.Entry<String, Short> moduleItem = new AbstractMap.SimpleEntry<>("CHEST", (short)0);

    public static BukkitQuestsPlugin getQuests() {
        return quests;
    }

    public static String getModuleName() {
        return moduleName;
    }

    public static Map.Entry<String, Short> getModuleItem() {
        return moduleItem;
    }

    @Override
    public void onEnable() {
        getLogger().severe(ChatColor.RED + "Move this jar to your " + File.separatorChar + "Quests" + File.separatorChar
                + "modules folder!");
        getServer().getPluginManager().disablePlugin(this);
        setEnabled(false);
    }

    @Override
    public void onDisable() {
    }

    public static String getSuggestions() {
        final List<PhatLoot> suggestionList = new LinkedList<>(PhatLootsAPI.getAllPhatLoots());
        suggestionList.sort(Comparator.comparing(PhatLoot::getName));
        final StringBuilder text = new StringBuilder("\n");
        for (int i = 0; i < suggestionList.size(); i++) {
            text.append(ChatColor.AQUA).append(suggestionList.get(i).getName());
            if (i < (suggestionList.size() - 1)) {
                text.append(ChatColor.GRAY).append(", ");
            }
        }
        return text.toString();
    }
}
