package com.example.administrator.txt;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.txt.common.CommonAdapter;
import com.example.administrator.txt.common.ViewHolder;
import com.example.administrator.txt.db.SerialDao;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2018\11\6 0006.
 */

public class Scanning_Fragment extends Fragment {


    @BindView(R.id.scanning_number_edt)
    EditText scanningNumberEdt;
    @BindView(R.id.scanning_search_tv)
    TextView scanningSearchTv;
    @BindView(R.id.scanning_name_tv)
    TextView scanningNameTv;
    @BindView(R.id.scanning_shuliang_edt)
    EditText scanningShuliangEdt;
    @BindView(R.id.scanning_lrv)
    LRecyclerView scanningLrv;
    Unbinder unbinder;
    @BindView(R.id.scanning_btn)
    Button scanningBtn;
    @BindView(R.id.scanning_delete_btn)
    Button scanningDeleteBtn;
    @BindView(R.id.saomiao_tv)
    TextView saomiaoTv;
    @BindView(R.id.shengyu_tv)
    TextView shengyuTv;
    @BindView(R.id.shuru_btn)
    Button shuruBtn;
    private LRecyclerViewAdapter lRecyclerViewAdapter = null;
    private CommonAdapter<String> adapter;
    private List<String> datas = new ArrayList<>(); //PDA机屏幕上的List集合
    private SweetAlertDialog sweetAlertDialog;
    SerialDao serialDao;
    private SweetAlertDialog chongfuDialog;
    int shuliang;

    public static Scanning_Fragment newInstance() {
        Scanning_Fragment fragment = new Scanning_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanning, container, false);
        unbinder = ButterKnife.bind(this, view);
        //注册订阅者
        EventBus.getDefault().register(this);
        chongfuDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
        chongfuDialog.setCancelable(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        serialDao = new SerialDao(getActivity());
        initAdapter();
        scanningShuliangEdt.setText("");
    }

