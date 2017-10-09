package tech.babashnik.castit;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.ImageButton;
import android.widget.RemoteViews;


public class PlayService extends Service {
    Player player;
    Notification status;
    public ImageButton bStart;
    boolean isPause = false;
    public PlayService() {
        player = new Player();

    }

    public void onCreate() {
        super.onCreate();
        TrackCheker trackCheker = new TrackCheker();
        trackCheker.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals("start")) {
            showNotification(0);
            player.start("http://52.169.1.232:7373/ices", this);
            isPause = false;
        }
        if (intent.getAction().equals("pause_resume")) {
            if (!isPause) {
                showNotification(1);
                player.stop();
                isPause = true;
            } else {
                showNotification(0);
                player.start("http://52.169.1.232:7373/ices", this);
                isPause = false;
            }
        }
        if (intent.getAction().equals("exit")) {
            player.stop();
            stopForeground(true);
            stopSelf();
        }
        if (intent.getAction().equals("main")) {
            intent = new Intent(this, MainActivity.class);
            onStartCommand(intent, flags, startId);
        }

        return START_STICKY;
    }

    public void onDestroy() {
        player.stop();
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private void showNotification(int pos) {
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.status_bar);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction("main");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent playIntent = new Intent(this, PlayService.class);
        playIntent.setAction("pause_resume");
        PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);
        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        Intent closeIntent = new Intent(this, PlayService.class);
        closeIntent.setAction("exit");
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0, closeIntent, 0);
        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        if (pos == 0) {
            views.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
            status = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_pause_black_24dp)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .build();
        } else if(pos == 1){
            views.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
            status = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_play_arrow_black_24dp)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .build();
        }



        status.contentView = views;
        startForeground(101, status);
    }


}