package com.example.logregsiterapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.logregsiterapp.R;
import com.google.android.material.button.MaterialButton;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ExtraActivity extends AppCompatActivity {

    public static final String TAG = ExtraActivity.class.getSimpleName();
    public static final int RESULT = 1;

    FileOutputStream fileOutputStream = null;
    FileOutputStream newGeneratedfile = null;
    OutputStreamWriter writer = null;

    Toolbar toolbar;
    ImageView bitmapfororiginalbase64String, bitmapfordecodebase64String;
    TextView bytearray, base64string;

    private MaterialButton mMakeSerializable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        toolbar = (Toolbar) findViewById(R.id.CustomToolBar);
        bitmapfororiginalbase64String = (ImageView) findViewById(R.id.base64);
        bitmapfordecodebase64String = (ImageView) findViewById(R.id.compressed);
        setSupportActionBar(toolbar);


        mMakeSerializable = (MaterialButton) findViewById(R.id.picture_gallary);
        bytearray = (TextView) findViewById(R.id.bytearray);
        base64string = (TextView) findViewById(R.id.base64string);

        mMakeSerializable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ExtraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    startActivityforGallary();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityforGallary();
                }
        }
    }

    public void startActivityforGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setAction("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, this.getResources().getString(R.string.selectOption)), RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {

                        File file = new File("/sdcard/Repnet/Mohit");
                        file.mkdirs();

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                        Bitmap resizedBitmap = getResizedBitmap(bitmap, 500);

                        bitmapfororiginalbase64String.setImageBitmap(resizedBitmap);

                        byte[] bytesFromBitmap = getBytesFromBitmap(resizedBitmap);

                        try {
                            //encode byte to base64 string
                            String generatedBase64Stringfrombytes = Base64.encodeToString(bytesFromBitmap, Base64.NO_WRAP);

                            //compress base64 string to byte array
                            byte[] compressedBase64StringtoByte = compress(generatedBase64Stringfrombytes);

                            //decompress byte array to string
                            String decompressed = decompress(compressedBase64StringtoByte);


                            Log.d(TAG, decompressed);

                            //compress base64 string to string
                            String compressedBase64StringtoString = compressnew(generatedBase64Stringfrombytes);
                            String nnn = compressString(generatedBase64Stringfrombytes);
                            Log.d(TAG, nnn.length() + "");

                            //decode decompressed string into original base64 string
                            byte[] decodedBase64BitmapImage = Base64.decode(decompressed, Base64.DEFAULT);

                            //bitmap from decode string
                            Bitmap bitmapfromdecodeString = BitmapFactory.decodeByteArray(decodedBase64BitmapImage, 0, decodedBase64BitmapImage.length);

                            bitmapfordecodebase64String.setImageBitmap(bitmapfromdecodeString);


                            bytearray.setText(this.getResources().getString(R.string.bytearray) + bytesFromBitmap);

                            Log.d(TAG, generatedBase64Stringfrombytes);
                            Log.d(TAG, "====================");
                            Log.d(TAG, decompressed);
//                            base64string.setText(this.getResources().getString(R.string.compressed) + new String(compressedBase64StringtoByte).length() + this.getResources().getString(R.string.decompressed) + decompressed.length() + "");
                            base64string.setText(this.getResources().getString(R.string.compressed) + compressedBase64StringtoByte.length + this.getResources().getString(R.string.decompressed) + decompressed.length() + "");

                            Log.d(TAG, generatedBase64Stringfrombytes.length() + "");
                            Log.d(TAG, "====================");
                            Log.d(TAG, compressedBase64StringtoString.length() + "");
                            createbyteIntofile(compressedBase64StringtoByte, this.openFileOutput("data", MODE_APPEND));
                        } catch (Exception e) {
                            Log.d(TAG, "onActivityResult: " + e.getMessage());
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "onActivityResult: 3fail");
                }
            } else {
                Log.d(TAG, "onActivityResult: 2fail");
            }
        } else {
            Log.d(TAG, "onActivityResult: 1fail");
        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public byte[] getBytesFromBitmapjpg(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public byte[] getBytesFromBitmappng(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
        return stream.toByteArray();
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void createbyteIntofile(byte[] bytes, FileOutputStream fileOutputStream) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        FileOutputStream fileOutputStream1 = fileOutputStream;
        byte[] bytes1 = new byte[2048];
        int len;
        while ((len = byteArrayInputStream.read(bytes1)) != -1) {
            fileOutputStream1.write(bytes1, 0, len);
        }
    }


    public static byte[] compress(String data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
        GZIPOutputStream gzip = new GZIPOutputStream(bos);
        gzip.write(data.getBytes());
        gzip.close();
        byte[] compressed = bos.toByteArray();
        bos.close();
        return compressed;
    }

    public static String decompress(byte[] compressed) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(bis);
        BufferedReader br = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        gis.close();
        bis.close();
        return sb.toString();
    }


    public static String compressnew(String str) throws IOException {

        byte[] blockcopy = ByteBuffer
                .allocate(4)
                .order(java.nio.ByteOrder.LITTLE_ENDIAN)
                .putInt(str.length())
                .array();
        ByteArrayOutputStream os = new ByteArrayOutputStream(str.length());
        GZIPOutputStream gos = new GZIPOutputStream(os);
        gos.write(str.getBytes());
        gos.close();
        os.close();
        byte[] compressedhere = new byte[4 + os.toByteArray().length];
        System.arraycopy(blockcopy, 0, compressedhere, 0, 4);
        System.arraycopy(os.toByteArray(), 0, compressedhere, 4,
                os.toByteArray().length);
        return Base64.encodeToString(compressedhere, Base64.NO_WRAP);
    }

    public static String compressString(String srcTxt) throws IOException {
        ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
        GZIPOutputStream zos = new GZIPOutputStream(rstBao);
        zos.write(srcTxt.getBytes());
        IOUtils.closeQuietly(zos);

        byte[] bytes = rstBao.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    public String getStringFromByteArray(byte[] settingsData) {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(settingsData);
        Reader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
        StringBuilder sb = new StringBuilder();
        int byteChar;

        try {
            while ((byteChar = reader.read()) != -1) {
                sb.append((char) byteChar);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();

    }
}
