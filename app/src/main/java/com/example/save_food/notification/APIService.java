package com.example.save_food.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Authorization: Bearer ya29.a0AfB_byAUAihFF_5NYdgmawpESzmd0MBhW9oDNDuGAayJ41fLtBdZlIRk74jehddbf0YrLVzXqE5IzRAFV5aqUcUffkZKAWS5OUGah_RlFxdXVpCzF-pPnAg46TWRhc2LW0PqWzlYkPhbBTnjc-F-baOOxUahaCgYKATgSARASFQHsvYlsSanQ1DFk_oYIfA-3BO6FZw0163",
            "Content-Type:application/json"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
