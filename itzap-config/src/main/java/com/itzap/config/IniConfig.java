package com.itzap.config;

import com.itzap.common.AnyConfig;
import com.itzap.common.Property;
import com.itzap.common.exception.IZapException;
import com.itzap.config.exception.ConfigErrorCodes;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.ini4j.Ini;

import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IniConfig implements AnyConfig {
    private static final Pattern KEY_PATTERN = Pattern.compile("^\\[(.*)\\](.*)");

    private final String fileName;
    private final String sectionName;
    private final Ini ini;
    private Ini.Section section;

    IniConfig(ConfigBuilder builder) {
        this.fileName = builder.fileName;
        this.sectionName = builder.configName;
        ini = new Ini();
    }

    @Override
    public String getString(Property key) {
        Pair sectionKey = parseSection(key);
        if (sectionKey.getLeft() == StringUtils.EMPTY) {
            return section.get(key.getName());
        } else {
            Ini.Section tempSection = ini.get(sectionKey.getLeft());
            return tempSection.get(sectionKey.getRight(), key.getDefault());
        }
    }

    @Override
    public void load() {
        try {
            ini.load(new FileReader(fileName));
            if (sectionName != null) {
                section = ini.get(sectionName);
                if (section == null) {
                    throw new IZapException(String.format("Section [%s] is not found in file [%s]",
                            this.sectionName, this.fileName),
                            ConfigErrorCodes.INI_SECTION_NOT_FOUND);
                }
            }
        } catch (IOException e) {
            throw new IZapException(String.format("File [%s] is not found", this.fileName),
                    ConfigErrorCodes.CONFIG_NOT_FOUND, e);
        }
    }

    @Override
    public AnyConfig getConfig(String name) {
        return ConfigBuilder.builder(ConfigType.INI)
                .setFileName(this.fileName)
                .setConfigName(name)
                .build();
    }

    private static Pair<String, String> parseSection(Property key) {
        Matcher matcher = KEY_PATTERN.matcher(key.getName());

        if (matcher.find() && matcher.groupCount() == 2) {
            return ImmutablePair.of(matcher.group(1), matcher.group(2));
        }

        return ImmutablePair.of(StringUtils.EMPTY, key.getName());
    }
}
