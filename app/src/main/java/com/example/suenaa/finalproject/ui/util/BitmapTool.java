package com.example.suenaa.finalproject.ui.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by user on 2018/1/2.
 */

public class BitmapTool {
    public static byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
