package com.lypaka.betterlures.Commands;

import com.lypaka.betterlures.Lures.Lure;
import com.lypaka.betterlures.Lures.LureRegistry;
import com.lypaka.betterlures.Lures.LureUtils;
import com.lypaka.lypakautils.FancyText;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;

public class PauseCommand {

    public PauseCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterLuresCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("pause")
                                            .then(
                                                    Commands.argument("lure", StringArgumentType.word())
                                                            .suggests(
                                                                    (context, builder) ->
                                                                            ISuggestionProvider.suggest(LureRegistry.lures.keySet(), builder)
                                                            )
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

                                                                    LureUtils.pauseLure(player, lure.getLureName());

                                                                }

                                                                return 0;

                                                            })
                                            )
                            )
            );

        }

    }

}
