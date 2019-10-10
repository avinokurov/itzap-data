package com.itzap.data.file.model;

public class ReadFileConnection extends AbstractFileConnection {
    private ReadFileConnection(Builder builder) {
        super(builder);
    }

    @Override
    public boolean isValid() {
        return getFile()!= null && getFile().exists();
    }

    public static class Builder extends AbstractFileConnection.Builder<ReadFileConnection> {

        @Override
        public ReadFileConnection build() {
            return new ReadFileConnection(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

