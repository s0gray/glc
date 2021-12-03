package com.ogray.glc.grav;

import com.ogray.glc.math.Star;
import lombok.Getter;
import lombok.Setter;

/**
 * Moments of gravitational field
 */
public class Moments {
    double m1x,m1y;
    double m2xx,m2xy,m2yx,m2yy;

    double m3xxx,m3xxy,m3xyx,m3xyy;
    double m3yxx,m3yxy,m3yyx,m3yyy;

    double m4xxxx,m4xxxy,m4xxyx,m4xxyy;
    double m4xyxx,m4xyxy,m4xyyx,m4xyyy;
    double m4yxxx,m4yxxy,m4yxyx,m4yxyy;
    double m4yyxx,m4yyxy,m4yyyx,m4yyyy;

    boolean done;
    GravitatorsGenerator gen;
    Gravitators grv;
    Foot ins = null;

    @Setter @Getter
    double Ro;
    @Setter @Getter
    int mode;
    @Setter @Getter
    int max_mom;

    public Moments(GravitatorsGenerator gen) {
        done = false;
        this.gen = gen;
        this.grv = gen.getGravs();
    }


    public void calculate() {
      if(done && gen.getMode()==GravitatorsGenerator.MODE_NO_MOTION)
          return;

       if(ins==null)
           ins =new Foot(grv.count,1024);
       ins.clear();

        m1x=0; m1y=0;
        m2xx=0; m2xy=0; m2yx=0; m2yy=0;
        m3xxx=0; m3xxy=0; m3xyx=0; m3xyy=0;
        m3yxx=0; m3yxy=0; m3yyx=0; m3yyy=0;

        m4xxxx=0; m4xxxy=0; m4xxyx=0; m4xxyy=0;
        m4xyxx=0; m4xyxy=0; m4xyyx=0; m4xyyy=0;
        m4yxxx=0; m4yxxy=0; m4yxyx=0; m4yxyy=0;
        m4yyxx=0; m4yyxy=0; m4yyyx=0; m4yyyy=0;

        for(int i=0; i<grv.getCount(); i++) {
          Star g = grv.getData()[i];
          double r2 = g.getR().mod();
          double r = Math.sqrt(r2);
          if(r>Ro)
          {
            m1x -= g.getR().getX()*g.getMass() / r2;
            m1y -= g.getR().getY()*g.getMass() / r2;

           double xy = g.getMass()*(-2*g.getR().getX()*g.getR().getY()/(r2*r2));
           m2xx += g.getMass()*(1/r2 - 2*g.getR().getX()*g.getR().getX()/(r2*r2));
           m2yy += g.getMass()*(1/r2 - 2*g.getR().getY()*g.getR().getY()/(r2*r2));
           m2xy += xy;
           m2yx += xy;

           double xxy = g.getMass()*(2*g.getR().getY()/(r2*r2)-
                   8*g.getR().getX()*g.getR().getX()*g.getR().getY()/(r2*r2*r2) );
           m3xxx += g.getMass()*(2*(g.getR().getX()+g.getR().getX()+g.getR().getX())/(r2*r2)
                   - 8*g.getR().getX()*g.getR().getX()*g.getR().getX()/(r2*r2*r2) );
           m3xxy += xxy;
           m3xyx += xxy;
           m3xyy += g.getMass()*(2*(g.getR().getX())/(r2*r2)
                   - 8*g.getR().getX()*g.getR().getY()*g.getR().getY()/(r2*r2*r2) );

           double yyx = g.getMass()*(2*(g.getR().getX())/(r2*r2)
                   - 8*g.getR().getY()*g.getR().getY()*g.getR().getX()/(r2*r2*r2));

           m3yxx += g.getMass()*(2*(g.getR().getY())/(r2*r2)
                   - 8*g.getR().getY()*g.getR().getX()*g.getR().getX()/(r2*r2*r2) );
           m3yxy += yyx;
           m3yyx += yyx;
           m3yyy += g.getMass()*(2*(g.getR().getY()+g.getR().getY()
                   +g.getR().getY())/(r2*r2) - 8*g.getR().getY()*g.getR().getY()*g.getR().getY()/(r2*r2*r2) );

     /*      double xxxy = g.m*(8/(r2*r2*r2)*(3*g.r.x*g.r.y) - 48/(r2*r2*r2*r2)*(g.r.x*g.r.x*g.r.x*g.r.y));
           double xxyy = g.m*(-2/(r2*r2) + 8/(r2*r2*r2)*(g.r.x*g.r.x+g.r.y*g.r.y) - 48/(r2*r2*r2*r2)*(g.r.x*g.r.x*g.r.y*g.r.y));
           double xyxy = g.m*(-2/(r2*r2) + 8/(r2*r2*r2)*(g.r.x*g.r.x+g.r.y*g.r.y) - 48/(r2*r2*r2*r2)*(g.r.x*g.r.x*g.r.y*g.r.y));
           double xyyy = g.m*(8/(r2*r2*r2)*(3*g.r.x*g.r.y) - 48/(r2*r2*r2*r2)*(g.r.x*g.r.y*g.r.y*g.r.y));
           m4xxxx = g.m*(-2*3/(r2*r2) + 8/(r2*r2*r2)*(6*g.r.x*g.r.x) - 48/(r2*r2*r2*r2)*(g.r.x*g.r.x*g.r.x*g.r.x));
           m4xxxy = xxxy;
           m4xxyx = xxxy;
           m4xxyy = xxyy;
           m4xyxx = xxxy;
           m4xyxy = xyxy;
           m4xyyx = xyxy;
           m4xyyy = xyyy;

           m4yxxx = xxxy;
           m4yxxy = xyxy;
           m4yxyx = xyxy;
           m4yxyy = xyyy;
           m4yyxx = xxyy;
           m4yyxy = xyyy;
           m4yyyx = xyyy;
           m4yyyy = g.m*(-2*3/(r2*r2) + 8/(r2*r2*r2)*(6*g.r.y*g.r.y) - 48/(r2*r2*r2*r2)*(g.r.y*g.r.y*g.r.y*g.r.y));
*/
           if(mode==1 || mode==3) ins.add(i);
           // in these modes external gravs are in calcs separately
          } else
          { // inside region
            if(mode!=1 && mode!=2)  // in 1 and 2 internal part is not in calcs
               ins.add(i);
          }
         }
    //   ins.shrink();  // if you need more RAM and more troubles uncomment this line

      //  log.info("inside: %ld outside: %ld\n", ins->items, grv->count - ins->items);
        done = true;
  }

}
