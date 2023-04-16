package com.lypaka.betterlures.Commands;

import com.lypaka.betterlures.ConfigGetters;
import com.lypaka.betterlures.Lures.Lure;
import com.lypaka.betterlures.Lures.LureRegistry;
import com.lypaka.betterlures.Lures.LureUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class GiveCommand {

    private static final SuggestionProvider<CommandSource> LURES = (context, builder) ->
            ISuggestionProvider.suggest(ConfigGetters.lureFiles, builder);

    public GiveCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterLuresCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("give")
                                            .then(
                                                    Commands.argument("player", EntityArgument.players())
                                                            .then(
                                                                    Commands.argument("lure", StringArgumentType.word())
                                                                            .suggests(LURES)
                                                                            .then(
                                                                                    Commands.argument("amount", IntegerArgumentType.integer(1))
                                                                                            .executes(c -> {

                                                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                                                    if (!PermissionHandler.hasPermission(player, "betterlures.command.admin")) {

                                                                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                                                                        return 0;

                                                                                                    }

                                                                                                }

                                                                                                ServerPlayerEntity target = EntityArgument.getPlayer(c, "player");
                                                                                                String lureName = StringArgumentType.getString(c, "lure");
                                                                                                if (!lureName.contains(".conf")) lureName = lureName + ".conf";
                                                                                                int amount = IntegerArgumentType.getInteger(c, "amount");
                                                                                                String lureCount = "Lures";
                                                                                                if (amount == 1) lureCount = "Lure";
                                                                                                Lure lure = LureRegistry.getFromName(lureName);
                                                                                                if (lure != null) {

                                                                                                    target.addItemStackToInventory(LureUtils.buildLure(lure.getDisplayItemID(), lure.getDisplayItemName(), lure.getLore(), amount));
                                                                                                    c.getSource().sendFeedback(FancyText.getFormattedText("&aSuccessfully gave " + target.getName().getString() + " " + amount + " " + lure.getLureName() + " " + lureCount + "!"), true);

                                                                                                } else {

                                                                                                    c.getSource().sendErrorMessage(FancyText.getFormattedText("&cInvalid lure name!"));

                                                                                                }

                                                                                                return 1;

                                                                                            })

                                                                            )
                                                                            .executes(c -> {

                                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                                    if (!PermissionHandler.hasPermission(player, "betterlures.command.admin")) {

                                                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                                                        return 0;

                                                                                    }

                                                                                }

                                                                                ServerPlayerEntity target = EntityArgument.getPlayer(c, "player");
                                                                                String lureName = StringArgumentType.getString(c, "lure");
                                                                                int amount = 1;
                                                                                Lure lure = LureRegistry.getFromName(lureName);
                                                                                if (lure != null) {

                                                                                    target.addItemStackToInventory(LureUtils.buildLure(lure.getDisplayItemID(), lure.getDisplayItemName(), lure.getLore(), amount));
                                                                                    c.getSource().sendFeedback(FancyText.getFormattedText("&aSuccessfully gave " + target.getName().getString() + " " + amount + " " + lure.getLureName() + " Lure!"), true);

                                                                                } else {

                                                                                    c.getSource().sendErrorMessage(FancyText.getFormattedText("&cInvalid lure name!"));

                                                                                }

                                                                                return 1;

                                                                            })
                                                            )
                                            )
                            )
            );

        }

    }

}
