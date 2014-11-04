package om.ma.selectapk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.selectapk.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

public class selectApk extends Activity {
	private ListView listview;
	private List<String> list = new ArrayList<String>();
	File file = Environment.getExternalStorageDirectory();

	ProgressDialog dialog = null;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			listAdapter adapter = new listAdapter(selectApk.this, list);
			listview.setAdapter(adapter);
			dialog.dismiss();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_apk_layout);
		dialog = new ProgressDialog(selectApk.this);
		listview = (ListView) findViewById(R.id.select_apk_listview);

		// List<PackageInfo> packageInfoList =
		// pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
		// for(PackageInfo packageInfo : packageInfoList){//获得已经安装的apk的信息
		// Log.d("test", "--"+packageInfo.packageName);
		// }
		dialog.setTitle("请稍后");
		dialog.show();
		Thread thread = new Thread(runnable);
		thread.start();
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			getFile(file);
			Message msg = new Message();
			handler.sendMessage(msg);
		}
	};

	private void getFile(File file) {

		File[] fileList = file.listFiles();// 到这里后有可能是 .开头的文件名,在下面有跳过
		if (fileList.length > 0) {
			for (File f : fileList) {
				if (f.isFile()) {
					if (f.getAbsolutePath().endsWith("apk")) {
						list.add(f.getAbsolutePath());
						continue;
					}
				} else if (f.isDirectory() && !(f.getName().startsWith("."))) {
					getFile(f);
				}
			}
		}
	}
}
