package org.moon.utils;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.moon.exception.ApplicationRunTimeException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * 二维码生成工具
 * @author:Gavin
 * @date 2015/6/1 0001
 */
public class QRCode {

    /**
     * 生成默认尺寸的二维码
     * @param content
     * @param out
     */
    public static void generateQRCode(String content, OutputStream out){
        generateQRCode(content, out, new Dimension(300,300));
    }

    /**
     * 生成二维码
     * @param content
     * @param out
     * @param dimension
     */
    public static void generateQRCode(String content, OutputStream out, Dimension dimension) {
        generateQRCodeWithImageAndText(content, null, null, out, dimension);
    }

    /**
     * 生成带图片的二维码
     * @param content
     * @param imagePath
     * @param out
     * @param dimension
     */
    public static void generateQRCodeWithImage(String content, String imagePath, OutputStream out, Dimension dimension) {
        generateQRCodeWithImageAndText(content, imagePath, null, out, dimension);
    }


    /**
     * 生成带文字的二维码
     * @param content
     * @param text
     * @param out
     * @param dimension
     */
    public static void generateQRCodeWithText(String content, String text, OutputStream out, Dimension dimension) {
        generateQRCodeWithImageAndText(content, null, text, out, dimension);
    }

    /**
     * 生成带文字和图片的二维码
     * @param content
     * @param imagePath
     * @param text
     * @param out
     * @param dimension
     */
    public static void generateQRCodeWithImageAndText(String content, String imagePath, String text, OutputStream out, Dimension dimension) {
        try {
            String format = "jpg";
            Hashtable<EncodeHintType,Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, dimension.getWidth(),
                    dimension.getHeight(), hints);
            MatrixToImageWriter.writeToStreamWithImageAndText(bitMatrix, format, imagePath, text, out);
        }catch (WriterException|IOException e){
            throw new ApplicationRunTimeException(e);
        }
    }
}
