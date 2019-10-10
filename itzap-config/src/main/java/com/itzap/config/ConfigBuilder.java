package com.itzap.config;

import com.itzap.common.AnyConfig;
import com.itzap.common.BuilderInterface;

public class ConfigBuilder implements BuilderInterface<AnyConfig> {
    String fileName;
    String configName;
    private final ConfigType type;

    private ConfigBuilder(ConfigType type) {
        this.type = type;
    }

    public ConfigBuilder setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public ConfigBuilder setConfigName(String configName) {
        this.configName = configName;
        return this;
    }

    @Override
    public AnyConfig build() {
        AnyConfig config;

        switch (type) {
            case INI:
                config = new IniConfig(this);
                break;
            default:
                config = new TypesafeConfig(this);
        }
        config.load();

        return config;
    }

    public static ConfigBuilder builder(ConfigType type) {
        return new ConfigBuilder(type);
    }
}
