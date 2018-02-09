package guyuanjun.com.myappdemo.fragment.my.help;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.dialog.ExitDialog;

public class FeedbackActivity extends AppCompatActivity {

    private static final String TAG = "FeedbackActivity";
    private Context mContext = FeedbackActivity.this;

    private ImageView back_help;
    private EditText feedback_content;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        initView();
        clickListener();
    }

    private void initView() {
        back_help = (ImageView) findViewById(R.id.back_help);
        feedback_content = (EditText)findViewById(R.id.feedback_content);
        submit = (Button)findViewById(R.id.submit);
    }

    private void clickListener(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, feedback_content.getText().toString()+"  提交成功!");
                Intent intent = new Intent();
                intent.setClass(mContext, HelpHomeActivity.class);
                startActivity(intent);
            }
        });

        back_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, HelpHomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void exitDialog(){
        ExitDialog exitDialog = new ExitDialog(mContext);
        exitDialog.show();
        exitDialog.setContent("放弃编辑的内容？");

    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        exitDialog();
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            exitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
