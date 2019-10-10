package com.itzap.data.file.model;

import com.itzap.common.BuilderInterface;
import com.itzap.common.exception.IZapException;
import com.itzap.data.api.Connection;
import com.itzap.data.api.DataSource;
import com.itzap.data.file.exceptions.FileErrorCodes;

import java.io.File;

public class FileSource implements DataSource {
    private final ReadFileConnection file;

    public FileSource(Builder builder) {
        this.file = builder.file;
    }

    @Override
    public Connection getConnection() {
        return this.file;
    }

    public File getFile() {
        return file.getFile();
    }

    @Override
    public boolean isValid() {
        return file.isValid();
    }

    @Override
    public String getName() {
        return this.file.getName();
    }

    public static class Builder implements BuilderInterface<FileSource> {
        private ReadFileConnection file;
        private ReadFileConnection.Builder builder = ReadFileConnection.builder();

        public Builder setFile(ReadFileConnection file) {
            this.file = file;
            return this;
        }

        public Builder setFile(File file) {
            this.builder.setFile(file);
            return this;
        }

        public Builder setFile(String fileName) {
            this.builder.setFile(fileName);
            return this;
        }

        @Override
        public FileSource build() {
            if (this.file == null) {
                this.file = this.builder.build();
            }

            if (!this.file.isValid()) {
                throw new IZapException(String.format("File [%s] not found",
                        this.file.getFile().getAbsoluteFile()), FileErrorCodes.FILE_NOT_FOUND);
            }

            return new FileSource(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
