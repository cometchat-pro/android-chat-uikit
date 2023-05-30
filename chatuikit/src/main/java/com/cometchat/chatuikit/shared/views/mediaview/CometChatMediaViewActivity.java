package com.cometchat.chatuikit.shared.views.mediaview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.chatuikit.R;

import com.cometchat.chatuikit.shared.constants.UIKitConstants;

public class CometChatMediaViewActivity extends AppCompatActivity {

    private ImageView imageMessage;
    private VideoView videoMessage;
    private Toolbar toolbar;
    private String senderName;
    private long sentAt;
    private String mediaUrl;
    private String mediaType;

    private int mSize;
    private ImageView playBtn;
    private MediaPlayer mediaPlayer;
    private TextView mediaSize;
    private ProgressBar progressBar;

    private RelativeLayout audioMessage;
    private final String TAG = CometChatMediaViewActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cometchat_media_view);
        handleIntent();
        mediaPlayer = new MediaPlayer();
        toolbar = findViewById(R.id.toolbar);
        toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.textColorWhite));
        toolbar.setTitle("");
        imageMessage = findViewById(R.id.image_message);
        videoMessage = findViewById(R.id.video_message);
        progressBar = findViewById(R.id.progress_bar);
        audioMessage = findViewById(R.id.audio_message);
        mediaSize = findViewById(R.id.media_size_tv);
        playBtn = findViewById(R.id.playBtn);
        if (mediaType.equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
            Glide.with(this).asBitmap().load(mediaUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageMessage);
            imageMessage.setVisibility(View.VISIBLE);
        } else if (mediaType.equals(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
            progressBar.setVisibility(View.GONE);
            MediaController mediacontroller = new MediaController(this,true);
            mediacontroller.setAnchorView(videoMessage);
            videoMessage.setMediaController(mediacontroller);
            videoMessage.setVideoURI(Uri.parse(mediaUrl));
            videoMessage.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            videoMessage.setVisibility(View.VISIBLE);
        } else if (mediaType.equals(CometChatConstants.MESSAGE_TYPE_AUDIO)) {
            mediaPlayer.reset();
            playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mediaPlayer.setDataSource(mediaUrl);
                        mediaPlayer.prepare();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                playBtn.setImageResource(R.drawable.ic_play_2x);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                        playBtn.setImageResource(R.drawable.ic_pause_24dp);
                    } else {
                        mediaPlayer.pause();
                        playBtn.setImageResource(R.drawable.ic_play_2x);
                    }
                }
            });
            audioMessage.setVisibility(View.VISIBLE);
        }
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //handle Video layout in handScape mode
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if(mediaType.equals(CometChatConstants.MESSAGE_TYPE_VIDEO)){
                RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, R.id.toolbar);
                videoMessage.setLayoutParams(params);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    private void handleIntent() {
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.MEDIA_SIZE))
            mSize = getIntent().getIntExtra(UIKitConstants.IntentStrings.MEDIA_SIZE,0);
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.NAME))
            senderName = getIntent().getStringExtra(UIKitConstants.IntentStrings.NAME);
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.SENTAT))
            sentAt = getIntent().getLongExtra(UIKitConstants.IntentStrings.SENTAT,0);
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.INTENT_MEDIA_MESSAGE))
            mediaUrl = getIntent().getStringExtra(UIKitConstants.IntentStrings.INTENT_MEDIA_MESSAGE);
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE))
            mediaType = getIntent().getStringExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE);
    }
}