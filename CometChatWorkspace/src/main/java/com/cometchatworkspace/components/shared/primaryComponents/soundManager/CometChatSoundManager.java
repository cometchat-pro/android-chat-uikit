package com.cometchatworkspace.components.shared.primaryComponents.soundManager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import com.cometchatworkspace.R;

import com.cometchatworkspace.resources.utils.Utils;

/**
 * CometChatAudioHelper class is used to manage the audio tone and ringtone for incoming and outgoing
 * calls.
 *
 * Created at: 29th March 2020
 *
 * Modified at 29th March 2020
 */
public class CometChatSoundManager {
    private static final String TAG = "CometChatSoundManager";

    private final Context context;

    private final IncomingAudioManager incomingAudioHelper;

    private final OutgoingAudioManager outgoingAudioHelper;
    private final Vibrator vibrator;

    private final SoundPool soundPool;
    private static final long[] VIBRATE_PATTERN = {0, 1000,1000};

    private final int  disconnectedSoundId;


    public CometChatSoundManager(Context context) {
        this.context = context;
        this.incomingAudioHelper=new IncomingAudioManager(context);
        this.outgoingAudioHelper=new OutgoingAudioManager(context);
        this.soundPool=new SoundPool(1,AudioManager.STREAM_VOICE_CALL,0);
        this.disconnectedSoundId=this.soundPool.load(context, R.raw.beep2,1);
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

//        initAudio();
    }

    private void initAudio(){
        AudioManager audioManager = Utils.getAudioManager(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            audioManager.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
        } else {
            audioManager.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        }
    }

    public void play(Sound sound) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.isMusicActive()){
            vibrator.vibrate(VIBRATE_PATTERN,2);
        }
        if (sound.equals(Sound.incomingCall)) {
            startIncomingAudio(Uri.parse("android.resource://" + context.getPackageName() + "/" +
                    Sound.incomingCall.getRawFile()),true);
        } else if (sound.equals(Sound.outgoingCall)) {
            startOutgoingAudio(OutgoingAudioManager.Type.IN_COMMUNICATION,
                    Sound.outgoingCall.getRawFile());
        } else if (sound.equals(Sound.incomingMessage)) {
            playMessageSound(context,Sound.incomingMessage.getRawFile());
        } else if (sound.equals(Sound.outgoingMessage)) {
            playMessageSound(context,Sound.outgoingMessage.getRawFile());
        } else if (sound.equals(Sound.incomingMessageFromOther)) {
            playMessageSound(context,Sound.incomingMessageFromOther.getRawFile());
        }

    }

    public void play(Sound sound,int rawFile) {
        if (rawFile==0) {
            play(sound);
            return;
        }
        if (sound.equals(Sound.incomingCall)) {
            startIncomingAudio(Uri.parse("android.resource://" + context.getPackageName() + "/" +
                    rawFile),true);
        } else if (sound.equals(Sound.outgoingCall)) {
            startOutgoingAudio(OutgoingAudioManager.Type.IN_COMMUNICATION,rawFile);
        } else if (sound.equals(Sound.incomingMessage)
                || sound.equals(Sound.outgoingMessage)
                || sound.equals(Sound.incomingMessageFromOther)) {
            playMessageSound(context,rawFile);
        }
    }
    private void startIncomingAudio(Uri ringtone, boolean isVibarte)
    {
        AudioManager audioManager = Utils.getAudioManager(context);
        boolean      speaker      = !audioManager.isWiredHeadsetOn() && !audioManager.isBluetoothScoOn();

        audioManager.setMode(AudioManager.MODE_RINGTONE);
        audioManager.setMicrophoneMute(false);
        audioManager.setSpeakerphoneOn(speaker);

        incomingAudioHelper.start(ringtone, isVibarte);
    }

    private void startOutgoingAudio(OutgoingAudioManager.Type type,int rawID) {
        AudioManager audioManager = Utils.getAudioManager(context);
        audioManager.setMicrophoneMute(false);

        if (type == OutgoingAudioManager.Type.IN_COMMUNICATION) {
            audioManager.setSpeakerphoneOn(false);
        }

        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);

        outgoingAudioHelper.start(type,rawID);
    }

    private void silenceIncomingRinger() {
        incomingAudioHelper.stop();
    }

    private void startCall(boolean preserveSpeakerphone) {
        AudioManager audioManager = Utils.getAudioManager(context);

        incomingAudioHelper.stop();
        outgoingAudioHelper.stop();

        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);

        if (!preserveSpeakerphone) {
            audioManager.setSpeakerphoneOn(false);
        }

    }

    public void pause() {
        AudioManager audioManager = Utils.getAudioManager(context);
        audioManager.setSpeakerphoneOn(false);
        audioManager.setMicrophoneMute(false);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.abandonAudioFocus(null);
        incomingAudioHelper.stop();
        outgoingAudioHelper.stop();

//        if (playDisconnected) {
            soundPool.play(disconnectedSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
//        }

        if (audioManager.isBluetoothScoOn()) {
            audioManager.setBluetoothScoOn(false);
            audioManager.stopBluetoothSco();
        }


    }

    private void playMessageSound(Context context ,int ringId) {
        MediaPlayer mMediaPlayer = MediaPlayer.create(context, ringId);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        });

    }

}
