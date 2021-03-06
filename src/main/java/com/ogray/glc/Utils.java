package com.ogray.glc;

import com.ogray.glc.math.Pix;
import com.ogray.glc.math.Point;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Utils {
    public static void rawToJpegFile(byte[] bytes, File outputFile) throws IOException{
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
            ImageIO.write(img, "jpg", outputFile);
    }

    public static byte[] rawToJpeg(int data[][], int width, int height) throws IOException {
        BufferedImage img = createImageFromRaw(data, width, height);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", out);

        return out.toByteArray();
    }

    public static BufferedImage createImageFromRaw(int data[][], int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                img.setRGB(i, j, data[i][j]);
            }
        }
        return img;
    }

    public static int convertRGB(byte r, byte g, byte b) {
        // combine to RGB format
        int rgb = ((r & 0xFF) << 16) |
                ((g & 0xFF) <<  8) |
                ((b & 0xFF)      ) |
                0xFF000000;
        return rgb;
    }

    public static byte getR(int rgb) {
        return (byte)( (rgb >> 16) & 0x000000FF);
    }
    public static byte getG(int rgb) {
        return (byte)( (rgb >> 8) & 0x000000FF);
    }
    public static byte getB(int rgb) {
        return (byte)( (rgb) & 0x000000FF);
    }

    public static int convertRGB(Pix p) {
        if(p==null) return 0;

       return convertRGB((byte)p.r, (byte)p.g, (byte)p.b);
    }

    public static int[][] combineRGB(byte[][] r, byte[][] g, byte[][] b, int width, int heigh) {
        int [][]rgb = new int[width][heigh];
        for(int i=0; i<width; i++) {
            for(int j=0;j<heigh; j++) {
                rgb[i][j] = convertRGB(r[i][j], g[i][j], b[i][j]);
            }
        }
        return rgb;
    }

    public static int[][] makeGreyRGB(byte[][] data, int width, int height) {
        int [][]rgb = new int[width][height];
        for(int i=0; i<width; i++) {
            for(int j=0;j<height; j++) {
                rgb[i][j] = convertRGB(data[i][j], data[i][j], data[i][j]);
            }
        }
        return rgb;
    }

    public static void writeFile(String file, byte[] data) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * Convert point coordinate from pixels to RE (Einstein radius)
     * @param a
     * @return
     */
    public static Point toRE(Point a, Point sizePX, Point REpx) {
        Point r = new Point(a.getX()-sizePX.getX()/2, a.getY()-sizePX.getY()/2);
        r.divide(REpx); // in RE
        return r;
    }

    /**
     * Convert point coordinate from RE (Einstein radius) to pixels
     * @param a
     * @return
     */
    public static Point toPX(Point a, Point sizePX,  Point REpx)
    { // from RE to PX
        a.mulMe(REpx);
        Point r = new Point(a.x+sizePX.x/2, a.y+sizePX.y/2);
        return r;
    }
}
