package com.docshare.docshare.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import com.docshare.docshare.model.UploadResponse;

public interface RelayApi {
    @Multipart
    @POST("/upload-ephemeral")
    Call<UploadResponse> uploadEphemeral(
            @Part MultipartBody.Part file,
            @Part("meta") RequestBody meta
    );
}

