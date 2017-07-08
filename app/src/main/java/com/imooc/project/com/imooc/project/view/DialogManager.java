package com.imooc.project.com.imooc.project.view;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooc.project.R;

/**
 * Created by Administrator on 2016/11/14.
 */

public class DialogManager {

    private Dialog mDialog;

    private ImageView mIcon;
    private ImageView mVoice;
    private TextView mLabel;

    private Context mContext;

    public DialogManager(Context context) {
        this.mContext = context;
    }

    public void showRecordingDialog() {
        mDialog = new Dialog(mContext, R.style.Theme_Dialog);
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.dialog_recorder,null);
        mDialog.setContentView(view);
        mIcon = (ImageView) view.findViewById(R.id.dialog_recorder_icon);
        mVoice = (ImageView) view.findViewById(R.id.dialog_recorder_voice);
        mLabel = (TextView) view.findViewById(R.id.dialog_recorder_label);
        mDialog.show();
    }

    public void recording() {
        if(mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLabel.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.mipmap.recorder);
            mLabel.setText(R.string.str_recorder_cance_sent);
        }
    }

    public void wantTocance() {
        if(mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLabel.setVisibility(View.VISIBLE);
            mIcon.setImageResource(R.mipmap.cancel);
            mLabel.setText(R.string.str_recorder_want_cance);
        }

    }

    public void showTooshort() {
        if(mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLabel.setVisibility(View.VISIBLE);
            mIcon.setImageResource(R.mipmap.voice_to_short);
            mLabel.setText(R.string.str_recorder_too_short);
        } else {
            Log.e("DialogManager","showTooshort error ");
        }
    }

    public void dismissDialog() {
        if(mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public void updateVoiceLevle(int voiceLevle) {
        if(mDialog != null && mDialog.isShowing()) {
            int resId = mContext.getResources()
                    .getIdentifier("v"+voiceLevle,"mipmap",mContext.getPackageName());
            mVoice.setImageResource(resId);
        }

    }


}
