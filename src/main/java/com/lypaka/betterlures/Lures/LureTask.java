package com.lypaka.betterlures.Lures;

import com.lypaka.betterlures.BetterLures;
import com.lypaka.betterlures.Utils.LureSpawnerTask;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.Listeners.JoinListener;
import net.minecraft.world.server.ServerBossInfo;

import java.util.*;

public class LureTask extends TimerTask {

    private final Lure lure;
    public Timer timer = null;
    private final List<UUID> players = new ArrayList<>();
    public static Map<UUID, Map<String, Integer>> currentMap = new HashMap<>();
    public static Map<UUID, Map<String, Integer>> maxMap = new HashMap<>();

    public LureTask (Lure lure) {

        this.lure = lure;

    }

    public String getLureName() {

        return this.lure.getLureName();

    }

    public List<UUID> getPlayers() {

        return this.players;

    }

    public void addPlayer (UUID uuid) {

        this.players.add(uuid);

    }

    public static int getCurrent (UUID uuid, String lureName) {

        return currentMap.get(uuid).get(lureName);

    }

    public static int getMax (UUID uuid, String lureName) {

        return maxMap.get(uuid).get(lureName);

    }

    @Override
    public void run() {

        if (this.players.isEmpty()) {

            this.cancel();
            this.timer.cancel();
            this.timer = new Timer();
            LureUtils.tasks.removeIf(t -> t == this);

        } else {

            this.players.removeIf(entry -> {

                int current = currentMap.get(entry).get(this.lure.getLureName());
                int max = maxMap.get(entry).get(this.lure.getLureName());
                if (current == 0) {

                    Map<String, Integer> cMapEntry = currentMap.get(entry);
                    cMapEntry.entrySet().removeIf(cEntry -> cEntry.getKey().equalsIgnoreCase(this.lure.getLureName()));
                    currentMap.put(entry, cMapEntry);
                    Map<String, Integer> mMapEntry = maxMap.get(entry);
                    mMapEntry.entrySet().removeIf(mEntry -> mEntry.getKey().equalsIgnoreCase(this.lure.getLureName()));
                    maxMap.put(entry, mMapEntry);

                    // boss bar removal
                    if (LureUtils.barMap.containsKey(entry)) {

                        Map<String, ServerBossInfo> barMap = LureUtils.barMap.get(entry);
                        barMap.entrySet().removeIf(e -> {

                            if (e.getKey().equalsIgnoreCase(this.lure.getLureName())) {

                                ServerBossInfo bar = e.getValue();
                                bar.removePlayer(JoinListener.playerMap.get(entry));
                                bar.setVisible(false);
                                return true;

                            }

                            return false;

                        });

                    }

                    // if the lure has its own spawner, remove player from that spawner's list
                    if (LureUtils.manualSpawnerTasks.containsKey(this.lure.getLureName())) {

                        LureSpawnerTask spawnerTask = LureUtils.manualSpawnerTasks.get(this.lure.getLureName());
                        spawnerTask.removePlayer(entry);

                    }

                    LurePokemonList.lurePokemonMap.get(entry).remove(this.lure);
                    if (!this.lure.getDeactivateMessage().equalsIgnoreCase("")) {

                        JoinListener.playerMap.get(entry).sendMessage(FancyText.getFormattedText(this.lure.getDeactivateMessage().replace("%lureName%", this.lure.getDisplayItemName())), entry);

                    }

                    return true;

                }

                Map<String, Integer> tempMap = new HashMap<>();
                if (currentMap.containsKey(entry)) {

                    tempMap = currentMap.get(entry);

                }
                tempMap.put(this.lure.getLureName(), current - 1);
                currentMap.put(entry, tempMap);

                // decrement boss bar
                if (LureUtils.barMap.containsKey(entry)) {

                    Map<String, ServerBossInfo> barMap = LureUtils.barMap.get(entry);
                    if (barMap.containsKey(this.lure.getLureName())) {

                        ServerBossInfo bar = barMap.get(this.lure.getLureName());
                        float percent = (float) current / max;
                        bar.setPercent(percent);
                        String name = this.lure.getBossBarText();
                        if (current == 1) {

                            name = name.replace("seconds", "second");

                        }
                        bar.setName(FancyText.getFormattedText(name.replace("%time%", String.valueOf(current))));
                        barMap.put(this.lure.getLureName(), bar);
                        LureUtils.barMap.put(entry, barMap);

                    }

                }

                return false;

            });

            if (this.players.isEmpty()) {

                this.cancel();
                this.timer.cancel();
                this.timer = new Timer();
                LureUtils.tasks.removeIf(t -> t == this);
                BetterLures.logger.info(this.lure.getLureName() + " has no players, stopping....");

            }

        }

    }

}
