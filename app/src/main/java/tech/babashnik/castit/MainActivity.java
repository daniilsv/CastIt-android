package tech.babashnik.castit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public Button bStart;
    public boolean isStarted = false;
    public Response response;
    Intent serviceIntent;
    View.OnClickListener controlButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isStarted) {
                startPlayerService();
                isStarted = !isStarted;
            } else {
                stopPlayerService();
                isStarted = !isStarted;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bStart = findViewById(R.id.start_btn);
        bStart.setOnClickListener(controlButtonListener);

        final TextView trackView = findViewById(R.id.track);
        final TextView authorView = findViewById(R.id.author);


        App.getApi().getData().enqueue(new Callback<Responce>() {
            @Override
            public void onResponse(Call<Responce> call, Response<Responce> response) {
                Responce r = response.body();
                if (r == null)
                    return;
                String[] split;
                switch (r.type) {
                    case "track":
                        Track t = r.track.get(0);
                        split = t.title.split(" - ");
                        Log.e("MainTrack", split.toString());
                        authorView.setText(split[0]);
                        trackView.setText(split[1]);
                        break;
                    case "mix":
                        Track t2 = r.track.get(1);
                        split = t2.title.split(" - ");
                        Log.e("MainMix", split.toString());
                        authorView.setText(split[0]);
                        trackView.setText(split[1]);
                        break;
                }
                //TODO:
            }

            @Override
            public void onFailure(Call<Responce> call, Throwable t) {

            }
        });


    }

    public void stopPlayerService() {
        stopService(serviceIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void startPlayerService() {
        serviceIntent = new Intent(MainActivity.this, PlayService.class);
        serviceIntent.setAction("start");
        startService(serviceIntent);
    }
}
