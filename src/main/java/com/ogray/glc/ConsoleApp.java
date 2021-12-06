package com.ogray.glc;

import java.io.IOException;

public class ConsoleApp {
    public static void main(String []args) {
        ManagerParams p = new ManagerParams();
        p.setGenPar("gen.properties");

        Manager boss = new Manager(p);
        boss.render();

        int sourceSize = 256;
        byte[][] raw = boss.map.field.getBrightnessData2d();

        int[][] rgb = Utils.makeGreyRGB(raw, sourceSize, sourceSize);
        byte[] jpg;
        try {
            jpg = Utils.rawToJpeg(rgb, sourceSize, sourceSize);

            Utils.writeFile("image.jpg", jpg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
