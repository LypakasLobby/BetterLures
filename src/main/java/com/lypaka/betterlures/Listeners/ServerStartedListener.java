package com.lypaka.betterlures.Listeners;

import com.lypaka.betterlures.BetterLures;
import com.lypaka.betterlures.Utils.PokemonBiomeMap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

@Mod.EventBusSubscriber(modid = BetterLures.MOD_ID)
public class ServerStartedListener {

    @SubscribeEvent
    public static void onServerStarted (FMLServerStartedEvent event) {

        MinecraftForge.EVENT_BUS.register(new LoginListener());
        PokemonBiomeMap.load();

    }

}
