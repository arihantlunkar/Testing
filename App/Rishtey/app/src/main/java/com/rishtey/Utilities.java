package com.rishtey;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.Objects;

public class Utilities {

    public static String getMimeType(@NonNull Context context, @NonNull Uri uri) {
        String extension;
        if (Objects.equals(uri.getScheme(), ContentResolver.SCHEME_CONTENT)) {
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(Objects.requireNonNull(uri.getPath()))).toString());
        }
        return extension;
    }

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

    public static String getRootURL() {
        return "http://192.168.0.10:8081/Testing/Backend/rishtey/index.php";
    }

    public static String getUploadID() {
        return "26072020160926000000";
    }
}
