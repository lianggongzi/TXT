package com.example.administrator.txt;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.fragment_demo_ll)
    LinearLayout fragmentDemoLl;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;

    Scanning_Fragment scanningFragment;
    Customer_Fragment customerFragment;
    private Fragment mContent;
    private SweetAlertDialog MEIDDialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MEIDDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        MEIDDialog.setCancelable(false);
        getPermission();
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (tm.getDeviceId().equals("86109403064010")) {
            intiView();
        } else {
            initDialog(tm.getDeviceId());
        }
//        intiView();
//        Log.d("feng",tm.getDeviceId()+"------");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 123:
                List<String> denied = new ArrayList<String>();
                for (int i = 0; i < permissions.length; i++){
                    if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                        denied.add(permissions[i]);
                    }
                }
                onPermission(denied.toArray(new String[denied.size()]));
                break;
        }
    }


    private void onPermission(String[] strings) {
        if (strings.length > 0){
            Toast.makeText(this, "访问需要获取和存储权限！",Toast.LENGTH_LONG).show();
            return;
        }
        Log.d("feng","获取成功");
    }
    private void getPermission(){
        String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        List<String> permissionsList = new ArrayList<String>();
        for (int i = 0; i < permissions.length; i++){
            if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permissions[i]);
            }
        }
        if (permissionsList.size() > 0){
            ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]), 123);
        }else {
            onPermission(new String[]{});
        }
    }


    private void initDialog(String s) {
        MEIDDialog
                .setTitleText("请联系商家");
        MEIDDialog.setContentText(s);
        MEIDDialog.setConfirmText("确定");
        MEIDDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                MEIDDialog.dismiss();
                System.exit(0);
            }
        });
        MEIDDialog.show();
    }

    private void intiView() {
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.scanning, "扫码").setActiveColor("#3F51B5"))
                .addItem(new BottomNavigationItem(R.drawable.customer, "数据").setActiveColor("#3F51B5"))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
//        setDefaultFragment();

        setDefaultFragment();
    }


    /**
     * 设置默认的fragment，即//第一次加载界面;
     */
    private void setDefaultFragment() {
        scanningFragment = Scanning_Fragment.newInstance();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_demo_ll, scanningFragment).commit();
        mContent = scanningFragment;
    }

    /**
     * 修改显示的内容 不会重新加载 *
     */
    public void switchContent(Fragment to) {
        FragmentManager fragmentManager = getFragmentManager();
        if (mContent != to) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(mContent).add(R.id.fragment_demo_ll, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mContent = to;
        }
    }

    @Override
    public void updateCount() {

    }

    @Override
    public void updateList(String data) {
        EventBus.getDefault().post(new MessageEvent(data));
    }

    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = this.getFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (scanningFragment == null) {
                    scanningFragment = Scanning_Fragment.newInstance();
                }
//                showCustomizeDialog(srttingFragmenr, (Integer) SPUtils.get(this, "position", 1));
                //将当前的事务添加到了回退栈
//                transaction.addToBackStack(null);
//                transaction.add(R.id.fragment_demo_ll, fragment1);
                switchContent(scanningFragment);
                break;
            case 1:
                if (customerFragment == null) {
                    customerFragment = Customer_Fragment.newInstance();
                }
//                transaction.add(R.id.fragment_demo_ll, fragment2);
                switchContent(customerFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }



    /**
     * 根据行读取内容
     *
     * @return
     */

    public List<String> Txt() {
        //将读出来的一行行数据使用List存储
        String filePath = "/storage/emulated/0/DCIM/Camera/user1.txt";
        List list = new ArrayList<String>();

        try {
            File file = new File(filePath);
            int count = 0;//初始化 key值
            if (file.isFile() && file.exists()) {//文件存在
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    if (!"".equals(lineTxt)) {
                        String reds = lineTxt.split("\\+")[0];  //java 正则表达式
                        list.add(count, reds);
                        count++;
                    }
                }
                isr.close();
                br.close();
            } else {
                Toast.makeText(getApplicationContext(), "can not find file", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> Txt1(String filePath) {
        //将读出来的一行行数据使用List存储
//        String filePath = "/storage/emulated/0/DCIM/Camera/user1.txt";
        List list = new ArrayList<String>();

        try {
            File file = new File(filePath);
            int count = 0;//初始化 key值
            if (file.isFile() && file.exists()) {//文件存在
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    if (!"".equals(lineTxt)) {
                        String reds = lineTxt.split("\\+")[0];  //java 正则表达式
                        list.add(count, reds);
                        count++;
                    }
                }
                isr.close();
                br.close();
            } else {
                Toast.makeText(getApplicationContext(), "can not find file", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
