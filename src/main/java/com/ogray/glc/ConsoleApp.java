package com.ogray.glc;

import com.ogray.glc.grav.Field;
import com.ogray.glc.math.Res;

import java.io.IOException;

public class ConsoleApp {
    public static void main(String []args) {
        //ManagerParams p = new ManagerParams();
        //p.setGenPar("gen.properties");

        Manager boss = new Manager(null);
       // boss.getSrc().setParameter("type", 1);

        boss.init();
       // boss.map.field.setOutType(Field.OutImageType.eMagnification);

      //  boss.getGen().grv.log();
        Res res = boss.render();

        byte[] jpg = boss.map.field.getJPG();
        try {
            Utils.writeFile("image.jpg", jpg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
