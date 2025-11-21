package com.docshare.docshare.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.security.crypto.EncryptedFile;
import androidx.security.crypto.MasterKey;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class EncryptedFileManager {
    private static final String TAG = "EncryptedFileManager";

    public static byte[] readEncryptedFile(Context context, Uri uri) {
        try {
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                File file = new File(uri.getPath());
                if (file.exists()) {
                    MasterKey masterKey = new MasterKey.Builder(context)
                            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                            .build();
                    EncryptedFile encryptedFile = new EncryptedFile.Builder(context, file, masterKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB)
                            .build();
                    try (InputStream is = encryptedFile.openFileInput()) {
                        return readAll(is);
                    }
                }
            }
        } catch (Throwable t) {
            Log.w(TAG, "Encrypted read failed; falling back to raw bytes: " + t.getMessage());
        }

        // Fallback: read raw bytes from content resolver (MVP only)
        try (InputStream is = context.getContentResolver().openInputStream(uri)) {
            return readAll(is);
        } catch (Throwable t) {
            Log.e(TAG, "Failed to read file: " + t.getMessage());
            return new byte[0];
        }
    }

    private static byte[] readAll(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int r;
        while ((r = is.read(buf)) != -1) {
            bos.write(buf, 0, r);
        }
        return bos.toByteArray();
    }
}

