package com.lypaka.betterlures.Listeners.BetterAreas;

import com.lypaka.betterareas.API.AreaEnterEvent;
import com.lypaka.betterareas.API.AreaLeaveEvent;
import com.lypaka.betterlures.Lures.Lure;
import com.lypaka.betterlures.Lures.LurePokemonList;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class AreaUpdateListener {

    @SubscribeEvent
    public void onAreaEnter (AreaEnterEvent event) {

        ServerPlayerEntity player = event.getPlayer();
        if (LurePokemonList.lurePokemonMap.containsKey(player.getUniqueID())) {

            Map<Lure, Map<Pokemon, Double>> pokemonMap = LurePokemonList.lurePokemonMap.get(player.getUniqueID());
            for (Map.Entry<Lure, Map<Pokemon, Double>> entry : pokemonMap.entrySet()) {

                LurePokemonList.populateLureSpawns(player, entry.getKey());

            }

        }

    }

    @SubscribeEvent
    public void onAreaLeave (AreaLeaveEvent event) {

        ServerPlayerEntity player = event.getPlayer();
        if (LurePokemonList.lurePokemonMap.containsKey(player.getUniqueID())) {

            Map<Lure, Map<Pokemon, Double>> pokemonMap = LurePokemonList.lurePokemonMap.get(player.getUniqueID());
            for (Map.Entry<Lure, Map<Pokemon, Double>> entry : pokemonMap.entrySet()) {

                LurePokemonList.populateLureSpawns(player, entry.getKey());

            }

        }

    }

}
