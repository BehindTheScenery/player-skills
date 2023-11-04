package net.impleri.playerskills.manager;

import net.impleri.playerskills.registry.RegistryItemNotFound;
import net.impleri.playerskills.server.ServerApi;
import net.impleri.playerskills.server.api.Skill;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.HashMap;
import java.util.Map;

public class PresetManager {


    private static final PresetManager Instance = new PresetManager();

    private HashMap<String, PresetData> presetMap = new HashMap<String, PresetData>();

    public HashMap<String, PresetData> getPresetMap() {
        return presetMap;
    }

    public void setPresetMap(HashMap<String, PresetData> presetMap) {
        this.presetMap = presetMap;
    }

    public void setPreset(CommandSourceStack source, String preset) {
        if(!getPresetMap().containsKey(preset)) {
            source.sendFailure(Component.literal("Preset " + preset + " not found!").withStyle(ChatFormatting.DARK_RED));
            return;
        }
        PresetData presetData = getPresetMap().get(preset);
        for(Map.Entry<String, Boolean> entry : presetData.presetData.entrySet()) {
            try {
                ServerApi.set(source.getPlayer(), Skill.find(entry.getKey()), entry.getValue());
                var message = Component.translatable(
                        "commands.playerskills.skill_changed",
                        Component.literal(entry.getKey()).withStyle(ChatFormatting.DARK_AQUA),
                        Component.literal(entry.getValue().toString()).withStyle(ChatFormatting.RED, ChatFormatting.ITALIC),
                        Component.literal(source.getTextName()).withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN)
                );
                source.sendSuccess(message, true);
            } catch (RegistryItemNotFound e) {
                source.sendFailure(Component.literal("Skill " + entry.getKey() + " not found!").withStyle(ChatFormatting.DARK_RED));
            }
        }
        source.sendSuccess(Component.literal("Preset " + preset + " activated!").withStyle(ChatFormatting.DARK_GREEN), true);
    }

    public static PresetManager getInstance() {
        return Instance;

    }
}
