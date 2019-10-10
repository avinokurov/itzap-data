package com.itzap.data.api;

import com.itzap.common.BuilderInterface;
import io.reactivex.Completable;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public interface IOData {
    Logger LOGGER = LoggerFactory.getLogger(IOData.class);

    Completable start();

    Completable stop();

    default void cleanDataDir(String path) {
        File dataDir = new File(path);

        try {
            LOGGER.info("Deleting contents of data dir {}", path);
            if (dataDir.exists()) {
                FileUtils.cleanDirectory(dataDir);
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Failed to delete data dir: %s",
                    dataDir.getAbsolutePath()), e);
        }
    }

    default File createDataDir(String dataDirLoc) throws IOException {
        Path dataDirPath = FileSystems.getDefault().getPath(dataDirLoc);
        Files.createDirectories(dataDirPath);
        return dataDirPath.toFile();
    }

    default File setupDataDir(String path) throws IOException {
        cleanDataDir(path);
        return createDataDir(path);
    }

    interface Builder<T extends IOData, B extends Builder> extends BuilderInterface<T> {
        B setLayout(Layout layout);
    }
}
