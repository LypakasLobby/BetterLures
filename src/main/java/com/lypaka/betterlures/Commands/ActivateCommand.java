package com.lypaka.betterlures.Commands;

import com.lypaka.betterlures.Lures.Lure;
import com.lypaka.betterlures.Lures.LureRegistry;
import com.lypaka.betterlures.Lures.LureUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.Listeners.JoinListener;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Map;
import java.util.UUID;

public class ActivateCommand {

    public ActivateCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterLuresCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("activate")
                                            .then(
                                                    Commands.argument("lure", StringArgumentType.word())
                                                            .suggests(
                                                                    (context, builder) ->
                                                                            ISuggestionProvider.suggest(LureRegistry.lures.keySet(), builder)
                                                            )
                                                            .then(
                                                                    Commands.argument("target", EntityArgument.players())
                                                                            .then(
                                                                                    Commands.argument("time", IntegerArgumentType.integer(0))
                                                                                            .executes(c -> {

                                                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                                                    if (!PermissionHandler.hasPermission(player, "betterlures.command.admin")) {

                                                                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                                                                        return 1;

                                                                                                    }

                                                                                                }

                                                                                                String lureArg = StringArgumentType.getString(c, "lure");
                                                                                                if (!lureArg.contains(".conf")) lureArg = lureArg + ".conf";
                                                                                                Lure lure = LureRegistry.getFromName(lureArg);
                                                                                                if (lure == null) {

                                                                                                    c.getSource().sendErrorMessage(FancyText.getFormattedText("&cInvalid Lure!"));
                                                                                                    return 1;

                                                                                                }

                                                                                                // doing time first for the sake of getting the target last so the booster can just go ahead and be activated
                                                                                                int time = IntegerArgumentType.getInteger(c, "time");
                                                                                                int current;
                                                                                                if (time == 0) {

                                                                                                    current = lure.getTimer();

                                                                                                } else {

                                                                                                    current = time;

                                                                                                }

                                                                                                ServerPlayerEntity target = EntityArgument.getPlayer(c, "target");
                                                                                                for (Map.Entry<UUID, ServerPlayerEntity> entry : JoinListener.playerMap.entrySet()) {

                                                                                                    if (entry.getValue().getName().getString().equalsIgnoreCase(target.getName().getString())) {

                                                                                                        target = entry.getValue();
                                                                                                        break;

                                                                                                    }

                                                                                                }

                                                                                                if (target == null) {

                                                                                                    c.getSource().sendErrorMessage(FancyText.getFormattedText("&cInvalid player name!"));
                                                                                                    return 1;

                                                                                                }

                                                                                                LureUtils.activateLure(target, lure, false, current, time);
                                                                                                c.getSource().sendFeedback(FancyText.getFormattedText("&aSuccessfully activated Lure: " + lure.getDisplayItemName() + " for " + target.getName().getString() + "!"), true);
                                                                                                return 0;

                                                                                            })
                                                                            )
                                                            )
                                            )
                            )
            );

        }

    }

}
