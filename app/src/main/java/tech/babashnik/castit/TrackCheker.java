package tech.babashnik.castit;

import android.util.Log;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by GE72 on 09.10.2017.
 */

public class TrackCheker extends Thread {
    boolean thrWork;

    public TrackCheker() {
        super();
    }

    @Override
    public void run() {
        super.run();


        thrWork = true;
        while (thrWork) {
            apiCheck();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void apiCheck() {
        Response<Responce> res = null;
        try {
            res = App.getApi().getData().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Responce r = res.body();

        if (r == null)
            return;
        if (MainActivity.getInstance() == null)
            return;
        MainActivity.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] split;
                Track t;
                switch (r.type) {
                    case "track":
                        t = r.track.get(0);
                        setMainTexts(t.title);
                        break;
                    case "mix":
                        t = r.track.get(1);
                        if (Integer.parseInt(t.duration) < 5)
                            t = r.track.get(0);
                        setMainTexts(t.title);
                        break;
                }
            }
        });
    }

    void setMainTexts(String title) {
        Log.e("MainTrack", title);
        if (title.contains(" - ")) {
            String[] split = title.split(" - ");
            MainActivity.authorView.setText(split[0]);
            MainActivity.trackView.setText(split[1]);
        } else {
            MainActivity.authorView.setText("");
            MainActivity.trackView.setText(title);
        }
    }
}
