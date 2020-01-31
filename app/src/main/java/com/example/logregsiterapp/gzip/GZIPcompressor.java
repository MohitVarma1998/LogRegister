package com.example.logregsiterapp.gzip;

import android.util.Base64;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPcompressor {

    private static FileInputStream fileInputStream = null;
    private static FileOutputStream fileOutputStream = null;
    private static GZIPOutputStream gzipOutputStream = null;

    public static final String TAG = GZIPcompressor.class.getSimpleName();

    public static void compressorFromFile(FileInputStream InputStream, FileOutputStream OutputStream) {
        try {
            fileInputStream = InputStream;
            fileOutputStream = OutputStream;
            gzipOutputStream = new GZIPOutputStream(fileOutputStream);
            byte[] buffer = new byte[2048];
            int len;
            while ((len = fileInputStream.read()) != -1) {
                gzipOutputStream.write(buffer, 0, len);
            }
            Log.d(TAG, "compressor: output file generated");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
                gzipOutputStream.finish();
                gzipOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void decompressFromFile(FileInputStream filein, FileOutputStream fileout) {
        try {
            FileInputStream fis = filein;
            GZIPInputStream gis = new GZIPInputStream(fis);
            FileOutputStream fos = fileout;
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            //close resources
            fos.close();
            gis.close();
            Log.d(TAG, "decompressFromFile: done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static String compressfromBase64TOString(String base64) {
//        try {
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            Base64OutputStream base64OutputStream = new Base64OutputStream();
//            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(base64OutputStream);
//            gzipOutputStream.write(base64.getBytes());
//            gzipOutputStream.close();
//            String output = byteArrayOutputStream.toString("UTF-8");
//            byte[] bytes = byteArrayOutputStream.toByteArray();
//            String base_46 = Base64.encodeToString(bytes, Base64.DEFAULT);
//            Log.d(TAG, "compressfromBase64TOString: ");
//            return output;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static byte[] compressfromBase64TOByteArray(String base64) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(base64.getBytes());
            gzipOutputStream.close();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            Log.d(TAG, "compressfromBase64TOByteArray: ");
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String compressString(String srctxt) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gzipOutputStream.write(srctxt.getBytes());
        IOUtils.closeQuietly(gzipOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String result = Base64.encodeToString(bytes, Base64.DEFAULT);
        return result;
    }

    public static String decompressString(String compressedString) throws Exception {
        byte[] bytes = Base64.decode(compressedString, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
        String result = IOUtils.toString(gzipInputStream, "UTF-8");
        return result;
    }

    public static String decompress(byte[] bytes) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream, "UTF-8"));
            String string;
            StringBuilder stringBuilder = new StringBuilder();
            while ((string = bufferedReader.readLine()) != null) {
                stringBuilder.append(string);
            }
            Log.d(TAG, "decompress: ");
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
