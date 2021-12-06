package com.ogray.glc.source;

public class GausSource extends Source {
    public static final String NAME = "Gaussian";

    public GausSource(int size, int QSize) {
        super(size, SourceType.eGauss);
    }

    public byte value(int x, int y) {
        double r2=x*x+y*y;
        return (byte)( I * Math.exp(-r2/(QSize*QSize)));

    }
}


