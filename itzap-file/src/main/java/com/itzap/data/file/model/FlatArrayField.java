package com.itzap.data.file.model;

import com.itzap.data.api.ArrayField;

public interface FlatArrayField extends FlatField, ArrayField {
    FlatField getField(int i);
}
