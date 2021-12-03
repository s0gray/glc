package com.ogray.glc.source;


import lombok.Getter;
import lombok.Setter;

public abstract class Source {
    @Setter
    @Getter
    int width;
    @Setter
    @Getter
    int height;

    @Setter
    @Getter
    byte[][] data;

    int QSize;

    @Setter
    @Getter
    int I = 255;

    public Source(int size, int QSize) {
        this.width = size;
        this.height = size;
        this.data = new byte[width][height];
        this.QSize = QSize;
    }
    public void generate() {
        int x0 = width/2;
        int y0 = height/2;

        for(int i=0; i<width; i++) {
            for(int j=0;j<height; j++) {
                int x = i - x0;
                int y = j - y0;
                data[i][j] = value(x, y);
            }
        }
    }

    public abstract byte value(int x, int y);
}
