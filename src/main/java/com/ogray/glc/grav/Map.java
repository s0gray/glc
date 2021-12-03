package com.ogray.glc.grav;

import com.ogray.glc.math.Complex;
import com.ogray.glc.math.Matrix2;
import com.ogray.glc.math.Point;
import com.ogray.glc.source.Source;
import lombok.Getter;
import lombok.Setter;

public class Map {

    @Setter @Getter
    Point sizePX; // pixels
    @Setter @Getter
    Point sizeRE; // RE

    @Setter @Getter
    double minR2; // minimum dist to star
    @Setter @Getter
    double gamma;
    @Setter @Getter
    double sigmaC;
    @Setter @Getter
    double angleGamma;

    @Setter @Getter
    boolean direct; // no grav

    public final static int MAP_MODE_FFC = 0; // full fill code
    public final static int MAP_MODE_HFC = 1; // hierarfical
    public final static int MAP_MODE_SSD = 2; //

    @Setter @Getter
    int mode;
    @Setter @Getter
    int mode2; // HFC .x
    //  0 usual
    // 1 triangle

    @Setter @Getter
    int HFCStartLevel;

    @Setter @Getter
    double HFCeps; // max I grad

    @Setter @Getter
    double SSD_src_inc;  // source increment size for SSD2

    @Setter @Getter
    boolean fastV;

    @Setter @Getter
    int itterMode; // 0 classical 1 Zhdanov

    @Setter @Getter
    boolean calcDet;

    @Setter @Getter
    int nItter; //steps

    @Setter @Getter
    Moments moments;

    @Setter @Getter
    Source source;

    @Setter @Getter
    Field field = null;

    private Matrix2 A = new Matrix2(), E = new Matrix2(), A0 = new Matrix2();
    private Complex AA = new Complex();

    Point REpx = null;


    public Map(Moments moments, Source source) {
        this.moments = moments;
        this.source = source;
        setDefaultValues();
    }

    void setDefaultValues()
    {
        sizePX = new Point(256, 256);
        sizeRE = new Point(15, 15);

        mode = 1;
        minR2 = 1e-12;

        gamma = 0;
        sigmaC = 0;
        angleGamma = 0;
        calcMatrixA();

        direct = false;

        HFCStartLevel = 3;
        HFCeps = 0.1;
        SSD_src_inc = 0.7;

        mode2 = 0;

        fastV = false;
        itterMode = 0;
        calcDet = false;
        nItter = 10;

     //   p2 = null;
      //  mt = null;
    }

    void calcMatrixA()
    {
        double K= Math.PI/180;
        double g1 = gamma*Math.cos(2*angleGamma*K);
        double g2 = gamma*Math.sin(2*angleGamma*K);

        A.a[0][0] = 1-g1-sigmaC;
        A.a[0][1] = g2;
        A.a[1][0] = g2;
        A.a[1][1] = 1+g1-sigmaC;

        E.a[0][0] = 1;
        E.a[0][1] = 0;
        E.a[1][0] = 0;
        E.a[1][1] = 1;

        AA.a = g1;
        AA.b = -g2;

        A0 = E.minus(A);
    }

    public void init() {
        this.field = new Field(sizePX);
        this.REpx = sizePX.div(sizeRE);
    }
}
