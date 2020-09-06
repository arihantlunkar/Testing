package com.rishtey.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Utilities {

    public static String getRelativeName(@NonNull Context context, @NonNull Uri uri) {
        Cursor cursor = Objects.requireNonNull(context.getContentResolver().query(uri, null, null, null, null, null));
        final int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        final String name = cursor.getString(nameIndex);
        cursor.close();
        return name;
    }

    public static boolean isFileSizeValid(@NonNull Context context, @NonNull Uri uri, int expectedSize /*in MB*/) {
        Cursor cursor = Objects.requireNonNull(context.getContentResolver().query(uri, null, null, null, null, null));
        final int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
        cursor.moveToFirst();
        final long size = cursor.getLong(sizeIndex);
        cursor.close();
        return size <= expectedSize * 1024 * 1024;
    }

    public static String getRootURL(boolean isMultiFormData) {
        return "http://192.168.0.10:8081/Testing/Backend/rishtey/index.php?isMultiFormData=" + (isMultiFormData ? "yes" : "no");
    }

    public static String getUploadID() {
        return "26072020160926000000";
    }
}
