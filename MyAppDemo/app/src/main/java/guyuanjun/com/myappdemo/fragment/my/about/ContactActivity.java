package guyuanjun.com.myappdemo.fragment.my.about;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.fragment.my.help.FeedbackActivity;
import guyuanjun.com.myappdemo.fragment.my.help.HelpHomeActivity;

public class ContactActivity extends AppCompatActivity {

    private static final String TAG = "ContactActivity";
    private Context mContext = ContactActivity.this;

    private ImageView back_contact;
    private ImageView share_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initView();
        clickListener();
    }

    private void initView() {
        back_contact = (ImageView) findViewById(R.id.back_contact);
        share_contact = (ImageView) findViewById(R.id.share_contact);
    }

    private void clickListener(){
        //分享内容
        share_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareContent();
            }
        });

        back_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, HelpHomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void shareContent(){
        Intent intent=new Intent(Intent.ACTION_SEND);
        //intent.setType("image/*");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        intent.putExtra(Intent.EXTRA_TEXT, "I have successfully share my message through my app");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getTitle()));
    }
}
