package com.lypaka.betterlures.Commands;

import com.google.common.reflect.TypeToken;
import com.lypaka.betterlures.BetterLures;
import com.lypaka.betterlures.Lures.Lure;
import com.lypaka.betterlures.Lures.LureRegistry;
import com.lypaka.betterlures.Lures.LureUtils;
import com.lypaka.lypakautils.FancyText;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.Map;

public class ResumeCommand {

    public ResumeCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterLuresCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("resume")
                                            .then(
                                                    Commands.argument("lure", StringArgumentType.word())
                                                            .suggests(BetterLuresCommand.LURE_SUGGESTIONS)
                                                            .executes(c -> {

                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                    String lureArg = StringArgumentType.getString(c, "lure");
                                                                    if (!lureArg.contains(".conf")) lureArg = lureArg + ".conf";
                                                                    Lure lure = LureRegistry.getFromName(lureArg);
                                                                    if (lure == null) {

                                                                        player.sendMessage(FancyText.getFormattedText("&cInvalid lure name!"), player.getUniqueID());
                                                                        return 1;

                                                                    }

                                                                    if (BetterLures.playerConfigManager.getPlayerConfigNode(player.getUniqueID(), "Active-Boosters", lure.getLureName()).isVirtual()) {

                                                                        player.sendMessage(FancyText.getFormattedText("&eYou do not have this booster currently active!"), player.getUniqueID());
                                                                        return 1;

                                                                    }

                                                                    try {

                                                                        Map<String, Integer> boosterMap = BetterLures.playerConfigManager.getPlayerConfigNode(player.getUniqueID(), "Active-Boosters", lure.getLureName()).getValue(new TypeToken<Map<String, Integer>>() {});
                                                                        int current = boosterMap.get("Current");
                                                                        int max = boosterMap.get("Max");
                                                                        LureUtils.activateLure(player, lure, false, current, max);
                                                                        BetterLures.playerConfigManager.getPlayerConfigNode(player.getUniqueID(), "Active-Boosters", lure.getLureName()).setValue(null);
                                                                        BetterLures.playerConfigManager.savePlayer(player.getUniqueID());

                                                                    } catch (ObjectMappingException e) {

                                                                        e.printStackTrace();

                                                                    }

                                                                }

                                                                return 0;

                                                            })
                                            )
                            )
            );

        }

    }

}
