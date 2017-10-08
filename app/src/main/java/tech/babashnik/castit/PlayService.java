package tech.babashnik.castit;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class PlayService extends Service {
    Player player;
    Notification status;

    public PlayService() {
        player = new Player();

    }

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start("http://52.169.1.232:7373/ices", this);
        Log.e("PlayService", "vse zbs");
        showNotification(0);
        return super.onStartCommand(intent, flags, startId);
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
        notificationIntent.setAction("tech.babashnik.castit.action.main");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent playIntent = new Intent(this, PlayService.class);
        playIntent.setAction("tech.babashnik.castit.action.play");
        PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);
        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        Intent closeIntent = new Intent(this, PlayService.class);
        closeIntent.setAction("tech.babashnik.castit.action.stop");
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0, closeIntent, 0);
        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        if (pos == 0) {
            views.setImageViewResource(R.id.status_bar_play, R.drawable.pause_ntf);
        }


        status = new Notification.Builder(this)
                .setSmallIcon(R.drawable.radio)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
        status.contentView = views;
        startForeground(101, status);
    }


}