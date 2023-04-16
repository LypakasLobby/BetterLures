package com.lypaka.betterlures.Listeners;

import com.lypaka.betterlures.Lures.Lure;
import com.lypaka.betterlures.Lures.LurePokemonList;
import com.lypaka.betterlures.Lures.LureUtils;
import com.lypaka.betterlures.Utils.PokemonSpecHandler;
import com.lypaka.betterpixelmonspawner.API.PokemonSpawnEvent;
import com.pixelmonmod.pixelmon.api.config.PixelmonConfigProxy;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.IVStore;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BPSPokemonSpawnListener {

    @SubscribeEvent
    public void onBPSSpawn (PokemonSpawnEvent event) {

        PixelmonEntity pokemon = event.getPokemon();
        ServerPlayerEntity player = event.getPlayer();

        String biomeID = player.world.getBiome(player.getPosition()).getRegistryName().toString();
        if (LurePokemonList.lurePokemonMap.containsKey(player.getUniqueID())) {

            Map<Lure, Map<Pokemon, Double>> pokemonMap = LurePokemonList.lurePokemonMap.get(player.getUniqueID());
            List<Pokemon> spawnOptions = new ArrayList<>();
            for (Map.Entry<Lure, Map<Pokemon, Double>> entry : pokemonMap.entrySet()) {

                Lure lure = entry.getKey();
                if (!lure.getSpawnMode().equalsIgnoreCase("natural")) continue;
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

            if (spawnOptions.size() == 0) return;
            Pokemon selectedSpawn = RandomHelper.getRandomElementFromList(spawnOptions);
            int maxLevel = selectedSpawn.getForm().getSpawn().getSpawnLevel() + selectedSpawn.getForm().getSpawn().getSpawnLevelRange();
            int level = RandomHelper.getRandomNumberBetween(selectedSpawn.getForm().getSpawn().getSpawnLevel(), maxLevel);

            double x = pokemon.getPosition().getX();
            double y = pokemon.getPosition().getY();
            double z = pokemon.getPosition().getZ();

            PixelmonEntity newSpawn = selectedSpawn.getOrCreatePixelmon();
            if (newSpawn.getFlyingParameters() == null) newSpawn.setFlying(false);
            int[] newIVs = new int[]{RandomHelper.getRandomNumberBetween(1, 31), RandomHelper.getRandomNumberBetween(1, 31), RandomHelper.getRandomNumberBetween(1, 31), RandomHelper.getRandomNumberBetween(1, 31), RandomHelper.getRandomNumberBetween(1, 31), RandomHelper.getRandomNumberBetween(1, 31)};
            newSpawn.getPokemon().getStats().setIVs(new IVStore(newIVs));
            newSpawn.setPositionAndUpdate(x, y, z);
            newSpawn.getPokemon().setLevel(level);
            newSpawn.getPokemon().setLevelNum(level);
            newSpawn.getPokemon().setShiny(newSpawn.getPokemon().isShiny());
            event.setPokemon(newSpawn);

        }

    }

}
