package tech.babashnik.castit;

import android.util.Log;

import java.io.IOException;

import retrofit2.Response;

class TrackCheker extends Thread {
    private boolean thrWork;

    TrackCheker() {
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
        Response<TrackResponse> res = null;
        try {
            res = App.getApi().getData().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (res == null) return;
        TrackResponse r = res.body();
        if (r == null) return;

        final Track t;
        if (r.type.equals("mix") && Integer.parseInt(r.track.get(1).duration) > 5)
            t = r.track.get(1);
        else
            t = r.track.get(0);

        if (MainActivity.getInstance() != null)
            MainActivity.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setMainTexts(t.title);
                }
            });
    }

    private void setMainTexts(String title) {
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
