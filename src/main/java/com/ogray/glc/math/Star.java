package com.ogray.glc.math;

import lombok.Getter;
import lombok.Setter;

public class Star {
    // coordinates
    @Setter @Getter
    Point r = new Point();
    // velocity
    @Setter @Getter
    Point v = new Point();
    // mass
    @Setter @Getter
    double mass;

    public Star() {
    }
}
