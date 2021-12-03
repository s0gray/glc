package com.ogray.glc.math;

public class Matrix2 {
    /*
   (0,0) (0,1)
   (1,0) (1,1)
*/
    public double a[][] = new double[2][2];
    public Matrix2() {
    }

    public Matrix2 minus(Matrix2 b) {
        Matrix2 c = new Matrix2();
        c.a[0][0] = a[0][0]-b.a[0][0];
        c.a[0][1] = a[0][1]-b.a[0][1];
        c.a[1][0] = a[1][0]-b.a[1][0];
        c.a[1][1] = a[1][1]-b.a[1][1];
        return c;
    }
}
