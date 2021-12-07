package com.ogray.glc;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Persist {
    private static Persist instance = null;
    public static Persist getInstance() {
        if(instance==null) {
            instance = new Persist();
        }
        return instance;
    }

    @Getter
    private int sourceType = 1;

    @Setter @Getter
    private int sourceSize = 3;

    public void setSourceType(int value) {
        log.info("Persist.setSourceType = "+value);
        this.sourceType = value;
    }
}
