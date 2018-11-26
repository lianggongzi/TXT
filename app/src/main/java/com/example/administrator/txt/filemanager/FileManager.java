package com.example.administrator.txt.filemanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.txt.R;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 调用此文件管理器，要用如下格式才能得到选择的文件<br>
 * 在你的页面中调用此文件管理器如下定义：<br>
 * Intent intent =new Intent();<br>
 * intent.setClass(你的要类页面,FileManager.class);<br>
 * Bundle bundle=new Bundle();<br>
 * bundle.putString("xuanze","");<br>
 * bundle.putString("filename", "");
 * bundle.putBoolean("iserror", true);
 * intent.putExtras(bundle);<br>
 * startActivityForResult(intent,0);<br>
 * <br>
 * 同时要重写onActivityResult函数<br>
 * protected void onActivityResult(int requestCode, int resultCode, Intent data) {<br>
 *      switch(resultCode){<br>
 *      case RESULT_OK:<br>
 *           //这就是你要接收选择的文件<br>
 *           Bundle bundle=data.getExtras();<br>
 *           String xuanzewenjian=bundle.getString("xuanze");<br>
 *           String wenjian_text.setText(bundle.getString("filename"));
 *           boolean isError=bundle.getBoolean("iserror");
 *           break;<br>
 *      default:<br>
 *           break;<br>
 *      }<br>
 * 完毕
 *
 */
@SuppressLint("NewApi")
public class FileManager extends Activity {
	private List<String[]> pathname = null;
	private List<String[]> paths = null;
	private GridView gView;
	private RelativeLayout return_button;// 返回上一级目录按钮
	private TextView return_text;
	private ImageButton close_button;// 关闭按钮
	private String mulu = "/mnt";
	private String xianshimulu = "mnt";
	private String zhongjiemulu = "/mnt";
	private String xuanzhewenjian1 = "";//选择的文件
	private String xuanzhewenjian2="";

	Intent intent;
	Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_manager);
		intent=this.getIntent();
		bundle=intent.getExtras();
		if(bundle!=null){
			xuanzhewenjian1=bundle.getString("xuanzhe");
			xuanzhewenjian2=bundle.getString("filename");
		}
		return_text = (TextView) findViewById(R.id.pathname);
		return_text.setText(xianshimulu);
		return_button = (RelativeLayout) findViewById(R.id.return_button);
		// 添加返回上一级目录的时间
		return_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!mulu.equals(zhongjiemulu)) {
					File f = new File(mulu);
					if (f.exists()) {
						mulu = f.getParent();

						File f1 = new File(mulu);
						if (f1.exists()) {
							return_text.setText(f1.getName());
							xianshimulu = f1.getName();
						}
						getFileDir(mulu);
					}
				}

			}
		});
		return_button.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_UP) {
					return_button.setBackgroundResource(R.color.steelblue);
				} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
					return_button.setBackgroundResource(R.color.cornflowerblue);
				}
				return false;
			}
		});
		close_button = (ImageButton) findViewById(R.id.imagebuttonx);
		close_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		gView = (GridView) findViewById(R.id.filelist);
		getFileDir(mulu);
	}

	private void getFileDir(String filepath) {
		paths = new ArrayList<String[]>();
		pathname = new ArrayList<String[]>();
		File f = new File(filepath);
		File[] files = f.listFiles();
		List<String[]> dir1=new ArrayList<String[]>();
		List<String[]> dir2=new ArrayList<String[]>();
		List<String[]> file1=new ArrayList<String[]>();
		List<String[]> file2=new ArrayList<String[]>();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.canRead() && !file.isHidden()) {
				//pathname.add(new String[] { file.getPath() });
				if (file.isDirectory()) {
					dir1.add(new String[]{file.getPath()});
					dir2.add(new String[] { "dir", file.getName(),
							getDateTime(file.lastModified()), "" });
				} else if (file.isFile()) {
					if (file.getName().indexOf(".xls")!=-1) {
						file1.add(new String[] { file.getPath() });
						file2.add(new String[] { "file", file.getName(),
								getDateTime(file.lastModified()),
								getFileSizes(file) });
					}
				}
			}

		}
		sortList(dir1,dir2,file1,file2);
		// 使用自定义的MyAdapter来将数据传入ListActivity
		gView.setAdapter(new FileAdapter(this, paths));
		gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				if (paths.get(arg2)[0] == "dir") {
					return_text.setText(paths.get(arg2)[1]);
					mulu = pathname.get(arg2)[0];
					getFileDir(pathname.get(arg2)[0]);
				} else if (paths.get(arg2)[0] == "file") {
					if (bundle != null) {
						bundle.putString("xuanze", pathname.get(arg2)[0]);
						bundle.putString("filename", paths.get(arg2)[1]);
						bundle.putBoolean("iserror", false);

						intent.putExtras(bundle);
					}
					FileManager.this.setResult(RESULT_OK, intent);
					FileManager.this.finish();
				}
			}

		});
	}

	@SuppressWarnings("deprecation")
	private String getDateTime(long datetime) {
		String dt = "";
		Date times = new Date(datetime);
		dt = Integer.toString(times.getYear() + 1900) + "-"
				+ Integer.toString(times.getMonth() + 1) + "-"
				+ Integer.toString(times.getDate()) + "  "
				+ Integer.toString(times.getHours()) + ":"
				+ Integer.toString(times.getMinutes()) + ":"
				+ Integer.toString(times.getSeconds());
		return dt;
	}

	public String getFileSizes(File f) {// 取得文件大小
		long s = 0;
		String returnByte = "";
		if (f.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(f);
				s = fis.available();
			} catch (Exception e) {
			}
		} else {
			System.out.println("文件不存在");
		}
		if (s < 1024) {
			returnByte = Long.toString(s) + " B";
		} else if (s >= 1024 && s < (1024 * 1024)) {
			returnByte = Long.toString(s / 1024) + " KB";
		} else if (s > (1024 * 1024)) {
			returnByte = Long.toString(s / (1024 * 1024)) + " MB";
		}
		return returnByte;
	}
	/**
	 * 对文件及目录进行排序
	 */
	public void sortList(List<String[]> d1,List<String[]> d2,List<String[]> f1,List<String[]> f2){
		if(d2!=null){
			if(d2.size()>=2){
				for(int i=0;i<d2.size();i++){
					int indexlow=i;
					for(int j=i+1;j<d2.size();j++){
						if(d2.get(indexlow)[1].compareToIgnoreCase(d2.get(j)[1])>0){
							indexlow=j;
						}
					}
					String[] tmp1=d2.get(i);
					String[] tmp2=d2.get(indexlow);
					d2.remove(i);
					d2.add(i,tmp2);
					d2.remove(indexlow);
					d2.add(indexlow, tmp1);
					tmp1=d1.get(i);
					tmp2=d1.get(indexlow);
					d1.remove(i);
					d1.add(i,tmp2);
					d1.remove(indexlow);
					d1.add(indexlow, tmp1);
				}
			}
		}
		if(f2!=null){
			if(f2.size()>=2){
				for(int i=0;i<f2.size();i++){
					int indexlow=i;
					for(int j=i+1;j<f2.size();j++){
						if(f2.get(i)[1].compareToIgnoreCase(f2.get(j)[1])>0){
							indexlow=j;
						}
					}
					String[] tmp1=f2.get(i);
					String[] tmp2=f2.get(indexlow);
					f2.remove(i);
					f2.add(i,tmp2);
					f2.remove(indexlow);
					f2.add(indexlow, tmp1);
					tmp1=f1.get(i);
					tmp2=f1.get(indexlow);
					f1.remove(i);
					f1.add(i,tmp2);
					f1.remove(indexlow);
					f1.add(indexlow, tmp1);
				}
			}
		}
		pathname=d1;
		paths=d2;
		for(int i=0;i<f2.size();i++){
			pathname.add(f1.get(i));
			paths.add(f2.get(i));
		}
	}
}