    //接受扫码消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        String string = messageEvent.getMessage();
        string.replace(" ", "");
        initData(string);
        Log.d("feng", string);
    }

    private void initData(String data) {
//        重复扫到的不录入，超出数量的不录入,一维码的码位不能超过20包括20，

        try {
             shuliang = Integer.parseInt(scanningShuliangEdt.getText().toString());
            int error = 0;
            for (int i = 0; i < datas.size(); i++) {
                if (data.equals(datas.get(i))) {
                    error = 1;
//                return;
                }
            }
            if (data.length() >= 20) {
                error = 2;
            } else if (datas.size() >= shuliang) {
                error = 3;
            }

            switch (error) {
                case 1:
                    chongfuDialog
                            .setTitleText("重复录入...");
                    chongfuDialog.setConfirmText("确定");
                    chongfuDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            chongfuDialog.dismiss();
                        }
                    });
                    chongfuDialog.show();
                    break;
                case 2:
                    chongfuDialog
                            .setTitleText("超出码位...");
                    chongfuDialog.setConfirmText("确定");
                    chongfuDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            chongfuDialog.dismiss();
                        }
                    });
                    chongfuDialog.show();
                    break;
                case 3:
                    chongfuDialog
                            .setTitleText("超出数量...");
                    chongfuDialog.setConfirmText("确定");
                    chongfuDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            chongfuDialog.dismiss();
                        }
                    });
                    chongfuDialog.show();
                    break;
                default:
                    datas.add(data);
                    lRecyclerViewAdapter.notifyDataSetChanged();
                    chongfuDialog.dismiss();
                    saomiaoTv.setText("" + datas.size());
                    int shengyu = shuliang - datas.size();
                    shengyuTv.setText("" + shengyu);
                    break;
            }
        } catch (Exception e) {

        }


    }

    private void initAdapter() {
        adapter = new CommonAdapter<String>(getActivity(), R.layout.adapter_scanning, datas) {
            @Override
            public void setData(ViewHolder holder, String string) {
                holder.setText(R.id.adapter_data_tv, string);
            }
        };


        scanningLrv.setLayoutManager(new LinearLayoutManager(getActivity()));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        scanningLrv.setAdapter(lRecyclerViewAdapter);
        scanningLrv.setLoadMoreEnabled(false);
        scanningLrv.setPullRefreshEnabled(false);
        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.showCancelButton(true);
                sweetAlertDialog.setCancelText("取消");
                sweetAlertDialog.setTitleText("确定删除此条信息?");
                sweetAlertDialog.setConfirmText("确定");
                sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        datas.remove(position);
                        lRecyclerViewAdapter.notifyDataSetChanged();
                        saomiaoTv.setText("" + datas.size());
                        int shengyu = shuliang - datas.size();
                        shengyuTv.setText("" + shengyu);
                        sweetAlertDialog.dismiss();
                    }
                });
                sweetAlertDialog.show();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        //解除订阅者
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @OnClick({R.id.scanning_search_tv, R.id.scanning_btn, R.id.scanning_delete_btn, R.id.shuru_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scanning_search_tv:
                scanningNameTv.setText(serialDao.select(scanningNumberEdt.getText().toString()));


//                StringBuilder str1 = new StringBuilder();
//                List<TxTBean> list1 = serialDao.select1();
//
//                for (int i = 0; i <list1.size() ; i++) {
//                    str1.append(list1.get(i).getNumber() + "\t");
//                    str1.append(list1.get(i).getName() + "\r\n");
//                }
//                byte[] b1 = str1.toString().getBytes();
//                SDCardHelper.saveFileToSDCardCustomDir(b1, "DCIM", DateUtils.getCurrentTime3() + ".txt");
                break;
            case R.id.scanning_btn:
                int shuliang;
                try {
                    shuliang = Integer.parseInt(scanningShuliangEdt.getText().toString());
                } catch (Exception e) {
                    shuliang = 0;
                }

                if (TextUtils.isEmpty(scanningNumberEdt.getText()) == false && TextUtils.isEmpty(scanningNameTv.getText()) == false && datas.size() != 0 && datas.size() <= shuliang) {
                    StringBuilder str = new StringBuilder();
                    List<String> list = Txt1();
                    for (int i = 0; i < list.size(); i++) {
                        String toString = list.get(i).toString();
                        String danhao = toString.substring(0, toString.indexOf("\t")); //获取公司ID
                        String toString1 = toString.substring(danhao.length() + "\t".length(), toString.length()); //截取公司ID后剩下的所有信息
                        String name = toString1.substring(0, toString1.indexOf("\t")); //获取公司名字
                        String toString2 = toString1.substring(name.length() + "\t".length(), toString1.length());//截取公司ID 和 公司名字 后剩下的所有信息
                        String data = toString2.substring(0, toString2.indexOf("\t")); //获取扫描信息
                        String time = toString2.substring(data.length() + "\t".length(), toString2.length());//获取扫描时间
//                        if (name.length() < 18) {
//                            int differ = 18 - name.length();
//                            for (int j = 0; j < differ; j++) {
//                                name = name + R.string.space;
//                            }
//                        }
//                        Log.d("feng", name);
                        str.append(danhao + "\t");
                        str.append(name + "\t");
                        str.append(data + "\t");
                        str.append(time + "\r\n");
                    }
                    for (int i = 0; i < datas.size(); i++) {
                        str.append(scanningNumberEdt.getText().toString() + "\t");
                        String name = scanningNameTv.getText().toString();
//                        if (name.length() < 18) {
//                            int differ = 18 - name.length();
//                            for (int j = 0; j < differ; j++) {
//                                name = name + R.string.space;
//                            }
//                        }
                        str.append(name + "\t");
                        str.append(datas.get(i).toString() + "\t");
                        str.append(DateUtils.getCurrentTime3() + "\r\n");
                    }
                    byte[] b = str.toString().getBytes();
//                SDCardHelper.saveFileToSDCardPrivateFilesDir(b, "", "锋哥", getActivity());
                    //直接往本地存储

                    SDCardHelper.saveFileToSDCardCustomDir(b, "DCIM", DateUtils.getCurrentTime3() + ".txt");
                    Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                    datas.clear();
                    lRecyclerViewAdapter.notifyDataSetChanged();
                    scanningShuliangEdt.setText("");
                } else {
                    Toast.makeText(getActivity(), "数据有误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.scanning_delete_btn:
                datas.clear();
                lRecyclerViewAdapter.notifyDataSetChanged();
                scanningShuliangEdt.setText("");
                saomiaoTv.setText(0+"");
                shengyuTv.setText(0+"");
//                scanningShuliangEdt.setText("0");
//                scanningShuliangEdt.setSelection("0".length());
                //先把表中的数据解析出来
                break;
            case R.id.shuru_btn:
                showDialog();
                break;
        }
    }

    //初始化并弹出对话框方法
    private void showDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(view).create();
        final EditText shoudong = view.findViewById(R.id.dialog_edt);
        TextView cancel = view.findViewById(R.id.cancel_btn);
        TextView sure = view.findViewById(R.id.sure_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //... To-do
                dialog.dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //... To-do
                String string = shoudong.getText().toString();
                string.isEmpty();
                if (  string.isEmpty()==false){
                    initData(string);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
        //此处设置位置窗体大小，我这里设置为了手机屏幕宽度的3/4
        dialog.getWindow().setLayout((ScreenUtils.getScreenWidth(getActivity()) / 4 * 3), LinearLayout.LayoutParams.WRAP_CONTENT);
    }


    public List<String> Txt1() {
        //将读出来的一行行数据使用List存储
        String filePath = "/storage/emulated/0/DCIM/" + DateUtils.getCurrentTime3() + ".txt";
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

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
