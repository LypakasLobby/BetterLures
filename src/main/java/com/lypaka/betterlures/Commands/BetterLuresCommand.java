package com.lypaka.betterlures.Commands;

import com.lypaka.betterlures.BetterLures;
import com.lypaka.betterlures.Lures.LureRegistry;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = BetterLures.MOD_ID)
public class BetterLuresCommand {

    public static List<String> ALIASES = Arrays.asList("betterlures", "blures", "lures");
    public static SuggestionProvider<CommandSource> LURE_SUGGESTIONS = (context, builder) ->
            ISuggestionProvider.suggest(LureRegistry.lures.keySet(), builder);

    @SubscribeEvent
    public static void onCommandRegistration (RegisterCommandsEvent event) {

        new ActivateCommand(event.getDispatcher());
        new DeactivateCommand(event.getDispatcher());
        new GiveCommand(event.getDispatcher());
        new PauseCommand(event.getDispatcher());
        new ReloadCommand(event.getDispatcher());
        new ResumeCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());

    }

    public static void updateSuggestions() {

        LURE_SUGGESTIONS = (context, builder) ->
                ISuggestionProvider.suggest(LureRegistry.lures.keySet(), builder);

    }

}
