package net.impleri.playerskills.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.impleri.playerskills.manager.PresetFileManager;
import net.impleri.playerskills.manager.PresetManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

/*
 *  DELETE AT RELEASE!
 *  Temporary solution for testing the mod
 */

public class PlayerSkillsPresetsCommands {
     public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection) {
        final var command = dispatcher.register(Commands.literal("skills")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("presets")
                .then(Commands.literal("set")
                                        .then(Commands.argument("value", StringArgumentType.string())
                                                .executes(context -> setMode(
                                                        context.getSource(),
                                                        StringArgumentType.getString(context, "value")
                                                ))
                                        )
                                )
                .then(Commands.literal("init")
                        .executes(context -> init(context.getSource()))))
        );
    }


    private static int init(CommandSourceStack source) {
        try {
            PresetFileManager.init();
            source.sendSuccess(Component.literal("Config file initialized!").withStyle(ChatFormatting.DARK_GREEN), true);
        } catch (Exception e) {
            source.sendFailure(Component.literal("Error while creating config file!").withStyle(ChatFormatting.DARK_RED));
            source.sendFailure(Component.literal(e.getMessage()).withStyle(ChatFormatting.RED));
        }
        return 1;
    }

    private static int setMode(CommandSourceStack source, String mode) {
        PresetManager.getInstance().setPreset(source, mode);
        return 1;
    }

}
