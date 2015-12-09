package org.moon.utils;

import org.moon.exception.ApplicationRunTimeException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * 图片工具类
 * @author:Gavin
 * @date 2015/3/21 0021
 */
public class Images {
    /**
     * 压缩图片到文件
     * @param originImage
     * @param quality
     * @param file
     * @throws java.io.IOException
     */
    public static void compressImageToFile(BufferedImage originImage,float quality,File file) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter imageWriter = writers.next();
        ImageWriteParam param = imageWriter.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);
        imageWriter.setOutput(ImageIO.createImageOutputStream(new FileInputStream(file)));
        imageWriter.write(null,new IIOImage(originImage,null,null),param);
    }

    /**
     * 压缩图片
     * @param originImage
     * @param quality
     * @return
     * @throws IOException
     */
    public static BufferedImage compressImage(BufferedImage originImage,float quality) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter imageWriter = writers.next();
        ImageWriteParam param = imageWriter.getDefaultWriteParam();

        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageOutputStream imageOut = ImageIO.createImageOutputStream(out);
        imageWriter.setOutput(imageOut);
        imageWriter.write(null,new IIOImage(originImage,null,null),param);
        imageOut.flush();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        BufferedImage image = ImageIO.read(in);
        out.close();
        imageOut.close();
        in.close();

        return image;
    }


    /**
     * 将图片转换为正方形尺寸
     * @param originImage
     * @return
     */
    public static BufferedImage toSquare(BufferedImage originImage){
        int width = originImage.getWidth(),height = originImage.getHeight(),
                x = 0 , y = 0;
        //如果为奇数，则左边或者上边少一像素,以保证不会画到边界去
        if(width>height){//宽度更大，以高度为准
            x = (width-height)/2;
            width = height;
        }else{
            y = (height-width)/2;
            height = width;
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(originImage, 0, 0, width, height, x, y, width, height, null);
        return image;
    }

    /**
     * 剪裁图像
     * @param originImage
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage clipImage(BufferedImage originImage ,int x, int y, int width , int height){
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(originImage,0,0,width,height,x,y,width,height,null);
        return image;
    }

    /**
     * 缩放图片,注意：高度和宽度不能同时小于等于0
     * @param originImage
     * @param width 最大宽度，小于等于0则忽略,根据高度计算缩放
     * @param height 最大高度，小于等于0则忽略,根据宽度计算缩放
     * @param ratio 是否等比，如果为true,则width意义为maxWidth,height意义为maxHeight
     * @param allowBigger 是否允许比原图放大
     * @return
     */
    public static BufferedImage scaleImage(BufferedImage originImage , int width , int height, boolean ratio, boolean allowBigger){
        if(ratio){
            int originWidth = originImage.getWidth(),
                    originHeight = originImage.getHeight();
            if(!allowBigger){//不允许放大
                width = width > originWidth ? originWidth : width;
                height = height > originHeight ? originHeight : height;
            }

            if(width > 0 && height > 0 ) {//如果有合法的长宽值
                double widthScale = (double) originWidth / (double) width, heightScale = (double) originHeight / (double) height;
                if (widthScale > heightScale) {//宽度缩放比例更大，以宽度为基准
                    height = (int) (originHeight / widthScale);
                } else {
                    width = (int) (originWidth / heightScale);
                }
            }else if(width <= 0 && height <= 0){
                throw new ApplicationRunTimeException("The maximum-scaled width and maximum-scaled height for image must not both less than zero");
            }else if(width > 0){//高度不合法，根据宽度缩放
                double widthScale = (double) originWidth / (double) width;
                height = (int) (originHeight / widthScale);
            }else if(height > 0){//宽度不合法，根据高度缩放
                double heightScale = (double) originHeight / (double) height;
                width = (int) (originWidth / heightScale);
            }
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.createGraphics().drawImage(originImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        return image;
    }
}
