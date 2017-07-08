package com.imooc.project.com.imooc.project.view;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.imooc.project.R;

/**
 * Created by Administrator on 2016/11/13.
 */

public class AudioRecorderButton extends Button implements AudioManager.AudioStateListener{

    private static final int DISTANCE_Y_CANCE = 50;
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCE = 3;

    private int mCurState = STATE_NORMAL;
    private boolean isRecording = false;

    private DialogManager mDialogManager;

    private AudioManager mAudioManager;

    private float mTime;
    //是否触发longclick
    private boolean mReady = false;

    private Context mContext;

    private static final String TAG = "AudioRecorderButton";

    public AudioRecorderButton(Context context) {
        this(context,null);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mDialogManager = new DialogManager(context);

        String dir = Environment.getExternalStorageDirectory()+"/mooc_audio";
        mAudioManager = AudioManager.getmInstance(dir);
        mAudioManager.setAudioStateListener(this);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mReady = true;
                mAudioManager.prepareAudio();
                return false;
            }
        });
    }

    public AudioRecorderButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    @Override
    public void onAudioPrepared() {
        mHandler.sendEmptyMessage(AUDIO_PREPARED);

    }

    private Runnable voiceLevelRunable = new Runnable() {
        @Override
        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mHandler.sendEmptyMessage(VOICE_CHANGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private static final int AUDIO_PREPARED = 0x110;
    private static final int VOICE_CHANGED = 0x111;
    private static final int DIALOG_DISMISS = 0x112;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what) {
               case AUDIO_PREPARED:
                   //TODO showDiglog when prepard
                   mDialogManager.showRecordingDialog();
                   isRecording = true;
                   new Thread(voiceLevelRunable).start();
                   break;
               case VOICE_CHANGED:
                   mDialogManager.updateVoiceLevle(mAudioManager.getVoiceLevel(7));
                   break;
               case DIALOG_DISMISS:
                   mDialogManager.dismissDialog();
                   break;
               default:
                   break;
           }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                //根据x.y的坐标判断是否要取消
                if(isRecording) {
                    if(wantToCance(x,y)) {
                        changeState(STATE_WANT_TO_CANCE);
                    } else {
                        changeState(STATE_RECORDING);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                if(!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }
                Log.e("AudioRecorderButton","time = " + mTime);
                if(!isRecording || mTime < 0.6f) {
                    Log.e("AudioRecorderButton","time is too short");
                    mDialogManager.showTooshort();
                    mAudioManager.cance();
                    mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS,1300);
                } else if(STATE_RECORDING == mCurState) {
                    mAudioManager.release();
                    mDialogManager.dismissDialog();
                    if(mListener != null) {
                        mListener.onFinish(mTime,mAudioManager.getmCurrentFilePath());
                    }
                } else if(STATE_WANT_TO_CANCE == mCurState) {
                    mAudioManager.cance();
                    mDialogManager.dismissDialog();
                }
                reset();
                break;
        }
        return super.onTouchEvent(event);
    }

    private OnAudioRecordFinishListener mListener;

    public void setOnAudioRecordFinishListener(OnAudioRecordFinishListener listener) {
        this.mListener = listener;
    }

    public interface OnAudioRecordFinishListener {
        void onFinish(float secord, String path);
    }

    /**
     * 回复状态以及标志位
     */
    private void reset() {
        changeState(STATE_NORMAL);
        isRecording = false;
        mTime = 0;
        mReady = false;
    }

    private boolean wantToCance(int x, int y) {
        if(x<0 || x>getWidth()) {
            return true;
        }
        if(y< -DISTANCE_Y_CANCE || y> (getHeight() +DISTANCE_Y_CANCE)) {
            return true;
        }
        return false;
    }

    private void changeState(int state) {
        if(mCurState != state) {
            mCurState = state;
            switch (state) {
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.button_recorder_normal);
                    setText(R.string.str_recorder_normal);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.button_recorder_recording);
                    setText(R.string.str_recorder_recording);
                    if(isRecording) {
                        //TODO dialog.recording
                        mDialogManager.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCE:
                    setBackgroundResource(R.drawable.button_recorder_recording);
                    setText(R.string.str_recorder_want_cance);
                    mDialogManager.wantTocance();
                    break;
            }
        }
    }
}
