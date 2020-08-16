package com.rishtey;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Utilities {

    public static String getStringImage(Context context, Uri uri) throws IOException {
        byte[] imageBytes;
        if (getMimeType(context, uri).equalsIgnoreCase("pdf")) {
            imageBytes = getBytes(Objects.requireNonNull(context.getContentResolver().openInputStream(uri)));
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } else {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            getBitmap(context, uri).compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            imageBytes = byteArrayOutputStream.toByteArray();
        }
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;
        if (Objects.equals(uri.getScheme(), ContentResolver.SCHEME_CONTENT)) {
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(Objects.requireNonNull(uri.getPath()))).toString());
        }
        return extension;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNullOrEmpty(Uri uri) {
        return uri == null || isNullOrEmpty(uri.toString());
    }

    public static String getRelativeName(Context context, Uri uri) throws Exception {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null, null);
        if (null == cursor) {
            throw new NullPointerException();
        }
        final boolean isValidName = cursor.moveToFirst();
        if (!isValidName) {
            throw new Exception("File name is corrupt!");
        }
        final String name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        cursor.close();
        return name;
    }

    private static Bitmap getBitmap(Context context, Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
    }

    private static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        final int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
