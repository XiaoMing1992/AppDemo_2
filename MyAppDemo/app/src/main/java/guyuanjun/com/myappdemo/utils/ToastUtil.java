package guyuanjun.com.myappdemo.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {

	public static void show(Context context, Object obj) {
		if(obj != null){
			Toast toast = Toast.makeText(context, obj.toString(), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.BOTTOM, 0, 0);
			toast.show();
		}else{
			Toast toast = Toast.makeText(context, "null 空字符", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.BOTTOM, 0, 0);
			toast.show();
		}
	}

}
