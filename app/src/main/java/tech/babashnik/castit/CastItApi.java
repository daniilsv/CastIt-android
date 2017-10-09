package tech.babashnik.castit;


import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by misha on 31.10.2016.
 */
public interface CastItApi {

    @GET("/get.track.current")
    Call<Responce> getData();
}