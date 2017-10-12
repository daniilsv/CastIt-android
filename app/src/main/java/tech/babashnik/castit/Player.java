package tech.babashnik.castit;

import android.content.Context;
import android.media.AudioManager;
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


class Player implements ExoPlayer.Listener {
    private final Context mContext;
    private AudioManager am;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener;
    private ExoPlayer exoPlayer;
    private TrackRenderer audioRenderer;
    private Uri mUri;
    private int lastKnownAudioFocusState;
    private boolean audioFocusGranted;

    Player(Context context, String url) {
        mContext = context;

        mUri = Uri.parse(url);
        initAudioManager();
    }

    private void initAudioManager() {
        am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {

            @Override
            public void onAudioFocusChange(int focusChange) {
                audioFocusGranted = false;
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        audioFocusGranted = true;
                        if (lastKnownAudioFocusState == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
                            setVolume(1f);
                        else
                            start();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        stop();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        stop();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        setVolume(0.3f);
                        break;
                }
                lastKnownAudioFocusState = focusChange;
            }
        };
    }

    private void requestAudioFocus() {
        int result = am.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            audioFocusGranted = true;
        } else if (result == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
            audioFocusGranted = false;
        }
    }

    void stop() {
        if (exoPlayer != null) {
            exoPlayer.seekTo(0);
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop();
        }
    }

    private void setVolume(float volume) {
        if (exoPlayer != null) {
            exoPlayer.sendMessage(audioRenderer, MediaCodecAudioTrackRenderer.MSG_SET_VOLUME, volume);
        }
    }

    void start() {
        if (exoPlayer != null) {
            exoPlayer.stop();
        }
        requestAudioFocus();
        if (!audioFocusGranted)
            return;

        String userAgent = Util.getUserAgent(mContext, "ExoPlayerDemo");

        DataSource dataSource = new DefaultHttpDataSource(userAgent, null, null);
        Allocator allocator = new DefaultAllocator(64 * 1024);
        ExtractorSampleSource extractorSampleSource = new ExtractorSampleSource(mUri, dataSource, allocator, 64 * 1024 * 256, new Mp3Extractor());

        audioRenderer = new MediaCodecAudioTrackRenderer(extractorSampleSource, null, true);
        exoPlayer = ExoPlayer.Factory.newInstance(1);
        exoPlayer.prepare(audioRenderer);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(this);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayWhenReadyCommitted() {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }
}