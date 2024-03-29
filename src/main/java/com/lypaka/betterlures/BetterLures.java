package com.lypaka.betterlures;

import com.lypaka.betterlures.Lures.LureRegistry;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ComplexConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.lypakautils.ConfigurationLoaders.PlayerConfigManager;
import net.minecraftforge.fml.common.Mod;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("betterlures")
public class BetterLures {

    public static final String MOD_ID = "betterlures";
    public static final String MOD_NAME = "BetterLures";
    public static final Logger logger = LogManager.getLogger();
    public static BasicConfigManager configManager;
    public static PlayerConfigManager playerConfigManager;
    public static ComplexConfigManager lureConfigManager;
    public static boolean isPixelBoostersInstalled = false;

    public BetterLures() throws IOException, ObjectMappingException {

        Path dir = ConfigUtils.checkDir(Paths.get("./config/betterlures"));
        String[] files = new String[]{"betterlures.conf", "storage.conf"};
        configManager = new BasicConfigManager(files, dir, BetterLures.class, MOD_NAME, MOD_ID, logger);
        configManager.init();
        playerConfigManager = new PlayerConfigManager("account.conf", "player-accounts", dir, BetterLures.class, MOD_NAME, MOD_ID, logger);
        playerConfigManager.init();
        ConfigGetters.load();
        lureConfigManager = new ComplexConfigManager(ConfigGetters.lureFiles, "lures", "settings.conf", dir, BetterLures.class, MOD_NAME, MOD_ID, logger);
        lureConfigManager.init();
        LureRegistry.loadLures();

    }

}
