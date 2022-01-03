package dev.chasem.inventories;

import com.oroarmor.config.Config;
import com.oroarmor.config.ConfigItem;
import com.oroarmor.config.ConfigItemGroup;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.util.List;

import static java.util.List.of;

public class ModConfig extends Config {

    public static final ConfigItemGroup mainGroup = new ConfigGroupLevel1();

    public static final List<ConfigItemGroup> configs = of(mainGroup);

    public ModConfig() {
        super(configs, new File(FabricLoader.getInstance().getConfigDir().toFile(), "inventories.json"), "inventories");
    }

    public static class ConfigGroupLevel1 extends ConfigItemGroup {
        public static final ConfigItem<String> clientSecret = new ConfigItem<String>("clientSecret", "INSERT_CLIENT_SECRET", "Please locate your client secret on your server dashboard @ inventories.chasem.dev/dashboard");

        public ConfigGroupLevel1() {
            super(of(clientSecret), "main");
        }
    }
}
