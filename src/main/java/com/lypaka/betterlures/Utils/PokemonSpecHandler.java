package com.lypaka.betterlures.Utils;

import com.lypaka.betterlures.Lures.Lure;
import com.pixelmonmod.pixelmon.api.pokemon.Nature;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbilityRegistry;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;

import java.util.Map;

public class PokemonSpecHandler {

    /**
     * palette, ability, nature, growth, form
     *
     */
    public static void applyPokemonSpecs (Pokemon pokemon, Map<String, Map<String, Double>> specs) {

        for (Map.Entry<String, Map<String, Double>> entry : specs.entrySet()) {

            String spec = entry.getKey();
            Map<String, Double> values = entry.getValue();
            switch (spec.toLowerCase()) {

                case "ability":
                    for (Map.Entry<String, Double> e : values.entrySet()) {

                        if (RandomHelper.getRandomChance(e.getValue())) {

                            pokemon.setAbility(AbilityRegistry.getAbility(e.getKey()));
                            break;

                        }

                    }
                    break;

                case "nature":
                    for (Map.Entry<String, Double> e : values.entrySet()) {

                        if (RandomHelper.getRandomChance(e.getValue())) {

                            pokemon.setNature(Nature.natureFromString(e.getKey()));
                            break;

                        }

                    }
                    break;

                case "growth":
                    for (Map.Entry<String, Double> e : values.entrySet()) {

                        if (RandomHelper.getRandomChance(e.getValue())) {

                            pokemon.setGrowth(EnumGrowth.getGrowthFromString(e.getKey()));
                            break;

                        }

                    }
                    break;

                case "palette":
                    for (Map.Entry<String, Double> e : values.entrySet()) {

                        if (RandomHelper.getRandomChance(e.getValue())) {

                            pokemon.setPalette(e.getKey());
                            break;

                        }

                    }
                    break;

                case "form":
                    for (Map.Entry<String, Double> e : values.entrySet()) {

                        if (RandomHelper.getRandomChance(e.getValue())) {

                            pokemon.setForm(e.getKey());
                            break;

                        }

                    }
                    break;

            }

        }

    }

}
