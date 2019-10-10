package com.itzap.config;

import com.itzap.common.AnyConfig;
import com.itzap.common.Property;
import com.itzap.config.exception.ConfigErrorCodes;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

import static com.itzap.common.exception.IZapException.descriptor;
import static com.itzap.common.utils.PreconditionUtils.checkFileExists;
import static com.itzap.common.utils.PreconditionUtils.checkNotNull;


public class TypesafeConfig implements AnyConfig {
    private Config config;
    private String fileName;
    private String configName;

    private TypesafeConfig(Config config) {
        this.config = checkNotNull(config, descriptor("Config cannot be null in Typesafe config"));
    }

    TypesafeConfig(ConfigBuilder builder) {
        this.fileName = builder.fileName;
        this.configName = builder.configName;
    }

    @Override
    public String getString(Property key) {
        try {
            return this.config.getString(key.getName());
        } catch (ConfigException.Missing ex) {
            return key.getDefault();
        }
    }

    @Override
    public int getInt(Property key) {
        try {
            return this.config.getInt(key.getName());
        }  catch (ConfigException.Missing ex) {
            if (StringUtils.isNoneBlank(key.getDefault())) {
                return Integer.parseInt(key.getDefault());
            }
            return 0;
        }
    }

    @Override
    public boolean getBool(Property key) {
        try {
            return this.config.getBoolean(key.getName());
        }  catch (ConfigException.Missing ex) {
            if (StringUtils.isNoneBlank(key.getDefault())) {
                return BooleanUtils.toBoolean(key.getDefault());
            }
            return false;
        }
    }

    @Override
    public void load() {
        Config fileConfig = this.config;

        if (StringUtils.isNoneBlank(fileName)) {
            File file = checkFileExists(new File(fileName),
                    descriptor(ConfigErrorCodes.CONFIG_NOT_FOUND,
                            "Config file [%s] not found", fileName));
            fileConfig = ConfigFactory.parseFile(file);
        } else {
            fileConfig = ConfigFactory.load();
        }

        if (StringUtils.isNoneBlank(this.configName)) {
            this.config = fileConfig.getConfig(configName);
        } else {
            this.config = fileConfig;
        }
    }

    @Override
    public AnyConfig getConfig(String name) {
        return new TypesafeConfig(this.config.getConfig(name));
    }
}
