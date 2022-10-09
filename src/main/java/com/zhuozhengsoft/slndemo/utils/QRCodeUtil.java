package com.zhuozhengsoft.slndemo.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: dong
 * @Date: 2022/7/19 9:55
 * @Version 1.0
 */
public class QRCodeUtil {
    private String codeText;//二维码内容
    private BarcodeFormat barcodeFormat;//二维码类型
    private int width;//图片宽度
    private int height;//图片高度
    private String imageformat;//图片格式
    private int backColorRGB;//背景色，颜色RGB的数值既可以用十进制表示，也可以用十六进制表示
    private int codeColorRGB;//二维码颜色
    private ErrorCorrectionLevel errorCorrectionLevel;//二维码纠错能力
    private String encodeType;

    public QRCodeUtil() {
        codeText = "www.zhuozhengsoft.com";
        barcodeFormat = BarcodeFormat.PDF_417;
        width = 400;
        height = 400;
        imageformat = "png";
        backColorRGB = 0xFFFFFFFF;
        codeColorRGB = 0xFF000000;
        errorCorrectionLevel = ErrorCorrectionLevel.H;
        encodeType = "UTF-8";
    }
    public QRCodeUtil(String text) {
        codeText = text;
        barcodeFormat = BarcodeFormat.PDF_417;
        width = 400;
        height = 400;
        imageformat = "png";
        backColorRGB = 0xFFFFFFFF;
        codeColorRGB = 0xFF000000;
        errorCorrectionLevel = ErrorCorrectionLevel.H;
        encodeType = "UTF-8";
    }

    public String getCodeText() {
        return codeText;
    }

    public void setCodeText(String codeText) {
        this.codeText = codeText;
    }

    public BarcodeFormat getBarcodeFormat() {
        return barcodeFormat;
    }

    public void setBarcodeFormat(BarcodeFormat barcodeFormat) {
        this.barcodeFormat = barcodeFormat;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImageformat() {
        return imageformat;
    }

    public void setImageformat(String imageformat) {
        this.imageformat = imageformat;
    }

    public int getBackColorRGB() {
        return backColorRGB;
    }

    public void setBackColorRGB(int backColorRGB) {
        this.backColorRGB = backColorRGB;
    }

    public int getCodeColorRGB() {
        return codeColorRGB;
    }

    public void setCodeColorRGB(int codeColorRGB) {
        this.codeColorRGB = codeColorRGB;
    }

    public ErrorCorrectionLevel getErrorCorrectionLevel() {
        return errorCorrectionLevel;
    }

    public void setErrorCorrectionLevel(ErrorCorrectionLevel errorCorrectionLevel) {
        this.errorCorrectionLevel = errorCorrectionLevel;
    }

    private BufferedImage toBufferedImage(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? this.codeColorRGB: this.backColorRGB);
            }
        }
        return image;
    }

    private byte[] writeToBytes(BitMatrix bitMatrix)
            throws IOException {

        try {
            BufferedImage bufferedimage = toBufferedImage(bitMatrix);

            //将图片保存到临时路径中
            File file = java.io.File.createTempFile("~pic","."+ this.imageformat);
            //System.out.println("临时图片路径："+file.getPath());
            ImageIO.write(bufferedimage,this.imageformat,file);

            //获取图片转换成的二进制数组
            FileInputStream fis = new FileInputStream(file);
            int fileSize = fis.available();
            byte[] imageBytes = new byte[fileSize];
            fis.read(imageBytes);
            fis.close();

            //删除临时文件
            if (file.exists()) {
                file.delete();
            }

            return imageBytes;
        } catch (Exception e) {
            System.out.println(" Image err :" + e.getMessage());
            return null;
        }

    }


    //获取二维码图片的字节数组
    public byte[] getQRCodeBytes()
            throws IOException {

        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

            //设置二维码参数
            Map hints = new HashMap();
            if (this.errorCorrectionLevel != null) {
                //设置二维码的纠错级别
                hints.put(EncodeHintType.ERROR_CORRECTION, this.errorCorrectionLevel);
            }

            if (this.encodeType!=null && this.encodeType.trim().length() > 0) {
                //设置编码方式
                hints.put(EncodeHintType.CHARACTER_SET, this.encodeType);
            }

            BitMatrix bitMatrix = multiFormatWriter.encode(this.codeText, BarcodeFormat.QR_CODE, this.width, this.height, hints);
            byte[] bytes = writeToBytes(bitMatrix);

            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
