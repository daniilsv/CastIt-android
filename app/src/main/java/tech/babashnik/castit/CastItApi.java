package tech.babashnik.castit;


import retrofit2.Call;
import retrofit2.http.GET;

interface CastItApi {

    @GET("get.track.current")
    Call<TrackResponse> getData();
}