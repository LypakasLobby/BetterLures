package com.lypaka.betterlures.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.betterlures.BetterLures;
import com.lypaka.betterlures.Lures.LureUtils;
import com.lypaka.lypakautils.FancyText;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LoginListener {

    @SubscribeEvent
    public void onLogin (PlayerEvent.PlayerLoggedInEvent event) throws ObjectMappingException {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        BetterLures.playerConfigManager.loadPlayer(player.getUniqueID());
        Map<String, Map<String, Integer>> lureMap = BetterLures.playerConfigManager.getPlayerConfigNode(player.getUniqueID(), "Active-Lures").getValue(new TypeToken<Map<String, Map<String, Integer>>>() {});
        if (!lureMap.isEmpty()) {

            List<String> lureNames = new ArrayList<>();
            for (Map.Entry<String, Map<String, Integer>> entry : lureMap.entrySet()) {

                lureNames.add(entry.getKey());

            }

            String[] names = lureNames.toArray(new String[0]);
            String b = "&eHey! You have these lures active: &a";
            if (lureNames.size() == 1) {

                b = "&eHey! You have this lure active: &a";

            }
            player.sendMessage(FancyText.getFormattedText(b + Arrays.toString(names).replace("[", "").replace("]", "")), player.getUniqueID());
            player.sendMessage(FancyText.getFormattedText("&eTo reactivate, use \"/lures resume <lureName>\"!"), player.getUniqueID());

        }

    }

    @SubscribeEvent
    public void onLeave (PlayerEvent.PlayerLoggedOutEvent event) {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        if (LureUtils.activeLures.containsKey(player.getUniqueID())) {

            List<String> lures = LureUtils.activeLures.get(player.getUniqueID());
            for (String l : lures) {

                LureUtils.pauseLure(player, l);

            }

        }

    }

}
