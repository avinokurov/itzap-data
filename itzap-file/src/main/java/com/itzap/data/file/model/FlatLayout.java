package com.itzap.data.file.model;

import com.itzap.common.BuilderInterface;
import com.itzap.data.api.Layout;

import java.util.ArrayList;
import java.util.List;

public class FlatLayout implements Layout<FlatField> {
    private final String name;
    private final int recordLength;
    private final List<FlatField> flatFields;

    private FlatLayout(Builder builder) {
        this.name = builder.name;
        this.flatFields = builder.flatFields;
        this.recordLength = builder.recordLength;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getRecordLength() {
        return recordLength;
    }

    @Override
    public List<FlatField> getFields() {
        return flatFields;
    }

    public static class Builder implements BuilderInterface<FlatLayout> {
        private String name;
        private int recordLength;
        private List<FlatField> flatFields;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setFlatFields(List<FlatField> flatFields) {
            this.flatFields = flatFields;
            return this;
        }

        public Builder setRecordLength(int recordLength) {
            this.recordLength = recordLength;
            return this;
        }

        @Override
        public FlatLayout build() {
            boolean hasOffset = this.flatFields.stream().anyMatch(f -> f.getOffset() > 0);

            if (!hasOffset) {
                List<FlatField> newFields = new ArrayList<>(this.flatFields.size());

                int offSet = 0;
                for (FlatField field : this.flatFields) {
                    newFields.add(FlatField.from(field)
                            .setOffset(offSet)
                            .build());
                    offSet += field.getSize();
                }

                this.flatFields = newFields;
            }

            if (recordLength == 0) {
                this.flatFields.forEach(f -> recordLength += f.getSize());
            }

            return new FlatLayout(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
