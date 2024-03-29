package com.lypaka.betterlures.Lures;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.*;

public class LurePokemonList {

    public static Map<UUID, Map<Lure, Map<Pokemon, Double>>> lurePokemonMap = new HashMap<>();

    public static void populateLureSpawns (ServerPlayerEntity player, Lure lure) {

        Map<Lure, Map<Pokemon, Double>> map = new HashMap<>();
        if (lurePokemonMap.containsKey(player.getUniqueID())) {

            map = lurePokemonMap.get(player.getUniqueID());

        }
        Map<Pokemon, Double> pokemon = getAllPokemonForLure(lure);
        map.put(lure, pokemon);
        lurePokemonMap.put(player.getUniqueID(), map);

    }

    private static Map<Pokemon, Double> getAllPokemonForLure (Lure lure) {

        List<Pokemon> pokemonList = new ArrayList<>();

        // check types first
        if (!lure.getTypeList().isEmpty()) {

            for (String t : lure.getTypeList()) {

                for (Species species : PixelmonSpecies.getAll()) {

                    for (Stats form : species.getForms()) {

                        Pokemon pokemon = PokemonBuilder.builder().species(species).form(form).build();
                        for (Element type : form.getTypes()) {

                            if (type.getLocalizedName().equalsIgnoreCase(t)) {

                                if (!pokemonList.contains(pokemon)) {

                                    pokemonList.add(pokemon);

                                }

                            }

                        }

                    }

                }

            }

        } else {

            if (lure.getSimpleSpeciesMode() != null) {

                List<String> species = lure.getSimpleSpeciesMode().getSpecies();
                for (String s : species) {

                    Pokemon pokemon = PokemonBuilder.builder().species(s).build();
                    if (!pokemonList.contains(pokemon)) {

                        pokemonList.add(pokemon);

                    }

                }

            } else {

                Map<String, Map<String, Map<String, Double>>> map = lure.getComplicatedSpeciesMode().getSpeciesMap();
                for (Map.Entry<String, Map<String, Map<String, Double>>> entry : map.entrySet()) {

                    String species = entry.getKey();
                    Pokemon pokemon = PokemonBuilder.builder().species(species).build();
                    if (!pokemonList.contains(pokemon)) {

                        if (!pokemonList.contains(pokemon)) {

                            pokemonList.add(pokemon);

                        }

                    }

                }

            }

        }

        double spawnChance = 1.0;
        Map<Pokemon, Double> pokemonMap = new HashMap<>();
        filterBlacklist(lure, pokemonList);
        for (Pokemon p : pokemonList) {

            if (lure.appliesSpawnChance()) {

                spawnChance = getSpawnChance(p);

            }
            pokemonMap.put(p, spawnChance);

        }

        return pokemonMap;

    }

    private static void filterBlacklist (Lure lure, List<Pokemon> list) {

        for (String value : lure.getBlacklist()) {

            list.removeIf(entry -> {

                if (value.equalsIgnoreCase("legendaries")) {

                    return PixelmonSpecies.getLegendaries(false).contains(entry.getSpecies().getDex());

                }
                if (value.equalsIgnoreCase("mythicals")) {

                    return PixelmonSpecies.getMythicals().contains(entry.getSpecies().getDex());

                }
                if (value.equalsIgnoreCase("ultra beasts")) {

                    return PixelmonSpecies.getUltraBeasts().contains(entry.getSpecies().getDex());

                }
                if (value.contains("gen-")) {

                    int gen = Integer.parseInt(value.replace("gen-", ""));
                    return entry.getSpecies().getGeneration() == gen;

                }
                return value.equalsIgnoreCase(entry.getSpecies().getName());

            });

        }

    }

    public static double getSpawnChance (Pokemon pokemon) {

        double base;
        Random random = new Random();
        // Pokemon has no pre-evolutions and can evolve, Pokemon is baby-stage
        //if (pokemon.baseStats.preEvolutions.length == 0 && pokemon.baseStats.evolutions.length != 0) {
        if (pokemon.getForm().getPreEvolutions().size() == 0 && pokemon.getForm().getEvolutions().size() != 0) {

            base = 0.50;

            // middle stage
        } else if (pokemon.getForm().getPreEvolutions().size() != 0 && pokemon.getForm().getEvolutions().size() != 0) {

            base = 0.35;

            // final stage
        } else if (pokemon.getForm().getPreEvolutions().size() != 0 && pokemon.getForm().getEvolutions().size() == 0) {

            base = 0.05;

        } else {

            // Pokemon has no pre-evolutions and can not evolve, Pokemon is single-stage
            base = 0.25;

        }

        double mod = base * random.nextDouble();
        return base + mod;

    }

}
