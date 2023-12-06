package com.lypaka.betterlures.Utils;

import com.lypaka.betterlures.API.LuresLoadedEvent;
import com.lypaka.betterlures.BetterLures;
import com.lypaka.betterlures.Lures.LureUtils;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.species.Pokedex;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnSet;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Loads all biomes and populates them with a list of Pokemon that can spawn in those biomes
 * Used if BetterOutbreaks is not installed (since it already does this, no need to do it twice)
 */
public class PokemonBiomeMap {

    public static void load() {

        BetterLures.logger.info("Loading Pokemon biome map, please wait...");
        List<String> biomeList = new ArrayList<>();
        for (Biome biome : ForgeRegistries.BIOMES.getValues()) {

            biomeList.add(biome.getRegistryName().toString());

        }

        AtomicInteger pokemonIndex = new AtomicInteger(1);
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {

                if (pokemonIndex.get() > Pokedex.pokedexSize) {

                    this.cancel();
                    BetterLures.logger.info("Finished loading Pokemon biome map!");
                    LuresLoadedEvent loadedEvent = new LuresLoadedEvent();
                    MinecraftForge.EVENT_BUS.post(loadedEvent);

                }
                Pokemon pokemon = PokemonBuilder.builder().species(pokemonIndex.get()).build();
                for (Stats form : pokemon.getSpecies().getForms()) {

                    String formName = form.getName() == null ? "" : form.getName();
                    if (formName.contains("mega")) continue;
                    Iterator var58 = PixelmonSpawning.grassSpawner.spawnSets.iterator();
                    SpawnInfo info;
                    label348:
                    while (var58.hasNext()) {

                        SpawnSet set = (SpawnSet) var58.next();
                        Iterator var71 = set.spawnInfos.iterator();
                        while (true) {

                            do {

                                do {

                                    if (!var71.hasNext()) {

                                        continue label348;

                                    }

                                    info = (SpawnInfo) var71.next();

                                } while (!(info instanceof SpawnInfoPokemon));
                            } while(!((SpawnInfoPokemon)info).getPokemonSpec().toString().contains(pokemon.getLocalizedName()));

                            Iterator var80 = info.condition.biomes.iterator();
                            while (var80.hasNext()) {

                                Biome biome2 = (Biome) var80.next();
                                ResourceLocation resourceLocation = ForgeRegistries.BIOMES.getKey(biome2);
                                for (String biomeID : biomeList) {

                                    if (resourceLocation.toString().equalsIgnoreCase(biomeID)) {

                                        List<Pokemon> pokemonNames;
                                        if (LureUtils.biomeMap.containsKey(biomeID)) {

                                            pokemonNames = LureUtils.biomeMap.get(biomeID);

                                        } else {

                                            pokemonNames = new ArrayList<>();

                                        }
                                        if (!pokemonNames.contains(pokemon)) {

                                            pokemonNames.add(PokemonBuilder.builder().species(pokemon.getSpecies()).form(formName).build());
                                            LureUtils.biomeMap.put(biomeID, pokemonNames);

                                        }

                                    }

                                }


                            }

                        }

                    }

                }
                pokemonIndex.set(pokemonIndex.get() + 1);

            }

        }, 0, 40);

    }

}
