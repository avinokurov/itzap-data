package com.itzap.data.file.model;

public class FlatArrayFieldImpl extends FlatFieldImpl implements FlatArrayField {
    private final int length;
    private final FlatField[] fields;

    FlatArrayFieldImpl(Builder builder) {
        super(builder);

        this.length = builder.length;
        this.fields = new FlatField[builder.length];
        int offset = this.getOffset();

        for (int i = 0; i < this.length; ++i) {
            this.fields[i] = FlatField.builder()
                    .setName(this.getName() + "[" + i + "]")
                    .setOffset(offset)
                    .setSize(this.getSize())
                    .build();
            offset += this.getSize();
        }
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public FlatField getField(int i) {
        return this.fields[i];
    }
}
