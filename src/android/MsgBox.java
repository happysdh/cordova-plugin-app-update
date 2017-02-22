package com.vaenow.appupdate.android;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xunao.udsa.R;

import org.apache.cordova.LOG;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LuoWen on 2016/1/20.
 */
public class MsgBox {
    public static final String TAG = "MsgBox";
    public static final int CancelButtonIndex = 1;
    public static final int UpdateButtonIndex = 2;
    private Context mContext;
    private MsgHelper msgHelper;

    private Dialog noticeDialog;
    private AlertDialog downloadDialog;
    private ProgressBar downloadDialogProgress;
    private Dialog errorDialog;

    public MsgBox(Context mContext) {
        this.mContext = mContext;
        this.msgHelper = new MsgHelper(mContext.getPackageName(), mContext.getResources());
    }

    /**
     * 显示软件更新对话框
     *
     * @param onClickListener
     */
    public Dialog showNoticeDialog(OnClickListener onClickListener) {
        if (noticeDialog == null) {
            LOG.d(TAG, "showNoticeDialog");
            // 构造对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(msgHelper.getString(MsgHelper.UPDATE_TITLE));
            builder.setMessage("1.更新啦JsonParser是定义公共API用于读取的Json内容的基类。使用JsonFactory实例的工厂方法创建实例\n2.呵呵呵\n3.嘿嘿嘿\n4.啦啦啦\n5.滴滴滴");
//            builder.setMessage(msgHelper.getString(MsgHelper.UPDATE_MESSAGE));
            // 更新
            builder.setPositiveButton(msgHelper.getString(MsgHelper.UPDATE_UPDATE_BTN), onClickListener);
            noticeDialog = builder.create();
        }

        if (!noticeDialog.isShowing()) noticeDialog.show();

        noticeDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        return noticeDialog;
    }

    public Dialog showDialog(VersionInfo versionInfo,Version version,final OnClickListener onClickListener) {
        if(noticeDialog == null) {
            View versionView = View.inflate(mContext, R.layout.appupdate_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            TextView txtTitle = (TextView) versionView.findViewById(R.id.title);
            TextView txtContent = (TextView) versionView.findViewById(R.id.content);
            txtTitle.setText("检测到新版本V " + versionInfo.getCurrentVersion());
            txtContent.setText(versionInfo.getContent());
            Button btnCancel = (Button) versionView.findViewById(R.id.btn_cancel);
            Button btnUpdate = (Button) versionView.findViewById(R.id.btn_update);
            builder.setCancelable(false);
            builder.setView(versionView);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(noticeDialog,CancelButtonIndex);
                }
            });
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(noticeDialog,UpdateButtonIndex);
                }
            });
            //强制更新
            if (version.getLocal() < version.getLowest()) {
                btnCancel.setVisibility(View.GONE);
            }
            noticeDialog = builder.create();
        }
        if (!noticeDialog.isShowing()) noticeDialog.show();
        return noticeDialog;
    }


    /**
     * 显示软件下载对话框
     */
    public Map<String, Object> showDownloadDialog(OnClickListener onClickListenerNeg,
                                                  OnClickListener onClickListenerPos,
                                                  OnClickListener onClickListenerNeu) {
        if (downloadDialog == null) {
            LOG.d(TAG, "showDownloadDialog");

            // 构造软件下载对话框
            AlertDialog.Builder builder = new Builder(mContext);
            builder.setTitle(msgHelper.getString(MsgHelper.UPDATING));
            // 给下载对话框增加进度条
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            View v = inflater.inflate(msgHelper.getLayout(MsgHelper.APPUPDATE_PROGRESS), null);

            /* 更新进度条 */
            downloadDialogProgress = (ProgressBar) v.findViewById(msgHelper.getId(MsgHelper.UPDATE_PROGRESS));
            builder.setView(v);
            // 取消更新
            //builder.setNegativeButton(msgHelper.getString("update_cancel"), onClickListener);
            //转到后台更新
            builder.setNegativeButton(msgHelper.getString(MsgHelper.UPDATE_BG), onClickListenerNeg);
            builder.setNeutralButton(msgHelper.getString(MsgHelper.DOWNLOAD_COMPLETE_NEU_BTN), onClickListenerNeu);
            builder.setPositiveButton(msgHelper.getString(MsgHelper.DOWNLOAD_COMPLETE_POS_BTN), onClickListenerPos);
            downloadDialog = builder.create();
        }

        if (!downloadDialog.isShowing()) downloadDialog.show();

        downloadDialog.setTitle(msgHelper.getString(MsgHelper.UPDATING));
        downloadDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setVisibility(View.VISIBLE); //Update in background
        downloadDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setVisibility(View.GONE); //Install Manually
        downloadDialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.GONE); //Download Again

        downloadDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("dialog", downloadDialog);
        ret.put("progress", downloadDialogProgress);
        return ret;
    }

    /**
     * 错误提示窗口
     *
     * @param errorDialogOnClick
     */
    public Dialog showErrorDialog(OnClickListener errorDialogOnClick) {
        if (this.errorDialog == null) {
            LOG.d(TAG, "initErrorDialog");
            // 构造对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(msgHelper.getString(MsgHelper.UPDATE_ERROR_TITLE));
            builder.setMessage(msgHelper.getString(MsgHelper.UPDATE_ERROR_MESSAGE));
            // 更新
            builder.setPositiveButton(msgHelper.getString(MsgHelper.UPDATE_ERROR_YES_BTN), errorDialogOnClick);
            errorDialog = builder.create();
        }

        if (!errorDialog.isShowing()) errorDialog.show();

        return errorDialog;
    }

}
