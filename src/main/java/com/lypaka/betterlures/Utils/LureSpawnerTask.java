package com.lypaka.betterlures.Utils;

import com.lypaka.betterlures.Lures.Lure;
import com.lypaka.betterlures.Lures.LurePokemonList;
import com.lypaka.betterlures.Lures.LureUtils;
import com.lypaka.lypakautils.Listeners.JoinListener;
import com.pixelmonmod.pixelmon.api.config.PixelmonConfigProxy;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.IVStore;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;

import java.util.*;

/**
 * A manual spawner for Lure Pokemon, used if a lure is not linked to a natural Pokemon spawner either from Pixelmon
 */
public class LureSpawnerTask extends TimerTask {

    private final Lure lure;
    private int count = 0;
    private final int interval;
    public Timer timer = null;
    private final List<UUID> players = new ArrayList<>();

    public LureSpawnerTask (Lure lure, int interval) {

        this.lure = lure;
        this.interval = interval;

    }

    public String getLureName() {

        return this.lure.getLureName();

    }

    public int getInterval() {

        return this.interval;

    }

    public List<UUID> getPlayers() {

        return this.players;

    }

    public void addPlayer (UUID uuid) {

        this.players.add(uuid);

    }

    public void removePlayer (UUID uuid) {

        this.players.removeIf(e -> e.toString().equalsIgnoreCase(uuid.toString()));

    }

