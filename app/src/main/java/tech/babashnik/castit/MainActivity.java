package tech.babashnik.castit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {
    public boolean isStarted = false;
    public ImageButton bStart;
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
                isStarted = !isStarted;
                bStart.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bStart = findViewById(R.id.start_btn);
        bStart.setOnClickListener(controlButtonListener);
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
