package com.lypaka.betterlures.Listeners;

import com.lypaka.betterlures.BetterLures;
import com.lypaka.betterlures.Listeners.BetterAreas.AreaUpdateListener;
import com.lypaka.betterlures.Listeners.BetterAreas.FishSpawnListener;
import com.lypaka.betterlures.Listeners.BetterAreas.HeadbuttSpawnListener;
import com.lypaka.betterlures.Listeners.BetterAreas.RockSmashSpawnListener;
import com.lypaka.betterlures.Utils.PokemonBiomeMap;
import com.lypaka.betteroutbreaks.ConfigGetters;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

@Mod.EventBusSubscriber(modid = BetterLures.MOD_ID)
public class ServerStartedListener {

    @SubscribeEvent
    public static void onServerStarted (FMLServerStartedEvent event) {

        MinecraftForge.EVENT_BUS.register(new InteractListener());
        MinecraftForge.EVENT_BUS.register(new LoginListener());

        if (ModList.get().isLoaded("pixelboosters")) {

            BetterLures.logger.info("Detected PixelBoosters! Integrating...");
            BetterLures.isPixelBoostersInstalled = true;

        }
        if (ModList.get().isLoaded("betterareas")) {

            BetterLures.logger.info("Detected BetterAreas!");
            BetterLures.isBetterAreasInstalled = true;

        }
        if (ModList.get().isLoaded("betterpixelmonspawner")) {

            BetterLures.isBetterPixelmonSpawnerInstalled = true;
            BetterLures.logger.info("Detected BetterPixelmonSpawner!");

        }
        if (ModList.get().isLoaded("betteroutbreaks")) {

            BetterLures.logger.info("Detected BetterOutbreaks!");
            BetterLures.isBetterOutbreaksInstalled = true;
            if (BetterLures.isBetterPixelmonSpawnerInstalled) {

                PokemonBiomeMap.load();

            } else if (!ConfigGetters.outbreakSelectionMode.equalsIgnoreCase("biomeCategory") && !ConfigGetters.outbreakSelectionMode.equalsIgnoreCase("biome")) {

                PokemonBiomeMap.load();

            }

        } else {

            PokemonBiomeMap.load();

        }

        if (!BetterLures.isBetterPixelmonSpawnerInstalled) {

            Pixelmon.EVENT_BUS.register(new PixelmonSpawnListener());

        } else {

            MinecraftForge.EVENT_BUS.register(new BPSPokemonSpawnListener());

        }
        if (BetterLures.isBetterAreasInstalled) {

            MinecraftForge.EVENT_BUS.register(new AreaUpdateListener());
            MinecraftForge.EVENT_BUS.register(new FishSpawnListener());
            MinecraftForge.EVENT_BUS.register(new HeadbuttSpawnListener());
            MinecraftForge.EVENT_BUS.register(new RockSmashSpawnListener());

        }

    }

}
