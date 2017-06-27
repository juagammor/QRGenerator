package com.inpro.qrgenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * QRSample Sample Code Using http://code.google.com/p/zxing/
 *
 * @author peiretti
 *
 */
public class TestQRCode {

    public static void main(String[] args) {

        TestQRCode qr = new TestQRCode();
        File f = new File("/home/juanra/tmp/qrCode.png");

        String text = "All you need is love, love. Love is all you need. Beatles";

        try {

            qr.generateQR(f, text, 300, 300);

            System.out.println("QRCode Generated: " + f.getAbsolutePath());
            // QRCode Generated: c:\tmp\qrCode.png

            String qrString = qr.decoder(f);

            System.out.println("Text QRCode: " + qrString);
            // Text QRCode: All you need is love, love. Love is all you need. Beatles

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public File generateQR(File file, String text, int h, int w) throws Exception {

        Charset charset = Charset.forName("ISO-8859-1");
        CharsetEncoder encoder = charset.newEncoder();
        byte[] b = null;
        ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(text));
        b = bbuf.array();
        String data = new String(b, "ISO-8859-1");
        // get a byte matrix for the data
        BitMatrix matrix = null;
        QRCodeWriter writer = new QRCodeWriter();
        matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, w, h);
        // matrix = generateVCardQRCode(null, "H");
        MatrixToImageWriter.writeToFile(matrix, "PNG", file);
        return file;

    }

    public String decoder(File file) throws Exception {

        FileInputStream inputStream = new FileInputStream(file);

        BufferedImage image = ImageIO.read(inputStream);

        // convert the image to a binary bitmap source
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        // decode the barcode
        QRCodeReader reader = new QRCodeReader();

        Result result = reader.decode(bitmap);

        return new String(result.getText().getBytes("UTF8"));

    }
}
