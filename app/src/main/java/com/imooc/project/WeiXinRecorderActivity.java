package com.imooc.project;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;
import com.imooc.project.com.imooc.project.GuaGuaKaActivity;
import com.imooc.project.com.imooc.project.model.Record;
import com.imooc.project.com.imooc.project.view.AudioRecorderButton;
import com.imooc.project.com.imooc.project.view.MediaManager;
import com.imooc.project.com.imooc.project.view.RecordAdpater;

import java.util.ArrayList;
import java.util.List;

public class WeiXinRecorderActivity extends AppCompatActivity implements AudioRecorderButton.OnAudioRecordFinishListener,MediaPlayer.OnCompletionListener {

    private ListView mRecordListview;
    private AudioRecorderButton mAudioRecorderButton;

    private ArrayAdapter<Record> mAdpater;
    private List<Record> mDatas;
    private View animView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_xin_recorder);
        mRecordListview = (ListView) findViewById(R.id.listview);
        mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.recorder_button);
        mAudioRecorderButton.setOnAudioRecordFinishListener(WeiXinRecorderActivity.this);
        mDatas = new ArrayList<Record>();
        mAdpater = new RecordAdpater(WeiXinRecorderActivity.this, mDatas);
        mRecordListview.setAdapter(mAdpater);
        mRecordListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //播放
                if(animView != null) {
                    animView.setBackgroundResource(R.mipmap.adj);
                    animView = null;
                }
                animView = view.findViewById(R.id.recorder_anim);
                animView.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable animationDrawable = (AnimationDrawable) animView.getBackground();
                animationDrawable.start();
                MediaManager.playSound(mDatas.get(i).getPath(),WeiXinRecorderActivity.this);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(animView != null) {
            animView.setBackgroundResource(R.mipmap.adj);
        }
        Intent intent = new Intent(WeiXinRecorderActivity.this, GuaGuaKaActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFinish(float secord, String path) {
        mDatas.add(new Record(secord, path));
        mRecordListview.setSelection(mDatas.size() - 1);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("WeiXinRecorder Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }
}
