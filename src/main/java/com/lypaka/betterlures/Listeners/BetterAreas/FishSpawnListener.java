package com.lypaka.betterlures.Listeners.BetterAreas;

import com.lypaka.betterareas.API.AreaFishSpawnEvent;
import com.lypaka.betterareas.Areas.Area;
import com.lypaka.betterareas.Areas.AreaHandler;
import com.lypaka.betterareas.Areas.Spawns.AreaSpawns;
import com.lypaka.betterareas.Areas.Spawns.FishSpawn;
import com.lypaka.betterlures.Lures.Lure;
import com.lypaka.betterlures.Lures.LurePokemonList;
import com.lypaka.betterlures.Utils.PokemonSpecHandler;
import com.pixelmonmod.pixelmon.api.config.PixelmonConfigProxy;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FishSpawnListener {

    @SubscribeEvent
    public void onFish (AreaFishSpawnEvent event) {

        ServerPlayerEntity player = event.getPlayer();
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        int z = player.getPosition().getZ();

        List<Area> areas = AreaHandler.getSortedAreas(x, y, z, player.world);
        if (areas.size() == 0) return;

        if (LurePokemonList.lurePokemonMap.containsKey(player.getUniqueID())) {

            Area currentArea = areas.get(0);
            AreaSpawns spawns = AreaHandler.areaSpawnMap.get(currentArea);
            if (spawns.getFishSpawns().size() == 0) return;

            String time = "Night";
            List<WorldTime> times = WorldTime.getCurrent(player.world);
            for (WorldTime t : times) {

                if (t.name().contains("day") || t.name().contains("dawn") || t.name().contains("morning") || t.name().contains("afternoon")) {

                    time = "Day";
                    break;

                }

            }
            String weather = "Clear";
            if (player.world.isRaining()) {

                weather = "Rain";

            } else if (player.world.isThundering()) {

                weather = "Storm";

            }
            List<Pokemon> possiblePokemon = getFishablePokemonFromArea(spawns.getFishSpawns());
            Map<Lure, Map<Pokemon, Double>> pokemonMap = LurePokemonList.lurePokemonMap.get(player.getUniqueID());
            List<Pokemon> spawnOptions = new ArrayList<>();
            for (Map.Entry<Lure, Map<Pokemon, Double>> entry : pokemonMap.entrySet()) {

                Lure lure = entry.getKey();
                float value = PixelmonConfigProxy.getSpawning().getShinyRate();
                if (!lure.getShinyChance().equalsIgnoreCase("default")) {

                    value = Float.parseFloat(lure.getShinyChance());

                }
                if (RandomHelper.getRandomChance(lure.getSpawnChance())) {

                    Map<Pokemon, Double> map = entry.getValue();
                    float finalValue = value;
                    map.entrySet().removeIf(e2 -> {

                        for (Pokemon p : possiblePokemon) {

                            if (!e2.getKey().getSpecies().getName().equalsIgnoreCase(p.getSpecies().getName())) {

                                return true;

                            } else {

                                if (!e2.getKey().getForm().getName().equalsIgnoreCase(p.getForm().getName())) {

                                    return true;

                                }

                            }

                        }

                        if (RandomHelper.getRandomChance(e2.getValue())) {

                            boolean shiny = RandomHelper.getRandomNumberBetween(1, finalValue) == 1;
                            Pokemon p = e2.getKey();

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
                        return false;

                    });

                    if (spawnOptions.size() == 0) return;
                    Pokemon selectedSpawn = RandomHelper.getRandomElementFromList(spawnOptions);
                    int level = getLevelForPokemon(spawns.getFishSpawns(), event.getRodType(), time, weather, selectedSpawn);
                    selectedSpawn.setLevel(level);
                    selectedSpawn.setLevelNum(level);
                    event.setPokemon(selectedSpawn);

                }

            }

        }

    }

    private static int getLevelForPokemon (List<FishSpawn> spawns, String rod, String time, String weather, Pokemon pokemon) {

        int maxLevel = pokemon.getForm().getSpawn().getSpawnLevelRange() + pokemon.getForm().getSpawn().getSpawnLevel();
        int level = RandomHelper.getRandomNumberBetween(pokemon.getForm().getSpawn().getSpawnLevel(), maxLevel);
        for (FishSpawn spawn : spawns) {

            if (spawn.getSpecies().equalsIgnoreCase(pokemon.getSpecies().getName()) && spawn.getForm().equalsIgnoreCase(pokemon.getForm().getName())) {

                Map<String, Map<String, Map<String, String>>> map;
                if (spawn.getSpawnData().containsKey(rod)) {

                    map = spawn.getSpawnData().get(rod);

                } else if (spawn.getSpawnData().containsKey("Any")) {

                    map = spawn.getSpawnData().get("Any");

                } else {

                    continue;

                }

                Map<String, Map<String, String>> map2;
                if (map.containsKey(time)) {

                    map2 = map.get(time);

                } else if (map.containsKey("Any")) {

                    map2 = map.get("Any");

                } else {

                    continue;

                }

                if (map2.containsKey(weather) || map2.containsKey("Any")) {

                    level = RandomHelper.getRandomNumberBetween(spawn.getMinLevel(), spawn.getMinLevel());
                    break;

                }

            }

        }

        return level;

    }

    private static List<Pokemon> getFishablePokemonFromArea (List<FishSpawn> spawns) {

        List<Pokemon> pokemon = new ArrayList<>();
        for (FishSpawn spawn : spawns) {

            String species = spawn.getSpecies();
            String form = spawn.getForm();
            Pokemon p = PokemonBuilder.builder().species(species).build();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(form);

            }
            if (!pokemon.contains(p)) pokemon.add(p);

        }

        return pokemon;

    }

}
