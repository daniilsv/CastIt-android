package tech.babashnik.castit;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static CastItApi castItApi;

    public static CastItApi getApi() {
        return castItApi;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://52.169.1.232/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        castItApi = retrofit.create(CastItApi.class);
    }
}
