package com.lypaka.betterlures.Lures;

import com.lypaka.betterlures.BetterLures;
import com.lypaka.betterlures.Utils.BossBarHandler;
import com.lypaka.betterlures.Utils.LureSpawnerTask;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.ItemStackBuilder;
import com.lypaka.lypakautils.JoinListener;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerBossInfo;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.*;

public class LureUtils {

    public static Map<String, List<Pokemon>> biomeMap = new HashMap<>();
    public static List<LureTask> tasks = new ArrayList<>();
    public static Map<UUID, List<String>> activeLures = new HashMap<>();
    public static Map<UUID, Map<String, ServerBossInfo>> barMap = new HashMap<>();
    public static Map<String, LureSpawnerTask> manualSpawnerTasks = new HashMap<>();

    public static ItemStack buildLure (String id, String displayName, List<String> itemLore, int amount) {

        ItemStack item = ItemStackBuilder.buildFromStringID(id);
        item.setDisplayName(FancyText.getFormattedText(displayName));
        ListNBT lore = new ListNBT();
        for (String s : itemLore) {

            lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(s))));

        }

        item.getOrCreateChildTag("display").put("Lore", lore);
        item.setCount(amount);
        return item;

    }

    public static void activateLure (ServerPlayerEntity player, Lure lure, boolean removeItem, int current, int timer) {

        if (removeItem) {

            player.getHeldItem(Hand.MAIN_HAND).setCount(player.getHeldItem(Hand.MAIN_HAND).getCount() - 1);

        }

        UUID uuid = player.getUniqueID();
        if (timer == 0) {

            timer = lure.getTimer();

        }
        if (current == -1) {

            current = lure.getTimer();

        }

        List<String> lures = new ArrayList<>();
        if (activeLures.containsKey(uuid)) {

            lures = activeLures.get(uuid);

        }

        if (lures.isEmpty()) {

            lures.add(lure.getLureName());

        } else {

            // "Why not use 'if (lures.contains())'"? Case sensitive shit from lazy ass typers, that's why.
            boolean contains = false;
            for (String l : lures) {

                if (l.equalsIgnoreCase(lure.getLureName())) {

                    contains = true;
                    break;

                }

            }

            if (!contains) {

                lures.add(lure.getLureName());

            }

        }

        activeLures.put(uuid, lures);

        LureTask lureTask = null;
        for (LureTask task : tasks) {

            if (task.getLureName().equalsIgnoreCase(lure.getLureName())) {

                lureTask = task;
                break;

            }

        }

        if (lureTask == null) {

            lureTask = new LureTask(lure);

        }
        Map<String, Integer> cMap = new HashMap<>();
        Map<String, Integer> mMap = new HashMap<>();

        if (!lureTask.getPlayers().contains(uuid)) {

            if (LureTask.currentMap.containsKey(uuid)) {

                cMap = LureTask.currentMap.get(uuid);

            }
            if (LureTask.maxMap.containsKey(uuid)) {

                mMap = LureTask.maxMap.get(uuid);

            }
            cMap.put(lure.getLureName(), current);
            mMap.put(lure.getLureName(), timer);
            LureTask.currentMap.put(uuid, cMap);
            LureTask.maxMap.put(uuid, mMap);
            lureTask.addPlayer(uuid);

            // build and show boss bar
            BossBarHandler bar = new BossBarHandler(player, lure);
            bar.build();

        } else {

            // this player already has this lure active, so we need to increase the max time
            if (LureTask.currentMap.containsKey(uuid)) {

                cMap = LureTask.currentMap.get(uuid);

            }
            if (LureTask.maxMap.containsKey(uuid)) {

                mMap = LureTask.maxMap.get(uuid);

            }
            int newMax = LureTask.getMax(uuid, lure.getLureName()) + timer;
            int newCurrent = LureTask.getCurrent(uuid, lure.getLureName()) + timer;
            cMap.put(lure.getLureName(), newCurrent);
            mMap.put(lure.getLureName(), newMax);
            LureTask.currentMap.put(uuid, cMap);
            LureTask.maxMap.put(uuid, mMap);

        }
        if (!tasks.contains(lureTask)) {

            lureTask.timer = new Timer();
            lureTask.timer.schedule(lureTask, 0, 1000);
            tasks.add(lureTask);

        }
        String activate = lure.getActivateMessage();
        if (!activate.equals("")) {

            player.sendMessage(FancyText.getFormattedText(activate.replace("%lureName%", lure.getDisplayItemName())), player.getUniqueID());

        }
        LurePokemonList.populateLureSpawns(player, lure);
        if (!lure.getSpawnMode().equalsIgnoreCase("natural")) {

            int seconds = Integer.parseInt(lure.getSpawnMode());
            if (manualSpawnerTasks.containsKey(lure.getLureName())) {

                LureSpawnerTask task = manualSpawnerTasks.get(lure.getLureName());
                task.addPlayer(player.getUniqueID());

            } else {

                LureSpawnerTask task = new LureSpawnerTask(lure, seconds);
                task.timer = new Timer();
                task.timer.schedule(task, 0, 1000);
                manualSpawnerTasks.put(lure.getLureName(), task);

            }

        }

    }

    public static void deactivateLure (Lure lure, UUID uuid) throws ObjectMappingException {

        LureTask task = null;

        for (LureTask lt : tasks) {

            if (lt.getLureName().equalsIgnoreCase(lure.getLureName())) {

                task = lt;
                break;

            }

        }

        if (lure == null || task == null) {

            BetterLures.logger.error("Trying to deactivate a null lure!");
            return;

        }

        if (uuid != null) {

            task.getPlayers().removeIf(entry -> {

                if (uuid.toString().equalsIgnoreCase(entry.toString())) {

                    Map<String, Integer> cMapEntry = LureTask.currentMap.get(entry);
                    cMapEntry.entrySet().removeIf(cEntry -> cEntry.getKey().equalsIgnoreCase(lure.getLureName()));
                    LureTask.currentMap.put(entry, cMapEntry);
                    Map<String, Integer> mMapEntry = LureTask.maxMap.get(entry);
                    mMapEntry.entrySet().removeIf(mEntry -> mEntry.getKey().equalsIgnoreCase(lure.getLureName()));
                    LureTask.maxMap.put(entry, mMapEntry);

                    // boss bar removal
                    if (LureUtils.barMap.containsKey(entry)) {

                        Map<String, ServerBossInfo> barMap = LureUtils.barMap.get(entry);
                        barMap.entrySet().removeIf(e -> {

                            if (e.getKey().equalsIgnoreCase(lure.getLureName())) {

                                ServerBossInfo bar = e.getValue();
                                bar.removePlayer(JoinListener.playerMap.get(entry));
                                bar.setVisible(false);
                                return true;

                            }

                            return false;

                        });

                    }

                    // if the lure has its own spawner, remove player from that spawner's list
                    if (LureUtils.manualSpawnerTasks.containsKey(lure.getLureName())) {

                        LureSpawnerTask spawnerTask = LureUtils.manualSpawnerTasks.get(lure.getLureName());
                        spawnerTask.removePlayer(entry);

                    }

                    LurePokemonList.lurePokemonMap.get(entry).remove(lure);
                    if (!lure.getDeactivateMessage().equalsIgnoreCase("")) {

                        JoinListener.playerMap.get(entry).sendMessage(FancyText.getFormattedText(lure.getDeactivateMessage().replace("%lureName%", lure.getDisplayItemName())), entry);

                    }

                    return true;

                }

                return false;

            });

        } else {

            task.getPlayers().removeIf(entry -> {

                Map<String, Integer> cMapEntry = LureTask.currentMap.get(entry);
                cMapEntry.entrySet().removeIf(cEntry -> cEntry.getKey().equalsIgnoreCase(lure.getLureName()));
                LureTask.currentMap.put(entry, cMapEntry);
                Map<String, Integer> mMapEntry = LureTask.maxMap.get(entry);
                mMapEntry.entrySet().removeIf(mEntry -> mEntry.getKey().equalsIgnoreCase(lure.getLureName()));
                LureTask.maxMap.put(entry, mMapEntry);

                // boss bar removal
                if (LureUtils.barMap.containsKey(entry)) {

                    Map<String, ServerBossInfo> barMap = LureUtils.barMap.get(entry);
                    barMap.entrySet().removeIf(e -> {

                        if (e.getKey().equalsIgnoreCase(lure.getLureName())) {

                            ServerBossInfo bar = e.getValue();
                            bar.removePlayer(JoinListener.playerMap.get(entry));
                            bar.setVisible(false);
                            return true;

                        }

                        return false;

                    });

                }

                // if the lure has its own spawner, remove player from that spawner's list
                if (LureUtils.manualSpawnerTasks.containsKey(lure.getLureName())) {

                    LureSpawnerTask spawnerTask = LureUtils.manualSpawnerTasks.get(lure.getLureName());
                    spawnerTask.removePlayer(entry);

                }

                LurePokemonList.lurePokemonMap.get(entry).remove(lure);
                if (!lure.getDeactivateMessage().equalsIgnoreCase("")) {

                    JoinListener.playerMap.get(entry).sendMessage(FancyText.getFormattedText(lure.getDeactivateMessage().replace("%lureName%", lure.getDisplayItemName())), entry);

                }

                return true;

            });

            task.cancel();
            task.timer.cancel();
            task.timer = new Timer();
            LureTask finalTask = task;
            tasks.removeIf(t -> t == finalTask);

        }

    }

    public static void pauseLure (ServerPlayerEntity player, String lure) {

        for (LureTask task : tasks) {

            if (task.getLureName().equalsIgnoreCase(lure)) {

                Map<String, Integer> currentMap = LureTask.currentMap.get(player.getUniqueID());
                for (Map.Entry<String, Integer> entry : currentMap.entrySet()) {

                    if (entry.getKey().equalsIgnoreCase(lure)) {

                        int current = entry.getValue();
                        BetterLures.playerConfigManager.getPlayerConfigNode(player.getUniqueID(), "Active-Boosters", lure, "Current").setValue(current);
                        break;

                    }

                }
                Map<String, Integer> maxMap = LureTask.maxMap.get(player.getUniqueID());
                for (Map.Entry<String, Integer> entry : maxMap.entrySet()) {

                    if (entry.getKey().equalsIgnoreCase(lure)) {

                        int max = entry.getValue();
                        BetterLures.playerConfigManager.getPlayerConfigNode(player.getUniqueID(), "Active-Boosters", lure, "Max").setValue(max);
                        break;

                    }

                }

                if (LureUtils.barMap.containsKey(player.getUniqueID())) {

                    Map<String, ServerBossInfo> barMap = LureUtils.barMap.get(player.getUniqueID());
                    barMap.entrySet().removeIf(e -> {

                        if (e.getKey().equalsIgnoreCase(lure)) {

                            ServerBossInfo bar = e.getValue();
                            bar.removePlayer(player);
                            bar.setVisible(false);
                            return true;

                        }

                        return false;

                    });

                }

                task.getPlayers().removeIf(entry -> entry.toString().equalsIgnoreCase(player.getUniqueID().toString()));
                Lure lureObject = LureRegistry.getFromName(lure);
                player.sendMessage(FancyText.getFormattedText("&eSuccessfully paused your " + lureObject.getDisplayItemName() + " &eLure!"), player.getUniqueID());
                break;

            }

        }

        BetterLures.playerConfigManager.savePlayer(player.getUniqueID());

    }

    public static boolean biomeContainsPokemon (Pokemon pokemon, String biomeID) {

        for (Pokemon p : LureUtils.biomeMap.get(biomeID)) {

            if (p.getSpecies().getName().equalsIgnoreCase(pokemon.getSpecies().getName()) && p.getForm().getName().equalsIgnoreCase(pokemon.getForm().getName())) {

                return true;

            }

        }
        return false;

    }

}
