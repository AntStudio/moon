package org.moon.utils;

import com.google.zxing.common.BitMatrix;
import org.moon.exception.ApplicationRunTimeException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 二维码生成辅助类
 * @author:Gavin
 * @date 2015/6/1 0001
 */
public class MatrixToImageWriter {

    //二维码额颜色
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private MatrixToImageWriter() {}


    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }


    public static void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }


    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        Graphics2D gs = image.createGraphics();

        //载入logo
        Image img = ImageIO.read(new File("D://logo.jpg"));
        gs.drawImage(img, 125, 125, null);
        gs.dispose();
        img.flush();
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }


    /**
     * 生成带图片的二维码
     * @param matrix
     * @param format
     * @param imagePath
     * @param stream
     * @throws IOException
     */
    public static void writeToStreamWithImage(BitMatrix matrix, String format, String imagePath, OutputStream stream)
            throws IOException {
        writeToStreamWithImageAndText(matrix, format, imagePath, null, stream);
    }

    /**
     * 生成带文字信息的二维码
     * @param matrix
     * @param format
     * @param text
     * @param stream
     * @throws IOException
     */
    public static void writeToStreamWithText(BitMatrix matrix, String format, String text, OutputStream stream)
            throws IOException {
        writeToStreamWithImageAndText(matrix, format, text, null, stream);
    }

    /**
     * 生成带图片和文字的二维码
     * @param matrix
     * @param format
     * @param imagePath
     * @param stream
     * @throws IOException
     */
    public static void writeToStreamWithImageAndText(BitMatrix matrix, String format, String imagePath, String text,
                                                     OutputStream stream)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        Graphics2D gs = image.createGraphics();
        int width = image.getWidth(), height = image.getHeight();

        //载入图片
        if(Objects.nonNull(imagePath)) {
            BufferedImage img = ImageIO.read(new File(imagePath));
            int imageWidth = img.getWidth(), imageHeight = img.getHeight();
            if (imageWidth >= width || imageHeight >= height) {
                throw new ApplicationRunTimeException("Image {width:" + imageWidth + ", height:" + imageHeight
                        + "} not suitable for QR_Code which size {width:" + width + ", height:" + height + "}");
            }
            gs.drawImage(img, width / 2 - imageWidth / 2, height / 2 - imageHeight / 2, null);
            img.flush();
        }

        //载入文字 （默认为黑色16号，目前不支持自定义）
        if(Objects.nonNull(text)){
            gs.setColor(Color.black);
            gs.setFont(new Font(null, Font.PLAIN, 16));
            int fontSize = gs.getFont().getSize(),
                textWidth = text.length()*fontSize,
                textHeight = fontSize;
            //y的起始位置为 height / 2 + textHeight / 2, 因为文字是从左下角而不是左上角开始绘制
            gs.drawString(text, width / 2 - textWidth / 2, height / 2 + textHeight / 2);
        }

        gs.dispose();

        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }
}
