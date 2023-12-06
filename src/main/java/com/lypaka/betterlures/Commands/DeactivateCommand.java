package com.lypaka.betterlures.Commands;

import com.lypaka.betterlures.Lures.Lure;
import com.lypaka.betterlures.Lures.LureRegistry;
import com.lypaka.betterlures.Lures.LureUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class DeactivateCommand {

    public DeactivateCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterLuresCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("deactivate")
                                            .then(
                                                    Commands.argument("lure", StringArgumentType.word())
                                                            .suggests(BetterLuresCommand.LURE_SUGGESTIONS)
                                                            .then(
                                                                    Commands.argument("target", EntityArgument.players())
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

                                                                                ServerPlayerEntity target = EntityArgument.getPlayer(c, "target");
                                                                                try {

                                                                                    LureUtils.deactivateLure(lure, target.getUniqueID());

                                                                                } catch (ObjectMappingException e) {

                                                                                    e.printStackTrace();

                                                                                }

                                                                                return 0;

                                                                            })
                                                            )
                                            )
                            )
            );

        }

    }

}
