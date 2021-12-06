package com.ogray.glc.grav;

import com.ogray.glc.math.Pix;
import com.ogray.glc.math.Pixel;
import com.ogray.glc.math.Point;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Gravitational field
 */
@Slf4j
public class Field {
    public  Point size;

    @Setter @Getter
    Pixel[] data;

    public int outType; // 0 b/w image
    // 1 field

    public int baseLevel;

    public Field(Point sz) {
        log.info("creating Field "+sz);
        size = sz;
        data = null;
        init();
    }

    public boolean getOk(int x, int y) {
        if(!checkPoint(x,y))
            return false;

        return data[ (int)(x+y*size.getX()) ].ok;
    }

    private boolean checkPoint(int x, int y) {
        if(x>=size.getX() || y>=size.getY() || x<0 || y<0)
            return false;
        return true;
    }

    public boolean getFieldOk(int x, int y) {
        if(!checkPoint(x,y))
            return false;
        return data[(int)(x+y*size.x)].fieldOk;
    }

    public Point getField(int x, int y) {
        if(!checkPoint(x,y))
            return  new Point();
        return data[(int)(x+y*size.x)].field;
    }

    public void setField(int x, int y, Point val) {
        if(!checkPoint(x,y))
            return;
        Pixel cur = data[(int)(x+y*size.x)];
        cur.field = val;
        cur.fieldOk = true;
        data[(int)(x+y*size.x)] = cur;
    }

    public void setI(int x, int y, Pix val) {
        if(!checkPoint(x,y))
            return;
        Pixel cur = data[(int)(x+y*size.x)];
        cur.i = val;
//    cur.i_ok = true;
        if(val.i()>1) cur.light = true; else cur.light = false;
        data[(int)(x+y*size.x)] = cur;
    }

    public void clear() {
        int totalPixels = (int)(size.x*size.y);

        data = new Pixel[totalPixels];
        for(int i=0; i< totalPixels; i++)
            data[i] = new Pixel();
    }

    public boolean getDetOk(int i, int j) {
        return false;
    }

    public float getDet(int i, int j) {
        return 0;
    }

    public void setDet(int i, int j, double det) {
    }

    public byte[] getBrightnessData() {
        byte[] bright = new byte[ data.length ];

        for(int i=0; i<data.length; i++) {
            bright[i] = data[i].i.getGrey();
        }
        return bright;
    }

    public byte[][] getBrightnessData2d() {
        byte[][] bright = new byte[(int) size.x][ (int) size.y];

        for(int i=0; i<size.x; i++) {
            for(int j=0;j<size.y;j++) {
                bright[i][j] = data[(int)(i +j*size.x)].i.getGrey();
            }
        }
        return bright;
    }

    void init() {
        int totalPixels = (int)(size.x*size.y);
        log.info("field init "+totalPixels);

        clear();
        baseLevel = 100;
    }

    public Pixel get(int x, int y)
    {
        Pixel a = new Pixel();
        if(!checkPoint(x,y))
            return a;

        return data[(int)(x+y*size.x)];
    }

    public void setOk(int x, int y, boolean val) {
        if(!checkPoint(x,y))
            return;
        Pixel cur = data[(int)(x+y*size.x)];
        cur.ok = val;
        data[(int)(x+y*size.x)] = cur;
    }
}
