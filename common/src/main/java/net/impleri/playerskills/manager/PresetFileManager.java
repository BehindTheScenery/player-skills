package net.impleri.playerskills.manager;

import com.google.common.annotations.Beta;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PresetFileManager {

    private static final PresetFileManager Instance = new PresetFileManager();
    private static final PresetManager presetManager = PresetManager.getInstance();
    private static final Path MINECRAFT_FOLDER_PATH = Paths.get("").toAbsolutePath();
    private static final Path MINECRAFT_MOD_FOLDER_PATH = Paths.get(MINECRAFT_FOLDER_PATH + File.separator + "config" + File.separator + "skills");
    private static final Path MINECRAFT_MOD_CONFIG_PATH = Paths.get(MINECRAFT_MOD_FOLDER_PATH + File.separator + "config.json");

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String init() {
        try {
            if (!Files.exists(MINECRAFT_MOD_FOLDER_PATH)) {
                new File(String.valueOf(MINECRAFT_MOD_FOLDER_PATH.toAbsolutePath())).mkdir();
            }
            if (!Files.exists(MINECRAFT_MOD_CONFIG_PATH)) {
                Writer writer = Files.newBufferedWriter(MINECRAFT_MOD_CONFIG_PATH, StandardCharsets.UTF_8);
                Map<String, Boolean> defaultPresetData = new HashMap<String, Boolean>();
                defaultPresetData.put("skills:stage_reaper", false);
                defaultPresetData.put("skills:stage_simply_light", false);
                Map<String, Boolean> developPresetData = new HashMap<String, Boolean>();
                developPresetData.put("skills:stage_reaper", true);
                developPresetData.put("skills:stage_simply_light", true);

                Map<String, PresetData> presets = new HashMap<String, PresetData>();
                presets.put("default", new PresetData(defaultPresetData));
                presets.put("develop", new PresetData(developPresetData));

                MINECRAFT_MOD_CONFIG_PATH.toFile().createNewFile();
                JsonElement tree = gson.toJsonTree(presets);
                gson.toJson(tree, writer);
                writer.close();
            }
            presetManager.setPresetMap(getPresetFromConfig());
            return String.valueOf(getPresetFromConfig());
        } catch(Exception e) {
            Log.error(LogCategory.LOG, "Error with mod files! \n" + e.getMessage(), e);
        }
        return null;
    }


    public static HashMap<String, PresetData> getPresetFromConfig() {
        JsonReader reader;
        try {
            reader = new JsonReader(new FileReader(MINECRAFT_MOD_CONFIG_PATH.toAbsolutePath().toString()));
            HashMap<String, PresetData> presets = gson.fromJson(reader, new TypeToken<HashMap<String, PresetData>>() {}.getType());
            reader.close();
            return presets;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PresetFileManager getInstance() {
        return Instance;
    }
}