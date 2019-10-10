package com.itzap.data.file.impl;

import com.itzap.common.exception.IZapException;
import com.itzap.data.api.DataReader;
import com.itzap.data.api.DataSource;
import com.itzap.data.api.Field;
import com.itzap.data.api.IOData;
import com.itzap.data.api.Layout;
import com.itzap.data.file.exceptions.FileErrorCodes;
import com.itzap.data.file.model.FileSource;
import com.itzap.data.file.model.FlatArrayField;
import com.itzap.data.file.model.FlatField;
import com.itzap.data.file.model.FlatLayout;
import com.itzap.rxjava.command.RunnableCommand;
import io.reactivex.Completable;
import io.reactivex.Observable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.itzap.data.file.utils.FieldUtils.parseBin;
import static com.itzap.data.file.utils.UnitUtils.parseFileSize;

public class FlatFileDataReader implements DataReader {

    private final FlatLayout layout;
    private final FileSource dataSource;
    private final String bufferSize;

    private BufferedInputStream stream;

    private FlatFileDataReader(Builder builder) {
        this.layout = builder.layout;
        this.dataSource = builder.dataSource;
        this.bufferSize = builder.bufferSize;
    }

    @Override
    public Completable start() {
        return new RunnableCommand<Void>("cmd-start-flatFile") {
            @Override
            protected Void run() {
                try {
                    if (StringUtils.isNotBlank(bufferSize)) {
                        stream = new BufferedInputStream(new FileInputStream(dataSource.getFile()),
                                ((Number) parseFileSize(bufferSize)).intValue());
                    } else {
                        stream = new BufferedInputStream(new FileInputStream(dataSource.getFile()));
                    }
                } catch (FileNotFoundException e) {
                    throw new IZapException(String.format("File [%s] not found", dataSource.getFile().getAbsolutePath()),
                            FileErrorCodes.FILE_NOT_FOUND, e);
                }
                return null;
            }
        }.toCompletable();
    }

    @Override
    public Completable stop() {
        return new RunnableCommand<Void>("cmd-stop-flatFile") {
            @Override
            protected Void run() {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        throw new IZapException(String.format("File [%s] failed to close", dataSource.getFile().getAbsolutePath()),
                                FileErrorCodes.FILE_CLOSE, e);
                    }
                }
                return null;
            }
        }.toCompletable();
    }

    @Override
    public Observable<Pair[]> read() {
        return new RunnableCommand<Pair[]>("cmd-read-flatFile") {
            @Override
            protected Pair[] run() {
                if (stream == null) {
                    return null;
                }
                byte[] buffer = new byte[layout.getRecordLength()];
                try {
                    int bytesIn = stream.read(buffer);
                    if (bytesIn > 0) {
                        if (bytesIn != layout.getRecordLength()) {
                            // cannot read bytes that are not equal to the record len
                            throw new IZapException(String
                                    .format("File [%s] read error. Bytes read [%d] expected to eqaul to record length [%s]",
                                            dataSource.getFile().getAbsolutePath(), bytesIn, layout.getRecordLength()),
                                    FileErrorCodes.FILE_PARSE_ASSERT);
                        }
                        return parseBuffer(buffer);
                    }
                } catch (IOException e) {
                    throw new IZapException(String.format("File [%s] read error",
                            dataSource.getFile().getAbsolutePath()),
                            FileErrorCodes.FILE_READ, e);
                }

                return null;
            }
        }.toObservable();
    }

    private Pair[] parseBuffer(byte[] buffer) {
        Pair[] record = new Pair[layout.getFields().size()];
        MutableInt it = new MutableInt(0);

        layout.getFields()
                .forEach(f -> {
                    record[it.getAndIncrement()] = ImmutablePair.of(f, parseData(f, buffer));
                });

        return record;
    }

    private Object parseData(FlatField f, byte[] buffer) {
        if (f.getType() == Field.Type.TEXT) {
            return new String(buffer, f.getOffset(), f.getSize());
        } else if (f.getType() == Field.Type.BINARY) {
            return parseBin(f, buffer);
        } else if (f.getType() == Field.Type.ARRAY) {
            // array
            FlatArrayField arrayField = (FlatArrayField) f;
            Number[] array = new Number[arrayField.getLength()];
            for (int i = 0; i < arrayField.getLength(); ++i) {
                array[i] = parseBin(arrayField.getField(i), buffer);
            }

            return array;
        }

        throw new IZapException(String.format("Failed to convert data for field [%s]. Unknown field type", f),
                FileErrorCodes.FIELD_TYPE_CONVERSION);
    }

    public static class Builder implements IOData.Builder<FlatFileDataReader, Builder> {
        private FlatLayout layout;
        private FileSource dataSource;
        private String bufferSize;

        public Builder setBufferSize(String bufferSize) {
            this.bufferSize = bufferSize;
            return this;
        }

        @Override
        public Builder setLayout(Layout layout) {
            this.layout = (FlatLayout) layout;
            return this;
        }

        public Builder setDataSource(DataSource dataSource) {
            this.dataSource = (FileSource) dataSource;
            return this;
        }

        @Override
        public FlatFileDataReader build() {
            return new FlatFileDataReader(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
