package com.ogray.glc;

import com.ogray.glc.grav.Field;
import com.ogray.glc.math.Res;

import java.io.IOException;

public class ConsoleApp {
    public static void main(String []args)  {
        //ManagerParams p = new ManagerParams();
        //p.setGenPar("gen.properties");

        Manager boss = new Manager(null);
        boss.init();
        boss.getMap().setParameter("sizePX", 1024);
        boss.getMap().setParameter("mode", 0);
        //boss.getSrc().setParameter("type", 4);

        boss.gen.setParam("m0", 0.1f);
        boss.getMap().setParameter("gamma", 0);
        boss.getMap().setParameter("sigma_c", 0);

        try {
            boss.getSrc().setJpeg("m31a.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        boss.refreshGravs();

        for(int i=0; i<1440; i++) {
            boss.gen.next();
            Res res = boss.render();

            byte[] jpg = boss.map.field.getJPG();
            try {
                Utils.writeFile("images/image_"+makeIdx(i,4)+".jpg", jpg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    static String makeIdx(int n, int Size)
    {
        String txt = "" + n;
        int delta = Size - txt.length();
        for(int i=0;i<delta;i++)
            txt = "0" + txt;

        return txt;
    }
}
