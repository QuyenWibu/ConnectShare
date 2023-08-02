package com.example.save_food.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization: key=AAAALLyDTbU:APA91bFbDR_hahSqDCp_9pzI3QtoJySnM5aKj2-LsddkrGioOKtVsxjAVf42gyhwO7r811wWeae9_2hwsKCNIStWxY1Q0mIPXbvDzzcZjm9mTJObsZHFY1fVPm9yiIs3QtP8x-omPTox"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
