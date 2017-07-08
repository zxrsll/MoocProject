package com.imooc.project.com.imooc.project.view;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2016/11/16.
 */

public class AudioManager {

    private MediaRecorder mediaRecorder;
    private String mDir;
    private String mCurrentFilePath;
    private boolean isPrepaerd = false;

    private static AudioManager mInstance;

    public static AudioManager getmInstance(String dir) {
        if(mInstance == null) {
            synchronized (AudioManager.class) {
                if(mInstance == null) {
                    mInstance = new AudioManager(dir);
                }
            }
        }
        return mInstance;
    }

    private AudioManager(String dir) {
        this.mDir = dir;
    }

    public interface AudioStateListener {
        void onAudioPrepared();
    }

    private AudioStateListener mListener;

    public void setAudioStateListener(AudioStateListener mListener) {
        this.mListener = mListener;
    }

    public String getmCurrentFilePath() {
        return mCurrentFilePath;
    }

    public void prepareAudio() {
        isPrepaerd = false;
        File dir = new File(mDir);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        String mFilename = getneratefilename();
        File file = new File(dir,mFilename);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        mCurrentFilePath = file.getAbsolutePath();
        Log.e("AudioManager","mCurrentFilePath = " + mCurrentFilePath);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isPrepaerd = true;
            if(mListener != null) {
                mListener.onAudioPrepared();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getneratefilename() {
      return   UUID.randomUUID().toString()+"amr";
    }

    public int getVoiceLevel(int maxLevel) {
        if(isPrepaerd) {
            try {
                return maxLevel*mediaRecorder.getMaxAmplitude()/32768+ 1;
            } catch (Exception exc) {

            }

        }
        return 1;
    }

    public void cance() {
        if(isPrepaerd) {
            release();
        }
        if(mCurrentFilePath != null) {
            File file = new File(mCurrentFilePath);
            if(file.exists()) {
                file.delete();
                mCurrentFilePath = null;
            }
        }
    }

    public void release() {
        if(mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
