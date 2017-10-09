package tech.babashnik.castit;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer.util.Util;


public class Player implements ExoPlayer.Listener {
    ExoPlayer exoPlayer;
    TrackRenderer audioRenderer;

    public void stop() {
        if (exoPlayer != null) {
            exoPlayer.seekTo(0);
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop();
        }
    }

    public void setVolume(float volume) {
        if (exoPlayer != null) {
            exoPlayer.sendMessage(audioRenderer, MediaCodecAudioTrackRenderer.MSG_SET_VOLUME, volume);
        }
    }

    public void start(String URL, Context context) {
        if (exoPlayer != null) {
            exoPlayer.stop();
        }
        Uri URI = Uri.parse(URL);

        String userAgent = Util.getUserAgent(context, "ExoPlayerDemo");

        DataSource dataSource = new DefaultHttpDataSource(userAgent, null, null);
        Allocator allocator = new DefaultAllocator(64 * 1024);
        ExtractorSampleSource extractorSampleSource = new ExtractorSampleSource(URI, dataSource, allocator, 64 * 1024 * 256, new Mp3Extractor());

        audioRenderer = new MediaCodecAudioTrackRenderer(extractorSampleSource, null, true);
        exoPlayer = ExoPlayer.Factory.newInstance(1);
        exoPlayer.prepare(audioRenderer);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(this);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == 4) {
            //OK
        }
    }

    @Override
    public void onPlayWhenReadyCommitted() {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }
}