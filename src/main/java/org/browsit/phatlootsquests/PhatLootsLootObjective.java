package org.browsit.phatlootsquests;

import com.codisimus.plugins.phatloots.events.PlayerLootEvent;
import me.pikamug.quests.module.BukkitCustomObjective;
import me.pikamug.quests.player.Quester;
import me.pikamug.quests.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class PhatLootsLootObjective extends BukkitCustomObjective implements Listener {

    public PhatLootsLootObjective() {
        setName("PhatLoots Open Objective");
        setAuthor("Browsit, LLC");
        setItem("CHEST", (short)0);
        setShowCount(true);
        addStringPrompt("PL Open Obj", "Set a name for the objective", "Open loot");
        addStringPrompt("PL Open Names", "- Available PhatLoots -"
                + PhatLootsModule.getSuggestions(), "ANY");
        setCountPrompt("Set the number of times to open the loot");
        setDisplay("%PL Open Obj% from %PL Open Names%: %count%");
    }

    @Override
    public String getModuleName() {
        return PhatLootsModule.getModuleName();
    }

    @Override
    public Map.Entry<String, Short> getModuleItem() {
        return PhatLootsModule.getModuleItem();
    }

    @EventHandler
    public void onPlayerLoot(final PlayerLootEvent event) {
        if (PhatLootsModule.getQuests() == null) {
            return;
        }
        final Quester quester = PhatLootsModule.getQuests().getQuester(event.getLooter().getUniqueId());
        if (quester == null) {
            return;
        }
        for (final Quest q : quester.getCurrentQuests().keySet()) {
            final Player p = quester.getPlayer();
            final Map<String, Object> dataMap = getDataForPlayer(p.getUniqueId(), this, q);
            if (dataMap != null) {
                final String arenaNames = (String)dataMap.getOrDefault("PL Open Names", "ANY");
                if (arenaNames == null) {
                    return;
                }
                final String[] spl = arenaNames.split(",");
                for (final String str : spl) {
                    if (str.equals("ANY") || event.getPhatLoot().getName().equalsIgnoreCase(str)) {
                        incrementObjective(p.getUniqueId(), this, q, 1);
                        break;
                    }
                }
            }
        }
    }
}
