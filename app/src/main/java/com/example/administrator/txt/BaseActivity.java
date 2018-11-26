
package com.example.administrator.txt;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.zltd.decoder.Constants;
import com.zltd.industry.ScannerManager;

import java.util.ArrayList;

public abstract class BaseActivity extends AppCompatActivity implements
        ScannerManager.IScannerStatusListener {
    protected static final int UPDATE_LIST = 0x1000;
    protected static final int UPDATE_NUMBER = 0x1001;

    protected ScannerManager mScannerManager;
    protected SoundUtils mSoundUtils;

    protected ArrayList<String> mBarcodeList = new ArrayList<String>();
    protected ArrayAdapter<String> mListAdaper;

    protected int pressed = 0;
    protected int scanned = 0;
    protected int decoderType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //1.????ScannerManager
        mScannerManager = ScannerManager.getInstance();
        decoderType = mScannerManager.getDecoderType();
        mSoundUtils = SoundUtils.getInstance();
        mSoundUtils.init(this);
//        mListAdaper = new ArrayAdapter<String>(this, R.layout.list_item, mBarcodeList);
    }

    public void onResume() {
        super.onResume();
        //2.??????????
        int res = mScannerManager.connectDecoderSRV();
        mScannerManager.addScannerStatusListener(this);
        if(decoderType == Constants.DECODER_ONED_SCAN){
            if (!mScannerManager.getScannerEnable()) {
                new AlertDialog.Builder(this)
                .setTitle("扫描测试")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage("请在设置中打开扫描头")
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        closeSelf();
                    }
                })
                .setCancelable(false)
                .show();
            }
        }
    }
    public void onPause() {
        mScannerManager.removeScannerStatusListener(this);
        mScannerManager.disconnectDecoderSRV();
        super.onPause();
    }

    protected void closeSelf() {
        this.finish();
    }
    @SuppressLint("HandlerLeak")
    protected Handler mHandle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_LIST:
                    scanned++;
                    mSoundUtils.success();
                    updateList((String) msg.obj);
                case UPDATE_NUMBER:
                    updateCount();
                    break;
                default:
                    break;
            }
        }
    };

    public abstract void updateCount();
    public abstract void updateList(String data);

    public void clear() {
        mBarcodeList.clear();
        mListAdaper.notifyDataSetChanged();
        pressed = 0;
        scanned = 0;
        mHandle.sendEmptyMessage(UPDATE_NUMBER);
    }
  //3.??????????????,??????????��??????
    @Override
    public void onScannerResultChanage(byte[] arg0) {
        String data = new String(arg0);
        Message msg = mHandle.obtainMessage(UPDATE_LIST, data);
        mHandle.sendMessage(msg);
    }

    @Override
    public void onScannerStatusChanage(int arg0) {
        // TODO Auto-generated method stub

    }

}
