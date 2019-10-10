package com.itzap.config;

import com.itzap.common.Named;

public enum ConfigType implements Named {
    INI("ini"),
    TYPE_SAFE("type-safe");

    private final String name;

    ConfigType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
