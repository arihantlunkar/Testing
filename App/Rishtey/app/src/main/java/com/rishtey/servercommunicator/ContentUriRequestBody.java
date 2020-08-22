package com.rishtey.servercommunicator;

import android.content.ContentResolver;
import android.net.Uri;

import com.rishtey.listeners.ResponseListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class ContentUriRequestBody extends RequestBody {

    private ContentResolver mContentResolver;
    private Uri mUri;
    private int mCurrentFileNoUploading;
    private ResponseListener mResponseListener;

    ContentUriRequestBody(@NotNull ContentResolver contentResolver, @NotNull Uri uri, @NotNull ResponseListener responseListener, int currentFileNoUploading) {
        mContentResolver = contentResolver;
        mUri = uri;
        mResponseListener = responseListener;
        mCurrentFileNoUploading = currentFileNoUploading;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse(Objects.requireNonNull(mContentResolver.getType(mUri)));
    }

    @Override
    public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
        InputStream inputStream = Objects.requireNonNull(mContentResolver.openInputStream(mUri));
        try (Source source = Okio.source(inputStream)) {
            bufferedSink.writeAll(source);
        }
        mResponseListener.onFileUpload(mCurrentFileNoUploading);
    }
}
