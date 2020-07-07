package com.team4infinity.meetapp.rest_api;

import com.team4infinity.meetapp.models.NewEvent;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IService {

    @Headers({

            "Content-type: application/json"

    })
    @POST("new_event")
    Call<String> newEvent(@Body NewEvent event);
}
