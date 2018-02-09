package guyuanjun.com.myappdemo.admin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.BeanConstant;
import guyuanjun.com.myappdemo.utils.Utils;

/**
 * Created by HP on 2017-5-21.
 */

public class PreviewNews extends Dialog {

    private Context mContext;
    private ImageView img;
    private TextView title;
    private TextView where;
    private TextView content;

    private String _title;
    private String _where;
    private String _content;
    private String _img_path;
    private int _flag;

    public PreviewNews(Context context) {
        super(context, R.style.MyDialogStyle);
        mContext = context;
    }


    public PreviewNews(Context context, String _title, String _where, int _flag) {
        super(context, R.style.MyDialogStyle);
        mContext = context;
        this._title = _title;
        this._where = _where;
        this._flag = _flag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview);
        initView();
        initData();
    }

    private void initView() {
        img = (ImageView) findViewById(R.id.img);
        where = (TextView) findViewById(R.id.where);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
    }

    private void initData() {
        title.setText(_title);
        where.setText(_where);
        if (_flag == BeanConstant.WORD_TYPE) {
            content.setVisibility(View.VISIBLE);
            content.setText(_content);
        } else if (_flag == BeanConstant.IMG_TYPE) {
            img.setVisibility(View.VISIBLE);
            img.setImageBitmap(Utils.getInstance().getPicture(getImgPath()));
        } else if (_flag == BeanConstant.WORD_IMG_TYPE) {
            content.setVisibility(View.VISIBLE);
            content.setText(_content);
            img.setVisibility(View.VISIBLE);
            img.setImageBitmap(Utils.getInstance().getPicture(getImgPath()));
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK/* || keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER */) && event.getAction() == KeyEvent.ACTION_DOWN) { // 监控/拦截/屏蔽返回键
            if (PreviewNews.this.isShowing())
                PreviewNews.this.dismiss();
            return true;
        }
        return false;
    }

    public void setContent(String content) {
        this._content = content;
    }

    public String getContent() {
        return this._content;
    }

    public void setImgPath(String path) {
        this._img_path = path;
    }

    public String getImgPath() {
        return this._img_path;
    }
}

