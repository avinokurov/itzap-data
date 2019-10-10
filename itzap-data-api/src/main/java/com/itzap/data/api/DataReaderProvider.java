package com.itzap.data.api;

import com.itzap.common.Property;
import com.itzap.common.Provider;

public interface DataReaderProvider extends Provider<DataReader> {
    enum ConfigProperties implements Property {
        BUFFER_SIZE("bufferSizeMb");

        private final String name;

        ConfigProperties(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}
