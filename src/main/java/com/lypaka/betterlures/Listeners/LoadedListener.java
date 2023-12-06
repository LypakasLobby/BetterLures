package com.lypaka.betterlures.Listeners;

import com.lypaka.betterlures.API.LuresLoadedEvent;
import com.lypaka.betterlures.BetterLures;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BetterLures.MOD_ID)
public class LoadedListener {

    @SubscribeEvent
    public static void onLuresLoaded (LuresLoadedEvent event) {

        MinecraftForge.EVENT_BUS.register(new InteractListener());

        if (ModList.get().isLoaded("betterboosters")) {

            BetterLures.logger.info("Detected BetterBoosters! Integrating...");
            BetterLures.isPixelBoostersInstalled = true;

        }

        Pixelmon.EVENT_BUS.register(new PixelmonSpawnListener());

    }

}
