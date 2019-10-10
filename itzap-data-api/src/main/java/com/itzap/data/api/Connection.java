package com.itzap.data.api;

import com.itzap.common.Named;

public interface Connection extends Named {
    boolean connect();

    boolean disconnect();

    boolean isValid();

    enum ConnectionEvents implements Event {
        CREATING("creating"),
        CREATED("created"),
        CONNECTED("connected"),
        DISCONNECTED("disconnected");

        private final String name;

        ConnectionEvents(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}
