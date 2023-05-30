package com.cometchat.chatuikit.shared.resources.utils;

import android.media.MediaPlayer;

public class AudioPlayer {
    private final static AudioPlayer audioPlayer = new AudioPlayer();
    private final MediaPlayer mediaPlayer = new MediaPlayer();

    private AudioPlayer() {
    }

    public static AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public void resetPlayer() {
        mediaPlayer.reset();
    }

    public void setAudioUrl(String url, MediaPlayer.OnCompletionListener completionListener) {
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                if (completionListener != null)
                    completionListener.onCompletion(mediaPlayer);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void pausePlayer() {
        mediaPlayer.pause();
    }

    public void startPlayer() {
        mediaPlayer.start();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void stopPlayer(){
        mediaPlayer.pause();
        mediaPlayer.reset();
    }
}
