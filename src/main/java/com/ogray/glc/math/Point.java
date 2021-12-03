package com.ogray.glc.math;

import lombok.Getter;
import lombok.Setter;

public class Point {
    @Setter @Getter
    double x;

    @Setter @Getter
    double y;
    /*
        double mod();
        double mul(_point b);       // z-component of vector multiplying
        double mul_sc(_point b);    // scalaar mult
        double L(_point b);  // length of difference
        double l(); // length


        _point operator +(_point);
        _point operator -(_point);

        _point operator +=(_point);
        _point operator -=(_point);

        _point operator *(_point);
        _point operator /(_point);

        _point operator *=(_point);
        _point operator /=(_point);

        _point operator*(double );
        _point operator/(double );

        _point operator*=(double );
        _point operator/=(double );

         double phi();
     */
    public Point() {
        this.x = 0;
        this.y = 0;
    }
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public Point(double r, double phi, int type)
    { // polar s.k init
        switch(type)
        {
            case 1:
                x = r * Math.cos(phi* Math.PI/180);
                y = r * Math.sin(phi * Math.PI/180);
                break;
            default:
                x = 0;
                y = 0;
        }
    }
}
