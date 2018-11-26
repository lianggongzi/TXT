package com.example.administrator.txt.filemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.txt.R;

import java.util.List;


public class FileAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	/**
	 * 要显示的按钮项
	 */
	private List<String[]> items;

	public FileAdapter(Context context, List<String[]> item) {
		mInflater = LayoutInflater.from(context);
		items = item;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View conView, ViewGroup parent) {
		ViewHolder holder;
		if (conView == null) {

			// 纵向布局使用自定义的item.xml作为Layout
			// conView = mInflater.inflate(R.layout.component, null);
			conView = mInflater.inflate(R.layout.fileline, null);

			// 初始化holder的item_pic,item_Text
			holder = new ViewHolder();
			holder.item_pic = (ImageView) conView.findViewById(R.id.file_pic);
			holder.item_filename = (TextView) conView
					.findViewById(R.id.filename);
			holder.item_filetime = (TextView) conView
					.findViewById(R.id.filetime);
			holder.item_filesize = (TextView) conView
					.findViewById(R.id.filesize);
			conView.setTag(holder);
		} else {
			holder = (ViewHolder) conView.getTag();
		}
		String[] tmp = (String[]) items.get(position);
		// tmp[0]放置的菜的图片
		// 检查图片是否存在

		if (tmp[0] == "dir") {
			holder.item_pic.setBackgroundResource(R.drawable.folder);
		} else if (tmp[0] == "file"){
			holder.item_pic.setBackgroundResource(R.drawable.doc);
		}
		// tmp[1]放置命令行名
		// System.out.println(tmp[1]);
		holder.item_filename.setText(tmp[1]);
		holder.item_filetime.setText(tmp[2]);
		holder.item_filesize.setText(tmp[3]);
		return conView;
	}

	private class ViewHolder {
		// item_pic 命令行的图片
		ImageView item_pic;
		// item_title 命令行的名称
		TextView item_filename;
		TextView item_filetime;
		TextView item_filesize;
	}
}
