package com.ogray.glc.source;

public class FlatSource extends Source {
    public static final String NAME = "Flat";

    public FlatSource(int size, int QSize) {
        super(size, SourceType.eFlat);
    }

    public byte value(int x, int y) {
        double r2=x*x+y*y;
        if( r2 < (QSize*QSize) )
            return (byte)I;
        else
            return 0;
    }
}
