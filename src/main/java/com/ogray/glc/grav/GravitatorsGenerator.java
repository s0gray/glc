package com.ogray.glc.grav;

import com.ogray.glc.math.Point;
import com.ogray.glc.math.Star;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

/**
 * Generator of Gravitators
 */
public class GravitatorsGenerator {

    @Setter @Getter
    int seed;       // seed for RND generator

    @Setter @Getter
    Point size;     // size of area in Re

    public static final int SQUARE_TYPE = 0;
    public static final int POLAR_TYPE = 1;
    public static final int CIRCLE_TYPE = 2;
    public static final int ELLIPTICAL_TYPE = 3;
    @Setter @Getter
    int areaType;

    @Setter @Getter
    int ng;     // number of stars

    @Setter @Getter
    boolean jump;            // jump in motion ?

    public static final int MASS_TYPE_SAME = 0;
    public static final int MASS_TYPE_SALPETER = 1;

    @Setter @Getter
    int massType;

    @Setter @Getter
    float massMin;

    @Setter @Getter
    float massMax;

    @Setter @Getter
    float massPower;

    @Setter @Getter
    double m0;            // mass of 1 star

    @Setter @Getter
    double v;             // speed

    @Setter @Getter
    double vAngle;

    @Setter @Getter
    boolean checkSame;

    public static final int MODE_N_BODY = 0;
    public static final int MODE_ONE_STAR = 1;
    public static final int MODE_DOUBLE_STAR = 2;
    public static final int MODE_NO_MOTION = 3;

    @Setter @Getter
    int mode;

    @Setter @Getter
    float dbRo; // for double R

    @Setter @Getter
    float dbFi; // for double fi0

    @Setter @Getter
    float dbT; // for double w

    public GravitatorsGenerator() {
    }

    /**
     * Generate gravitators
     * @return Gravitators
     */
    public Gravitators generate() {
        rnd.setSeed( this.seed);
        Gravitators grv = new Gravitators(ng);

  // double totalMass = 0;

   for(int i=0; i<ng; i++)
   {
    Star st = new Star();
     switch(massType)
     {
      case MASS_TYPE_SAME:
             st.setMass(m0);
             break;
      case MASS_TYPE_SALPETER:
            // st.m = getSalpeter(par.mass_min, par.mass_max, par.mass_power);
             break;
     }
    //  totalMass += st.getMass();

      st.setR(getNewGravPos());
      st.getV().setX( v * Math.cos(vAngle*Math.PI/180) );
      st.getV().setY( v * Math.sin(vAngle*Math.PI/180 ));
      grv.setStar(i, st);
   }

    return grv;
    }

    Point getNewGravPos() {
        Point s = new Point(size.getX() / 2, size.getY() / 2);
        Point a = new Point();
        double r, phi;

        switch (areaType) {
            case SQUARE_TYPE:
                a.setX(size.getX() * RND() - s.getX());
                a.setY( size.getY() * RND() - s.getY() );
                break;
            case POLAR_TYPE:
                phi = RND() * 2 * Math.PI;
                r = RND() * s.getX();
                a.setX( r * Math.cos(phi) );
                a.setY( r * Math.sin(phi) );
                break;
            case CIRCLE_TYPE:
            case ELLIPTICAL_TYPE:
                do {
                    a.setX( size.getX() * RND() - s.getX());
                    a.setY( size.getY() * RND() - s.getY());
                } while (!isInside(a));
                break;
        }
        return a;
    }

    Random rnd = new Random();
    double RND() {
        return rnd.nextDouble();
    }

    boolean isInside(Point r)
    {
        double a = Math.sqrt(r.getX()*r.getX() + r.getY()*r.getY());
        Point s = new Point(size.getX()/2,size.getY()/2);
        double b;
        switch(areaType)
        {
            case SQUARE_TYPE:
                if(r.getX()>-size.getX()/2 && r.getY()>-size.getY()/2
                        && r.getX()<size.getX()/2 && r.getY()<size.getY()/2)
                    return true;
                else
                    return false;
            case POLAR_TYPE:
            case CIRCLE_TYPE:
                if(a<s.getX())
                    return true;
                else
                    return false;
            case ELLIPTICAL_TYPE:
                b = r.getX()*r.getX() / (s.getX()*s.getX())+r.getY()*r.getY()/(s.getY()*s.getY());
                if( b<1 )
                    return true;
                else
                    return false;
        }
        return false;
    }
}
