package com.iqbalhasan.foodrecomm.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.iqbalhasan.foodrecomm.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import static com.iqbalhasan.foodrecomm.Config.Config.GOOGLE_API_KEY;

public class DetailTutorialActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private String VIDEO_ID;

    private YouTubePlayerView mYoutubePlayerView = null;

    private TextView judulTutorial;
    private TextView namaChannel;
    private TextView published;
    private TextView deskripsi;
    private TextView failMsg;

    private Bundle bundleGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tutorial);

        mYoutubePlayerView = findViewById(R.id.youtube_player);
        mYoutubePlayerView.initialize(GOOGLE_API_KEY, this);

        bundleGet = getIntent().getExtras();

        judulTutorial = findViewById(R.id.judul_tutorial);
        namaChannel = findViewById(R.id.nama_channel);
        published = findViewById(R.id.published);
        deskripsi = findViewById(R.id.deskripsi);
        failMsg = findViewById(R.id.fail_msg);

        setView();
    }

    private void setView() {
        VIDEO_ID = bundleGet.getString("id");

        judulTutorial.setText(bundleGet.getString("title"));
        namaChannel.setText(bundleGet.getString("channel"));
        published.setText(bundleGet.getString("publishedAt"));
        deskripsi.setText(bundleGet.getString("deskripsi"));
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);

        mYoutubePlayerView.setVisibility(View.VISIBLE);
        failMsg.setVisibility(View.GONE);

        if (!b) {
            youTubePlayer.cueVideo(VIDEO_ID);
        }
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        mYoutubePlayerView.setVisibility(View.GONE);
        failMsg.setVisibility(View.VISIBLE);
    }
}
