package com.lypaka.betterlures.Lures;

import java.util.ArrayList;
import java.util.List;

public class Lure {

    private final String lureName;
    private final boolean applySpawnChance;
    private final List<String> blacklist;
    private final String bossBarColor;
    private final String bossBarText;
    private final int cooldown;
    private final int cost;
    private final String displayItemID;
    private final String displayItemName;
    private final List<String> lore;
    private final boolean followsBiomeLogic;
    private final double legendarySpawnChance;
    private final String activateMessage;
    private final String deactivateMessage;
    private final String pauseMessage;
    private final List<String> permissions;
    private final String shinyChance;
    private final double spawnChance;
    private final String spawnMode;
    private String speciesMode;
    private SimpleSpeciesMode simpleSpeciesMode;
    private ComplicatedSpeciesMode complicatedSpeciesMode;
    private List<String> typeList = new ArrayList<>();
    private final int timer;

    public Lure (String lureName, boolean applySpawnChance, List<String> blacklist, String bossBarColor, String bossBarText, int cooldown, int cost, String displayItemID,
                 String displayItemName, List<String> lore, boolean followsBiomeLogic, double legendarySpawnChance, String activateMessage, String deactivateMessage,
                 String pauseMessage, List<String> permissions, String shinyChance, double spawnChance, String spawnMode, String speciesMode, SimpleSpeciesMode mode, int timer) {

        this.lureName = lureName;
        this.applySpawnChance = applySpawnChance;
        this.blacklist = blacklist;
        this.bossBarColor = bossBarColor;
        this.bossBarText = bossBarText;
        this.cooldown = cooldown;
        this.cost = cost;
        this.displayItemID = displayItemID;
        this.displayItemName = displayItemName;
        this.lore = lore;
        this.followsBiomeLogic = followsBiomeLogic;
        this.legendarySpawnChance = legendarySpawnChance;
        this.activateMessage = activateMessage;
        this.deactivateMessage = deactivateMessage;
        this.pauseMessage = pauseMessage;
        this.permissions = permissions;
        this.shinyChance = shinyChance;
        this.spawnChance = spawnChance;
        this.spawnMode = spawnMode;
        this.speciesMode = speciesMode;
        this.simpleSpeciesMode = mode;
        this.timer = timer;

    }

    public Lure (String lureName, boolean applySpawnChance, List<String> blacklist, String bossBarColor, String bossBarText, int cooldown, int cost, String displayItemID,
                 String displayItemName, List<String> lore, boolean followsBiomeLogic, double legendarySpawnChance, String activateMessage, String deactivateMessage,
                 String pauseMessage, List<String> permissions, String shinyChance, double spawnChance, String spawnMode, String speciesMode, ComplicatedSpeciesMode mode, int timer) {

        this.lureName = lureName;
        this.applySpawnChance = applySpawnChance;
        this.blacklist = blacklist;
        this.bossBarColor = bossBarColor;
        this.bossBarText = bossBarText;
        this.cooldown = cooldown;
        this.cost = cost;
        this.displayItemID = displayItemID;
        this.displayItemName = displayItemName;
        this.lore = lore;
        this.followsBiomeLogic = followsBiomeLogic;
        this.legendarySpawnChance = legendarySpawnChance;
        this.activateMessage = activateMessage;
        this.deactivateMessage = deactivateMessage;
        this.pauseMessage = pauseMessage;
        this.permissions = permissions;
        this.shinyChance = shinyChance;
        this.spawnChance = spawnChance;
        this.spawnMode = spawnMode;
        this.speciesMode = speciesMode;
        this.complicatedSpeciesMode = mode;
        this.timer = timer;

    }

    public Lure (String lureName, boolean applySpawnChance, List<String> blacklist, String bossBarColor, String bossBarText, int cooldown, int cost, String displayItemID,
                 String displayItemName, List<String> lore, boolean followsBiomeLogic, double legendarySpawnChance, String activateMessage, String deactivateMessage,
                 String pauseMessage, List<String> permissions, String shinyChance, double spawnChance, String spawnMode, List<String> typeList, int timer) {

        this.lureName = lureName;
        this.applySpawnChance = applySpawnChance;
        this.blacklist = blacklist;
        this.bossBarColor = bossBarColor;
        this.bossBarText = bossBarText;
        this.cooldown = cooldown;
        this.cost = cost;
        this.displayItemID = displayItemID;
        this.displayItemName = displayItemName;
        this.lore = lore;
        this.followsBiomeLogic = followsBiomeLogic;
        this.legendarySpawnChance = legendarySpawnChance;
        this.activateMessage = activateMessage;
        this.deactivateMessage = deactivateMessage;
        this.pauseMessage = pauseMessage;
        this.permissions = permissions;
        this.shinyChance = shinyChance;
        this.spawnChance = spawnChance;
        this.spawnMode = spawnMode;
        this.typeList = typeList;
        this.timer = timer;

    }

    public void create() {

        LureRegistry.lures.put(this.lureName, this);

    }

    public String getLureName() {

        return this.lureName;

    }

    public boolean appliesSpawnChance() {

        return this.applySpawnChance;

    }

    public List<String> getBlacklist() {

        return this.blacklist;

    }

    public String getBossBarColor() {

        return this.bossBarColor;

    }

    public String getBossBarText() {

        return this.bossBarText;

    }

    public int getCooldown() {

        return this.cooldown;

    }

    public int getCost() {

        return this.cost;

    }

    public String getDisplayItemID() {

        return this.displayItemID;

    }

    public String getDisplayItemName() {

        return this.displayItemName;

    }

    public boolean followsBiomeLogic() {

        return this.followsBiomeLogic;

    }

    public double getLegendarySpawnChance() {

        return this.legendarySpawnChance;

    }

    public List<String> getLore() {

        return this.lore;

    }

    public String getActivateMessage() {

        return this.activateMessage;

    }

    public String getDeactivateMessage() {

        return this.deactivateMessage;

    }

    public String getPauseMessage() {

        return this.pauseMessage;

    }

    public List<String> getPermissions() {

        return this.permissions;

    }

    public String getShinyChance() {

        return this.shinyChance;

    }

    public double getSpawnChance() {

        return this.spawnChance;

    }

    public String getSpawnMode() {

        return this.spawnMode;

    }

    public String getSpeciesMode() {

        return this.speciesMode;

    }

    public SimpleSpeciesMode getSimpleSpeciesMode() {

        return this.simpleSpeciesMode;

    }

    public ComplicatedSpeciesMode getComplicatedSpeciesMode() {

        return this.complicatedSpeciesMode;

    }

    public List<String> getTypeList() {

        return this.typeList;

    }

    public int getTimer() {

        return this.timer;

    }

}
