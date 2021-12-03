package com.ogray.glc;

import com.ogray.glc.grav.GravitatorsGenerator;
import com.ogray.glc.grav.Map;
import com.ogray.glc.grav.Moments;
import com.ogray.glc.source.Source;
import lombok.Getter;
import lombok.Setter;

public class Manager {
    @Setter @Getter
    int mode;
    @Setter @Getter

    long steps;
    /*
    0 smooth
    1 rnd
    2 show 1
    */
    @Setter @Getter
    boolean saveAll;

    @Setter @Getter
    boolean samePic;
    @Setter @Getter
    int outType; // what on picture?  0 brighness 1 field 2 det
    @Setter @Getter
    int baseLevel; // for mag.pattern

    GravitatorsGenerator gen;
    Moments moments;

    Map map;
    Source source;
//    data *db;

    public Manager() {

    }
}
