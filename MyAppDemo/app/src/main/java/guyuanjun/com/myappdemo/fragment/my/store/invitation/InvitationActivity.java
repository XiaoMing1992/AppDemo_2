package guyuanjun.com.myappdemo.fragment.my.store.invitation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.dialog.LoginByPhone;
import guyuanjun.com.myappdemo.user.QQAuthActivity;
import guyuanjun.com.myappdemo.user.weixin.wxapi.WXEntryActivity;
import guyuanjun.com.myappdemo.utils.Utils;

public class InvitationActivity extends AppCompatActivity {
    private Context mContext;

    private TextView invitation_code;
    private ImageView weixin_login;
    private ImageView qq_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        mContext = InvitationActivity.this;
        initView();
        initData();
        listener();
    }

    private void initView(){
        weixin_login = (ImageView) findViewById(R.id.weixin_login);
        qq_login = (ImageView) findViewById(R.id.qq_login);
        invitation_code = (TextView)findViewById(R.id.invitation_code);
    }

    private void initData(){
        invitation_code.setText(getinvitationCode());
    }

    private String getinvitationCode(){
        String _invitation_code = null;
        final int id = 1;
        List<UserInfo>  user = UserInfoDaoOpe.getInstance().query(mContext, id);
        if (user != null && user.size()!=0){
            _invitation_code = user.get(0).getInvitationCode();
        }
        return _invitation_code;
    }

    private void listener(){

        weixin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "微信登录", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(mContext, WXEntryActivity.class);
                startActivity(intent);
            }
        });


        qq_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "qq登录", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(mContext, QQAuthActivity.class);
                startActivity(intent);
            }
        });
    }


}
