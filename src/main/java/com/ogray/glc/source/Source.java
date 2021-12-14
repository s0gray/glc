package com.ogray.glc.source;

import com.ogray.glc.Utils;
import com.ogray.glc.math.Pix;
import com.ogray.glc.math.Point;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Source {
    public class SourcePar {
        public double size;
        public double Io;
        public SourceType type;
        public Point r;
        public Point v;

        public double power;
        public Pix color = new Pix();

    }

    public SourcePar par = new SourcePar();

 /*   @Setter
    @Getter
    int width;
    @Setter
    @Getter
    int height;
  */

    @Setter @Getter
    Point sizePX = new Point();
    private Point REpx = new Point();

    @Setter
    @Getter
    Pix[][] bitmap;

    @Setter
    @Getter
    int I = 255;

    public Source() {
        set_default_values();
    }

    public Source(double size, SourceType type)
    {
        set_default_values();
        par.size = size;
        par.type = type;
    }

    /**
     * Get source value pixel
     * @param r in RE coordinates
     * @return
     */
    public Pix value(Point r) {
        Pix a = new Pix();
        double I=0;

        r.minus( par.r );
        if(par.size==0 && (r.x!=0 || r.y!=0) )
            return a;

        if(r.x==0 && r.y==0) {
            I = par.Io;
        }
        else
        {
            double r2 = r.x*r.x+r.y*r.y;
            double s2 = par.size*par.size;
            double sz = r2/s2;
            switch(par.type)
            {
                case eFlat: // flat
                    if(r2<s2) I = par.Io;
                    else      I = 0;
                    break;
                case eGauss: // gauss
                    I = par.Io*Math.exp(-sz);
                    break;
                case ePow: // pow
                    I = par.Io*Math.exp(-Math.pow(sz,par.power/2));
                    break;
                case eLimb: // limb
                    if(sz<=1.5) I=par.Io*Math.sqrt(1-2*sz/3);
                    else I=0;
                    break;
                case eDisk: // disk
                    if(sz<1 || sz>4) I=0;
                    else I=par.Io*Math.pow(sz,-1.5);
                    break;
                case eBitmap: {
                    Point px = Utils.toPX(r, sizePX, this.REpx);
                    if (px.x < 0 || px.y < 0 || px.x > this.sizePX.x || px.y > this.sizePX.y)
                        return a;
                    return bitmap[(int) px.x][(int) px.y];
                }
            }
        }
        a.r = (int)I & par.color.r;
        a.g = (int)I & par.color.g;
        a.b = (int)I & par.color.b;
        return a;
    }
    public void set_default_values()
    {
        par.size = 3;
        par.Io = 250;
        par.type = SourceType.eGauss;
        par.r = new Point(0,0);
        par.v = new Point(0,0);
        par.power = 2;
        par.color.r = 250;
        par.color.g = 250;
        par.color.b = 255;
    }
    public void refresh(double t)
    {
        par.r.x += par.v.x*t;
        par.r.y += par.v.y*t;
    }

    public boolean loadPar(String srcPar) {
        return false;
    }

    public boolean setParameter(String key, float val)
    {
        switch (key) {
            case "pos_x": {
                par.r.x = val;
                return true;
            }
            case "pos_y": {
                par.r.y = val;
                return true;
            }
            case "v_x": {
                par.v.x = val;
                return true;
            }
            case "v_y": {
                par.v.y = val;
                return true;
            }
            case "size": {
                par.size = val;
                return true;
            }
            case "type": {
                par.type = SourceType.values ()[(int)val];
                return true;
            }
            case "brightness": {
                par.Io = val;
                return true;
            }
            case "power": {
                par.power = val;
                return true;
            }
            case "color_r": {
                par.color.r = (int)val;
                return true;
            }
            case "color_g": {
                par.color.g = (int)val;
                return true;
            }
            case "color_b": {
                par.color.b = (int)val;
                return true;
            }
        }
        return false;
    }

    public void setJpeg(String fileName) throws IOException {
        BufferedImage img = ImageIO.read(new File(fileName));

        this.sizePX.x = img.getWidth();
        this.sizePX.y = img.getHeight();
        this.par.type = SourceType.eBitmap;

        bitmap = new Pix[(int)this.sizePX.x][(int)this.sizePX.y];
        for(int i=0;i<(int)this.sizePX.x;i++) {
            for(int j=0;j<(int)this.sizePX.y; j++) {
                bitmap[i][j] = new Pix( img.getRGB(i,j) );
            }
        }
    }

    /**
     * Set image size in RE
     * @param re
     */
    public void setRE(Point re) {
        this.REpx = sizePX.div(re);
    }
}
