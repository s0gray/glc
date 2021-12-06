package com.ogray.glc;

import java.io.IOException;

public class ConsoleApp {
    public static void main(String []args) {
        //ManagerParams p = new ManagerParams();
        //p.setGenPar("gen.properties");

        Manager boss = new Manager(null);
       // boss.getSrc().setParameter("type", 1);
        boss.init();

        boss.render();

        byte[] jpg = boss.map.field.getJPG();
        try {
            Utils.writeFile("image.jpg", jpg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
