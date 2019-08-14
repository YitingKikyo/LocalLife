package com.example.suenaa.finalproject.ui.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by 婷 on 2018/1/7.
 */
public class BitmapUtil {
    public static byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
