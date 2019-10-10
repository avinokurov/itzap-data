package com.itzap.data.api;

import com.itzap.common.Named;

public interface Listener extends Named {
    void handle(Event event);
}
