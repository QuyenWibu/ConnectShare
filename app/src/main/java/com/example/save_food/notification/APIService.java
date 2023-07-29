package com.example.save_food.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAALLyDTbU:APA91bExgWVQ3dLY1EazRB9ZAHkXy7jbCbDeiljjrIlsEvhXC4NzPWmZGSKxbRsdjNZI2KJRJabzvDLG6PyNAEdbth12nhdTy5p8zjN3vjoet6A3mdY_2xPeW3HYSWVKdijHdgZlXEOI"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
