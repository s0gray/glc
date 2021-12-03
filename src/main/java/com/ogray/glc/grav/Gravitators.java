package com.ogray.glc.grav;

import com.ogray.glc.math.Star;
import lombok.Getter;
import lombok.Setter;

/**
 * Gravitators distribution
 */
public class Gravitators {
    @Setter @Getter
    int count;

    @Setter @Getter
    Star[] data;

    public Gravitators(int ng) {
        this.count = ng;
        data = new Star[ng];
    }

    public void setStar(int i, Star st) {
        data[i] = st;
    }
}
