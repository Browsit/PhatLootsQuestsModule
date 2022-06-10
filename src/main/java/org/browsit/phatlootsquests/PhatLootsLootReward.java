package org.browsit.phatlootsquests;

import com.codisimus.plugins.phatloots.PhatLoot;
import com.codisimus.plugins.phatloots.PhatLootsAPI;
import com.codisimus.plugins.phatloots.loot.CommandLoot;
import com.codisimus.plugins.phatloots.loot.LootBundle;
import me.blackvein.quests.CustomReward;
import me.blackvein.quests.util.InventoryUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class PhatLootsLootReward extends CustomReward {

    public PhatLootsLootReward() {
        setName("PhatLoots Loot Reward");
        setAuthor("Browsit, LLC");
        setItem("CHEST", (short)0);
        setDisplay("%PL Reward Name% Loot");
        addStringPrompt("PL Reward Name", "- Available PhatLoots -"
                + PhatLootsModule.getSuggestions(), "ANY");
    }

    @Override
    public String getModuleName() {
        return PhatLootsModule.getModuleName();
    }

    @Override
    public Map.Entry<String, Short> getModuleItem() {
        return PhatLootsModule.getModuleItem();
    }

    @Override
    public void giveReward(final Player player, final Map<String, Object> data) {
        if (data != null) {
            final String lootName = (String)data.getOrDefault("PL Reward Name", "ANY");

            if (lootName.equalsIgnoreCase("ANY")) {
                final Optional<PhatLoot> randomLoot = getRandom(PhatLootsAPI.getAllPhatLoots());
                if (randomLoot.isPresent()) {
                    final LootBundle lootBundle = randomLoot.get().rollForLoot();
                    processLoot(player, lootBundle);
                }
            } else {
                final LootBundle lootBundle = PhatLootsAPI.getPhatLoot(lootName).rollForLoot();
                processLoot(player, lootBundle);
            }
        }
    }

    public void processLoot(Player player, LootBundle lootBundle) {
        if (lootBundle.getExp() > 0) {
            if (player.isOnline()) {
                player.giveExp(lootBundle.getExp());
            }
        }
        if (lootBundle.getMoney() > 0) {
            if (PhatLootsModule.getQuests() != null) {
                if (PhatLootsModule.getQuests().getDependencies().getVaultEconomy() != null) {
                    PhatLootsModule.getQuests().getDependencies().getVaultEconomy()
                            .depositPlayer(player, lootBundle.getMoney());
                }
            }
        }
        if (!lootBundle.getItemList().isEmpty()) {
            if (player.isOnline()) {
                for (final ItemStack is : lootBundle.getItemList()) {
                    try {
                        InventoryUtil.addItem(player, is);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (!lootBundle.getCommandList().isEmpty() && player.isOnline()) {
            for (final CommandLoot cl : lootBundle.getCommandList()) {
                cl.execute(player);
            }
        }
        if (!lootBundle.getMessageList().isEmpty() && player.isOnline()) {
            for (final String ml : lootBundle.getMessageList()) {
                player.sendMessage(ml);
            }
        }
    }

    public static <E> Optional<E> getRandom(Collection<E> collection) {
        return collection.stream().skip(new Random().nextInt(collection.size())).findFirst();
    }
}
