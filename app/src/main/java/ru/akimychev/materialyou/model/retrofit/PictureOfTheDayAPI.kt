package ru.akimychev.materialyou.model.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.akimychev.materialyou.model.dto.PODServerResponseData

interface PictureOfTheDayAPI {
    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String):
            Call<PODServerResponseData>
}