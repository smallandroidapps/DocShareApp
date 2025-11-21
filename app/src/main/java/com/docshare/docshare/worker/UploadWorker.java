package com.docshare.docshare.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UploadWorker extends Worker {
    public static final String TAG = "UploadWorker";
    public static final String KEY_REQUEST_ID = "requestId";
    public static final String KEY_DOC_ID = "docId";
    public static final String KEY_FILE_URI = "fileUri";
    public static final String KEY_OWNER_ID = "ownerId";

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data input = getInputData();
        String requestId = input.getString(KEY_REQUEST_ID);
        String docId = input.getString(KEY_DOC_ID);
        String fileUri = input.getString(KEY_FILE_URI);
        String ownerId = input.getString(KEY_OWNER_ID);

        Log.i(TAG, "Executing UploadWorker with input:" +
                " requestId=" + requestId +
                ", docId=" + docId +
                ", fileUri=" + fileUri +
                ", ownerId=" + ownerId);

        // Skeleton only; actual upload logic will come later.
        return Result.success();
    }
}

