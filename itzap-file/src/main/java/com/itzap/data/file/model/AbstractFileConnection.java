package com.itzap.data.file.model;

import com.itzap.common.BuilderInterface;
import com.itzap.data.api.Connection;
import com.itzap.data.api.ListenerRegistry;

import java.io.File;

public abstract class AbstractFileConnection extends ListenerRegistry implements Connection {
    private final File file;


    AbstractFileConnection(Builder builder) {
        this.file = builder.file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public boolean connect() {
        fireEvent(ConnectionEvents.CONNECTED);
        return true;
    }

    @Override
    public boolean disconnect() {
        fireEvent(ConnectionEvents.DISCONNECTED);
        return true;
    }

    @Override
    public String getName() {
        return this.file.getName();
    }

    public static abstract class Builder<T extends AbstractFileConnection> implements BuilderInterface<T> {
        private File file;

        public Builder setFile(File file) {
            this.file = file;
            return this;
        }

        public Builder setFile(String file) {
            this.file = new File(file);
            return this;
        }
    }
}