    @Override
    public void run() {

        if (this.players.isEmpty()) {

            this.cancel();
            this.timer.cancel();
            this.timer = new Timer();
            LureUtils.manualSpawnerTasks.entrySet().removeIf(e -> e.getKey().equalsIgnoreCase(this.lure.getLureName()));

        }
        this.count++;
        if (this.count == this.interval) {

            // spawn shit
            for (UUID u : this.players) {

                ServerPlayerEntity player = JoinListener.playerMap.get(u);
                String biomeID = player.world.getBiome(player.getPosition()).getRegistryName().toString();
                if (LurePokemonList.lurePokemonMap.containsKey(player.getUniqueID())) {

                    Map<Lure, Map<Pokemon, Double>> pokemonMap = LurePokemonList.lurePokemonMap.get(player.getUniqueID());
                    List<Pokemon> spawnOptions = new ArrayList<>();
                    for (Map.Entry<Lure, Map<Pokemon, Double>> entry : pokemonMap.entrySet()) {

                        Lure lure = entry.getKey();
                        if (lure.getSpawnMode().equalsIgnoreCase("natural")) continue;
                        float value = PixelmonConfigProxy.getSpawning().getShinyRate();
                        if (!lure.getShinyChance().equalsIgnoreCase("default")) {

                            value = Float.parseFloat(lure.getShinyChance());

                        }
                        if (RandomHelper.getRandomChance(lure.getSpawnChance())) {

                            Map<Pokemon, Double> map = entry.getValue();
                            boolean leggie = RandomHelper.getRandomChance(lure.getLegendarySpawnChance());
                            for (Map.Entry<Pokemon, Double> m2 : map.entrySet()) {

                                if (lure.followsBiomeLogic()) {

                                    if (LureUtils.biomeMap.containsKey(biomeID)) {

                                        if (LureUtils.biomeContainsPokemon(m2.getKey(), biomeID)) {

                                            if (RandomHelper.getRandomChance(m2.getValue())) {

                                                boolean shiny = RandomHelper.getRandomNumberBetween(1, value) == 1;
                                                Pokemon p = m2.getKey();
                                                if (leggie) {

                                                    if (PixelmonSpecies.isLegendary(p.getSpecies()) || PixelmonSpecies.isUltraBeast(p.getSpecies()) || PixelmonSpecies.isMythical(p.getSpecies())) {

                                                        if (lure.getTypeList().isEmpty()) {

                                                            if (lure.getSpeciesMode().equalsIgnoreCase("complicated")) {

                                                                if (lure.getComplicatedSpeciesMode().getSpeciesMap().containsKey(p.getSpecies().getName())) {

                                                                    Map<String, Map<String, Double>> specsMap = lure.getComplicatedSpeciesMode().getSpeciesMap().get(p.getSpecies().getName());
                                                                    PokemonSpecHandler.applyPokemonSpecs(p, specsMap);

                                                                }

                                                            }

                                                        }
                                                        p.setShiny(shiny);
                                                        spawnOptions.add(p);

                                                    }

                                                } else {

                                                    if (!PixelmonSpecies.isMythical(p.getSpecies()) && !PixelmonSpecies.isLegendary(p.getSpecies()) && !PixelmonSpecies.isUltraBeast(p.getSpecies())) {

                                                        if (lure.getTypeList().isEmpty()) {

                                                            if (lure.getSpeciesMode().equalsIgnoreCase("complicated")) {

                                                                if (lure.getComplicatedSpeciesMode().getSpeciesMap().containsKey(p.getSpecies().getName())) {

                                                                    Map<String, Map<String, Double>> specsMap = lure.getComplicatedSpeciesMode().getSpeciesMap().get(p.getSpecies().getName());
                                                                    PokemonSpecHandler.applyPokemonSpecs(p, specsMap);

                                                                }

                                                            }

                                                        }
                                                        p.setShiny(shiny);
                                                        spawnOptions.add(p);

                                                    }

                                                }

                                            }

                                        }

                                    }

                                } else {

                                    if (RandomHelper.getRandomChance(m2.getValue())) {

                                        boolean shiny = RandomHelper.getRandomNumberBetween(1, value) == 1;
                                        Pokemon p = m2.getKey();
                                        if (leggie) {

                                            if (PixelmonSpecies.isLegendary(p.getSpecies()) || PixelmonSpecies.isUltraBeast(p.getSpecies()) || PixelmonSpecies.isMythical(p.getSpecies())) {

                                                if (lure.getTypeList().isEmpty()) {

                                                    if (lure.getSpeciesMode().equalsIgnoreCase("complicated")) {

                                                        if (lure.getComplicatedSpeciesMode().getSpeciesMap().containsKey(p.getSpecies().getName())) {

                                                            Map<String, Map<String, Double>> specsMap = lure.getComplicatedSpeciesMode().getSpeciesMap().get(p.getSpecies().getName());
                                                            PokemonSpecHandler.applyPokemonSpecs(p, specsMap);

                                                        }

                                                    }

                                                }
                                                p.setShiny(shiny);
                                                spawnOptions.add(p);

                                            }

                                        } else {

                                            if (!PixelmonSpecies.isMythical(p.getSpecies()) && !PixelmonSpecies.isLegendary(p.getSpecies()) && !PixelmonSpecies.isUltraBeast(p.getSpecies())) {

                                                if (lure.getTypeList().isEmpty()) {

                                                    if (lure.getSpeciesMode().equalsIgnoreCase("complicated")) {

                                                        if (lure.getComplicatedSpeciesMode().getSpeciesMap().containsKey(p.getSpecies().getName())) {

                                                            Map<String, Map<String, Double>> specsMap = lure.getComplicatedSpeciesMode().getSpeciesMap().get(p.getSpecies().getName());
                                                            PokemonSpecHandler.applyPokemonSpecs(p, specsMap);

                                                        }

                                                    }

                                                }
                                                p.setShiny(shiny);
                                                spawnOptions.add(p);

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                    if (spawnOptions.size() == 0) continue;
                    Pokemon selectedSpawn = RandomHelper.getRandomElementFromList(spawnOptions);
                    int maxLevel = selectedSpawn.getForm().getSpawn().getSpawnLevel() + selectedSpawn.getForm().getSpawn().getSpawnLevelRange();
                    int level = RandomHelper.getRandomNumberBetween(selectedSpawn.getForm().getSpawn().getSpawnLevel(), maxLevel);

                    double x;
                    double y;
                    double z;

                    double playerX = player.getPosX();
                    double playerY = player.getPosY();
                    double playerZ = player.getPosZ();

                    if (RandomHelper.getRandomChance(50)) {

                        x = playerX - RandomHelper.getRandomNumberBetween(1, 15);

                    } else {

                        x = playerX + RandomHelper.getRandomNumberBetween(1, 15);

                    }
                    if (RandomHelper.getRandomChance(50)) {

                        z = playerZ - RandomHelper.getRandomNumberBetween(1, 15);

                    } else {

                        z = playerZ + RandomHelper.getRandomNumberBetween(1, 15);

                    }
                    Heightmap.Type type = Heightmap.Type.WORLD_SURFACE;
                    y = player.world.getChunk(new BlockPos(x, playerY, z)).getTopBlockY(type, (int)x, (int)z) + 1.5;

                    player.getServer().deferTask(() -> {

                        PixelmonEntity newSpawn = selectedSpawn.getOrCreatePixelmon();
                        if (newSpawn.getFlyingParameters() == null) newSpawn.setFlying(false);
                        newSpawn.setSpawnLocation(newSpawn.getDefaultSpawnLocation());
                        int[] newIVs = new int[]{RandomHelper.getRandomNumberBetween(1, 31), RandomHelper.getRandomNumberBetween(1, 31), RandomHelper.getRandomNumberBetween(1, 31), RandomHelper.getRandomNumberBetween(1, 31), RandomHelper.getRandomNumberBetween(1, 31), RandomHelper.getRandomNumberBetween(1, 31)};
                        newSpawn.getPokemon().getStats().setIVs(new IVStore(newIVs));
                        newSpawn.setPositionAndUpdate(x, y, z);
                        newSpawn.getPokemon().setLevel(level);
                        newSpawn.getPokemon().setLevelNum(level);
                        newSpawn.getPokemon().setShiny(newSpawn.getPokemon().isShiny());

                    });

                }

            }

            this.count = 0;

        }

    }

}
