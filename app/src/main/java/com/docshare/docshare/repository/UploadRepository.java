package com.docshare.docshare.repository;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.docshare.docshare.util.CryptoUtil;
import com.docshare.docshare.worker.UploadWorker;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class UploadRepository {
    private static final String TAG = "UploadRepository";
    private final Context appContext;

    public UploadRepository(Context context) {
        this.appContext = context.getApplicationContext();
    }

    public UUID enqueueUpload(String requestId, String docId, Uri fileUri, String ownerId) {
        Data input = new Data.Builder()
                .putString(UploadWorker.KEY_REQUEST_ID, requestId)
                .putString(UploadWorker.KEY_DOC_ID, docId)
                .putString(UploadWorker.KEY_FILE_URI, fileUri != null ? fileUri.toString() : null)
                .putString(UploadWorker.KEY_OWNER_ID, ownerId)
                .build();

        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .setInputData(input)
                .build();

        WorkManager.getInstance(appContext).enqueue(work);

        // Temporary test: exercise CryptoUtil to verify encrypt/decrypt works
        try {
            String key = CryptoUtil.generateKey();
            CryptoUtil.CryptoResult res = CryptoUtil.encrypt("hello".getBytes(StandardCharsets.UTF_8), key);
            byte[] plain = CryptoUtil.decrypt(res.ciphertextBase64, key, res.ivBase64);
            Log.i(TAG, "CryptoUtil round-trip: ct=" + res.ciphertextBase64 + ", iv=" + res.ivBase64 + ", pt=" + new String(plain, StandardCharsets.UTF_8));
        } catch (Throwable t) {
            Log.e(TAG, "CryptoUtil test failed: " + t.getMessage());
        }

        return work.getId();
    }

    public LiveData<WorkInfo> observeUpload(UUID workId) {
        return WorkManager.getInstance(appContext).getWorkInfoByIdLiveData(workId);
    }
}
