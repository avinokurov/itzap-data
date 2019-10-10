package com.itzap.data.api;

import io.reactivex.Observable;
import org.apache.commons.lang3.tuple.Pair;

public interface DataReader extends IOData {
    Observable<Pair[]> read();
}
