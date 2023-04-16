package com.lypaka.betterlures;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;
import java.util.Map;

public class ConfigGetters {

    public static List<String> lureFiles;

    public static Map<String, Map<String, String>> cooldownStorageMap;

    public static void load() throws ObjectMappingException {

        lureFiles = BetterLures.configManager.getConfigNode(0, "Lure-List").getList(TypeToken.of(String.class));

        cooldownStorageMap = BetterLures.configManager.getConfigNode(1, "Cooldown-Storage").getValue(new TypeToken<Map<String, Map<String, String>>>() {});

    }

}
