package om.ma.selectapk;

import java.io.File;
import java.util.List;

import com.example.selectapk.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class listAdapter extends BaseAdapter {
	private Activity mActivity;
	private List<String> mList;
	PackageManager pm = null;

	public listAdapter(Activity activity, List<String> list) {
		this.mActivity = activity;
		this.mList = list;
		pm = mActivity.getBaseContext().getPackageManager();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return mList.hashCode();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mActivity, R.layout.select_item, null);
			holder.apk_path = (TextView) convertView.findViewById(R.id.apk_path);
			holder.apk_img = (ImageView) convertView.findViewById(R.id.apk_img);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.apk_path.setText(mList.get(position));

		PackageInfo pi = pm.getPackageArchiveInfo(mList.get(position), PackageManager.GET_ACTIVITIES);
		ApplicationInfo ai = pi.applicationInfo;
		ai.sourceDir = mList.get(position);
		ai.publicSourceDir = mList.get(position);
		Drawable drawable = pm.getApplicationIcon(ai);
		holder.apk_img.setBackgroundDrawable(drawable);
		holder.apk_path.setOnClickListener(new OnClickListener() {// 点击每一条会跳到安装界面

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.fromFile(new File(mList.get(position))), "application/vnd.android.package-archive");
						mActivity.startActivity(intent);
					}
				});
		holder.apk_path.setOnLongClickListener(new OnLongClickListener() {// 长按会跳出对话框,确认删除

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
						builder.setTitle("确定删除?");
						builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								File file = new File(mList.get(position));
								file.delete();
								notifyDataSetChanged();
							}
						});
						builder.create().show();
						return true;
					}
				});

		return convertView;
	}

	static class ViewHolder {
		TextView apk_path;
		ImageView apk_img;
	}
}
