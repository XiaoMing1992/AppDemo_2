package guyuanjun.com.myappdemo.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import guyuanjun.com.myappdemo.R;

/**
 * Created by HP on 2017-10-10.
 */

public class ExitDialog extends Dialog {
    private Context mContext;
    private TextView content;
    private Button sure;
    private Button cancel;

    public ExitDialog(Context context) {
        super(context, R.style.MyDialogStyle02);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit_dialog);
        initView();
        listener();
    }

    private void initView(){
        content = (TextView)findViewById(R.id.content);
        sure = (Button)findViewById(R.id.sure);
        cancel = (Button)findViewById(R.id.cancel);
    }

    private void listener(){
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof Activity)
                    ((Activity) mContext).finish();
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setContent(String content){
        this.content.setText(content);
    }

    public String getContent(){
       return this.content.getText().toString().trim();
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        getWindow().getDecorView().setPadding(10, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}
