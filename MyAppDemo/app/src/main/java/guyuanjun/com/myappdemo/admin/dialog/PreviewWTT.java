package guyuanjun.com.myappdemo.admin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.utils.Utils;

/**
 * Created by HP on 2017-5-21.
 */

public class PreviewWTT extends Dialog {

    private Context mContext;
    private ImageView img;
    private TextView content;

    private String _content;
    private String _img_path;

    public PreviewWTT(Context context) {
        super(context, R.style.MyDialogStyle);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_wtt);
        initView();
        initData();
    }

    private void initView() {
        img = (ImageView) findViewById(R.id.img);
        content = (TextView) findViewById(R.id.content);
    }

    private void initData() {
        content.setText(_content);
        img.setImageBitmap(Utils.getInstance().getPicture(_img_path));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK/* || keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER */) && event.getAction() == KeyEvent.ACTION_DOWN) { // 监控/拦截/屏蔽返回键
            if (PreviewWTT.this.isShowing())
                PreviewWTT.this.dismiss();
            return true;
        }
        return false;
    }

    public void setContent(String content){
        this._content = content;
    }

    public String getContent(){
        return this._content;
    }

    public void setImgPath(String path){
        this._img_path = path;
    }

    public String getImgPath(){
        return this._img_path;
    }
}


