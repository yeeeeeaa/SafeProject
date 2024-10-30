package com.example.safeproject

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface SafeApi {
    @Multipart
    @POST("/save/image")
    fun sendImage(
        @Part imageFile: MultipartBody.Part
    ): Call<String>
    @GET("/res") // 서버에 GET 요청을 할 주소 입력
    fun getRes() : Call<String> // json 파일을 가져오는 메소드
}