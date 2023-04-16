package com.lypaka.betterlures.Lures;

import com.google.common.reflect.TypeToken;
import com.lypaka.betterlures.BetterLures;
import com.lypaka.betterlures.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LureRegistry {

    public static Map<String, Lure> lures = new HashMap<>();

    public static void loadLures() throws ObjectMappingException {

        lures = new HashMap<>();
        for (int i = 0; i < ConfigGetters.lureFiles.size(); i++) {

            String lureName = ConfigGetters.lureFiles.get(i);
            boolean appliesSpawnChance = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Apply-Spawn-Chance").getBoolean();
            List<String> blacklist = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Blacklist").getList(TypeToken.of(String.class));
            String bossBarColor = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Boss-Bar", "Color").getString();
            String bossBarText = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Boss-Bar", "Text").getString();
            int cooldown = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Cooldown").getInt();
            int cost = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Cost").getInt();
            String displayItemID = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Display-Item", "Display-ID").getString();
            String displayItemName = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Display-Item", "Display-Name").getString();
            List<String> lore = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Display-Item", "Lore").getList(TypeToken.of(String.class));
            boolean followsBiomeLogic = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Follow-Biome-Logic").getBoolean();
            double legendarySpawnChance = 0.01; // = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Legendary-Spawn-Chance").getDouble();
            if (!BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Legendary-Spawn-Chance").isVirtual()) {

                legendarySpawnChance = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Legendary-Spawn-Chance").getDouble();

            } else {

                BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Legendary-Spawn-Chance").setValue(0.01);
                BetterLures.lureConfigManager.save();

            }
            String activateMessage = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Messages", "Activate").getString();
            String deactivateMessage = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Messages", "Deactivate").getString();
            String pauseMessage = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Messages", "Pause").getString();
            List<String> permissions = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Permissions").getList(TypeToken.of(String.class));
            String shinyChance = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Shiny-Chance").getString();
            double spawnChance = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Spawn-Chance").getDouble();
            String spawnMode = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Spawn-Mode").getString();
            String speciesMode = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Species-Mode").getString();
            int timer = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Timer").getInt();
            Lure lure;
            Map<String, String> map = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Specs").getValue(new TypeToken<Map<String, String>>() {});
            if (map.containsKey("Species")) {

                if (speciesMode.equalsIgnoreCase("simple")) {

                    SimpleSpeciesMode mode = new SimpleSpeciesMode(BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Specs", "Species").getList(TypeToken.of(String.class)));
                    lure = new Lure(lureName, appliesSpawnChance, blacklist, bossBarColor, bossBarText, cooldown, cost, displayItemID, displayItemName, lore, followsBiomeLogic, legendarySpawnChance, activateMessage, deactivateMessage, pauseMessage, permissions, shinyChance, spawnChance, spawnMode, speciesMode, mode, timer);

                } else {

                    ComplicatedSpeciesMode mode = new ComplicatedSpeciesMode(BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Specs", "Species").getValue(new TypeToken<Map<String, Map<String, Map<String, Double>>>>() {}));
                    lure = new Lure(lureName, appliesSpawnChance, blacklist, bossBarColor, bossBarText, cooldown, cost, displayItemID, displayItemName, lore, followsBiomeLogic, legendarySpawnChance, activateMessage, deactivateMessage, pauseMessage, permissions, shinyChance, spawnChance, spawnMode, speciesMode, mode, timer);

                }

            } else {

                List<String> types = BetterLures.lureConfigManager.getConfigNode(i, "Settings", "Specs", "Types").getList(TypeToken.of(String.class));
                lure = new Lure(lureName, appliesSpawnChance, blacklist, bossBarColor, bossBarText, cooldown, cost, displayItemID, displayItemName, lore, followsBiomeLogic, legendarySpawnChance, activateMessage, deactivateMessage, pauseMessage, permissions, shinyChance, spawnChance, spawnMode, types, timer);

            }

            lure.create();
            BetterLures.logger.info("Successfully created lure: " + lureName.replace(".conf", ""));

        }

    }

    public static Lure getFromName (String itemName) {

        Lure lure = null;
        for (Map.Entry<String, Lure> entry : lures.entrySet()) {

            if (entry.getKey().equalsIgnoreCase(itemName)) {

                lure = entry.getValue();
                break;

            }

        }

        return lure;

    }

    public static Lure getFromDisplayName (String displayName) {

        Lure lure = null;
        for (Map.Entry<String, Lure> entry : lures.entrySet()) {

            String name = FancyText.getFormattedString(entry.getValue().getDisplayItemName());
            if (name.equalsIgnoreCase(displayName)) {

                lure = entry.getValue();
                break;

            }

        }

        return lure;

    }

}
