package com.lypaka.betterlures.Listeners;

import com.lypaka.betterlures.API.LureUseEvent;
import com.lypaka.betterlures.ConfigGetters;
import com.lypaka.betterlures.Lures.Lure;
import com.lypaka.betterlures.Lures.LureRegistry;
import com.lypaka.betterlures.Lures.LureUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class InteractListener {

    @SubscribeEvent
    public void onItemUse (PlayerInteractEvent.RightClickItem event) {

        if (event.getSide() == LogicalSide.CLIENT) return;
        if (event.getHand() == Hand.OFF_HAND) return;

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        String displayName = player.getHeldItem(Hand.MAIN_HAND).getDisplayName().getString();
        Lure lure = LureRegistry.getFromDisplayName(displayName);
        if (lure != null) {

            if (lure.getCooldown() > 0) {

                LocalDateTime now = LocalDateTime.now();
                Map<String, String> cooldownMap = new HashMap<>();
                if (ConfigGetters.cooldownStorageMap.containsKey(player.getUniqueID().toString())) {

                    cooldownMap = ConfigGetters.cooldownStorageMap.get(player.getUniqueID().toString());
                    if (cooldownMap.containsKey(lure.getLureName())) {

                        LocalDateTime expires = LocalDateTime.parse(cooldownMap.get(lure.getLureName()));
                        if (!now.isAfter(expires)) {

                            event.setCanceled(true);
                            player.sendMessage(FancyText.getFormattedText("&cLure is on cooldown! Try again later!"), player.getUniqueID());
                            return;

                        } else {

                            LocalDateTime toSet = now.plusSeconds(lure.getCooldown());
                            cooldownMap.put(lure.getLureName(), toSet.toString());

                        }

                    } else {

                        LocalDateTime toSet = now.plusSeconds(lure.getCooldown());
                        cooldownMap.put(lure.getLureName(), toSet.toString());

                    }

                } else {

                    LocalDateTime toSet = now.plusSeconds(lure.getCooldown());
                    cooldownMap.put(lure.getLureName(), toSet.toString());

                }

                ConfigGetters.cooldownStorageMap.put(player.getUniqueID().toString(), cooldownMap);

            }
            PlayerPartyStorage storage = StorageProxy.getParty(player);
            if (lure.getCost() > 0) {

                int balance = storage.getBalance().intValue();
                if (balance < lure.getCost()) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFormattedText("&cYou don't have enough money to use this Lure!"), player.getUniqueID());
                    return;

                }

            }
            if (!lure.getPermissions().isEmpty()) {

                for (String p : lure.getPermissions()) {

                    if (!PermissionHandler.hasPermission(player, p)) {

                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this Lure!"), player.getUniqueID());
                        return;

                    }

                }

            }
            int defaultTimer = lure.getTimer();
            LureUseEvent useEvent = new LureUseEvent(player, lure, defaultTimer);
            MinecraftForge.EVENT_BUS.post(useEvent);
            if (!useEvent.isCanceled()) {

                if (lure.getCost() > 0) {

                    storage.setBalance(storage.getBalance().intValue() - lure.getCost());

                }
                LureUtils.activateLure(player, lure, true, useEvent.getTimer(), useEvent.getTimer());

            }

        }

    }

}
