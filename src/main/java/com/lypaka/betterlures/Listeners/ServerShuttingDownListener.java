package com.lypaka.betterlures.Listeners;

import com.lypaka.betterlures.BetterLures;
import com.lypaka.betterlures.ConfigGetters;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

@Mod.EventBusSubscriber(modid = BetterLures.MOD_ID)
public class ServerShuttingDownListener {

    @SubscribeEvent
    public static void onServerShuttingDown (FMLServerStoppingEvent event) {

        BetterLures.configManager.getConfigNode(1, "Cooldown-Storage").setValue(ConfigGetters.cooldownStorageMap);
        BetterLures.configManager.save();

    }

}
