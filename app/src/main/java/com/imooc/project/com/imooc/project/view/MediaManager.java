package com.imooc.project.com.imooc.project.view;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by Administrator on 2016/11/17.
 */

public class MediaManager {

    private static MediaPlayer mMediaPlayer;
    private static boolean isPaused;

    public static void playSound(String path, MediaPlayer.OnCompletionListener onCompletionListener) {
        if(mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    mMediaPlayer.reset();
                    return false;
                }
            });
        } else {
            mMediaPlayer.reset();
        }
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pause() {
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            isPaused = true;
        }
    }

    public static void resume() {
        if(mMediaPlayer != null && isPaused) {
            mMediaPlayer.pause();
            isPaused = false;
            mMediaPlayer.start();
        }
    }

    public static void release() {
        if(mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
