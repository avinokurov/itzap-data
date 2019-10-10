package com.itzap.data.api;

import com.itzap.common.Named;

import java.util.List;

public interface Layout<T extends Field> extends Named {
    List<T> getFields();
}
