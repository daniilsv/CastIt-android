package tech.babashnik.castit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public Button bStart;
    public boolean isStarted = false;
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
    }

    private void stopPlayerService() {
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
        serviceIntent.setAction(String.valueOf(serviceIntent));
        startService(serviceIntent);
    }
}
