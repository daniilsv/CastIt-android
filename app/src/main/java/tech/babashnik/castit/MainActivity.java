package tech.babashnik.castit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    static TextView trackView;
    static TextView authorView;
    private static MainActivity instance = null;
    public ImageButton bStart;
    public boolean isStarted = false;
    public Response response;
    Intent serviceIntent;
    View.OnClickListener controlButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isStarted) {
                startPlayerService();
                bStart.setImageResource(R.drawable.ic_pause_black_24dp);
                isStarted = !isStarted;
            } else {
                stopPlayerService();
                bStart.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                isStarted = !isStarted;
            }
        }
    };

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bStart = findViewById(R.id.start_btn);
        bStart.setOnClickListener(controlButtonListener);

        trackView = findViewById(R.id.track);
        authorView = findViewById(R.id.author);

    }

    public void stopPlayerService() {
        stopService(serviceIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        instance = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        instance = this;
    }

    public void startPlayerService() {
        serviceIntent = new Intent(MainActivity.this, PlayService.class);
        serviceIntent.setAction("start");
        startService(serviceIntent);
    }
}