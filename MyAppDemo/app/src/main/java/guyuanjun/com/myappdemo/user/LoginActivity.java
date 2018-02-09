package guyuanjun.com.myappdemo.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.PersistentCookieStore;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;
import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.admin.MyPushNewsActivity;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.dialog.LoginByPhone;
import guyuanjun.com.myappdemo.http.HttpHelper;
import guyuanjun.com.myappdemo.model.User;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.HttpUtils;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.PrefUtils;
import guyuanjun.com.myappdemo.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Context mContext = LoginActivity.this;

    private EditText user_name;
    private EditText myPassword;
    private Button btn_login;
    private ImageView back;
    private TextView go_register;
    private TextView forget_pw;
    private TextView mobile_login;

    private boolean post_result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
        clickListener();
    }

    private void initView() {
        myPassword = (EditText) findViewById(R.id.password);
        user_name = (EditText) findViewById(R.id.user_name);
        btn_login = (Button) findViewById(R.id.btn_login);
        back = (ImageView) findViewById(R.id.back_login);
        mobile_login = (TextView)findViewById(R.id.mobile_login);

        go_register = (TextView) findViewById(R.id.go_register);
        go_register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        go_register.getPaint().setAntiAlias(true);//抗锯齿

        forget_pw = (TextView) findViewById(R.id.forget_pw);
        forget_pw.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        forget_pw.getPaint().setAntiAlias(true);//抗锯齿
    }

    private void clickListener() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = user_name.getText().toString();
                String password = myPassword.getText().toString();
                user_name.setText("");
                myPassword.setText("");

                //UserInfo userInfo = new UserInfo(userName, password);
                password = Utils.getInstance().hashKeyFormUrl(password); //取hash后的密码
                List<UserInfo> UserInfoList = UserInfoDaoOpe.getInstance().query(mContext, userName, password);
                if (UserInfoList.size() > 0) {
                    Utils.getInstance().saveSessionToLocal(mContext, "JSESSIONID=AD5F5C9EEB16C71EC3725DBF209F6178");
                    UserHelper.getInstance().setLogin(true);
                    UserHelper.getInstance().setUserName(userName);
                    UserHelper.getInstance().setUserId(UserInfoList.get(0).getId());

                    LogUtil.d(TAG, "user_name = "+userName+"    "+"user_id = "+UserInfoList.get(0).getId());

                    PrefUtils.set(Constant.SAVE_USER_INFO_NAME, "user_name", userName, mContext);
                    PrefUtils.set(Constant.SAVE_USER_INFO_NAME, "user_id", UserInfoList.get(0).getId(), mContext);

                    EventBus.getDefault().post("login"); //改变登录状态
                    Toast.makeText(mContext, "登陆成功， 用户名是 " + userName + ", 密码是 " + password, Toast.LENGTH_SHORT).show();
                }
                else {
                    UserHelper.getInstance().setLogin(false);
                    Toast.makeText(mContext, "登陆失败， 用户名是 " + userName + ", 密码是 " + password, Toast.LENGTH_SHORT).show();
                }
/*                boolean result = postData(SERVER_ADDR + "/user/login/", userName, password);
                if (result)
                    Toast.makeText(mContext, "登陆成功， 用户名是 " + userName + ", 密码是 " + password, Toast.LENGTH_SHORT).show();
                else {
*//*                    user_name.setError("用户名错误");
                    myPassword.setError("密码错误");*//*
                    Toast.makeText(mContext, "登陆失败， 用户名是 " + userName + ", 密码是 " + password, Toast.LENGTH_SHORT).show();
                }*/
/*                Intent intent = new Intent();
                intent.setClass(mContext, HelpHomeActivity.class);
                startActivity(intent);*/
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //intent.setClass(mContext, RegisterFirst.class);
                //intent.setClass(mContext, PushNewsActivity.class);
                intent.setClass(mContext, MyPushNewsActivity.class);
                startActivity(intent);
            }
        });

        go_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, RegisterFirst.class);
                startActivity(intent);
            }
        });

        forget_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, ForgetpwFirst.class);
                startActivity(intent);
            }
        });

        mobile_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginByPhone loginByPhone = new LoginByPhone(mContext);
                loginByPhone.show();
            }
        });
    }

    private boolean postData(String url, final String userName, final String password) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                //打印请求返回结果
                Log.e("volley", result);
                //jieJson(testDatas());
                post_result = true;
                /*
                User user = null;
                saveSession(user);
                */

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volleyerror", volleyError.getMessage());
                post_result = false;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userName", userName);
                map.put("password", password);
                return map;
            }
        };
        //将StringRequest对象添加进RequestQueue请求队列中
        HttpHelper.getInstance(mContext).addToRequestQueue(stringRequest);
        return post_result;
/*        //封装的okhttp工具类的get异步
        OkHttp.getAsync("http://japi.juhe.cn/joke/content/list.from?key= 874ed931559ba07aade103eee279bb37 &page="+page+"&pagesize=10&sort=asc&time=1418745237", new OkHttp.DataCallBack() {
            //请求失败所做处理（工具类已处理）
            @Override
            public void requestFailure(Request request, IOException e) {
            }
            //请求成功所做处理
            @Override
            public void requestSuccess(String result) throws Exception {
                //解析数据
                jieJson(result);
            }
        });*/
    }

    private void saveSession(User user) {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getApplicationContext());
        HttpUtils.client.setCookieStore(myCookieStore);
        List<Cookie> cookies = myCookieStore.getCookies();
        String JSESSIONID = "";
        if (cookies.isEmpty()) {
            Log.i("session", "None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                if ("JSESSIONID".equals(cookies.get(i).getName())) {
                    JSESSIONID = cookies.get(i).getValue();  // 第二种方法 通过 JSESSIONID
                    System.out.println(JSESSIONID);
                    break;
                }
            }
            PrefUtils.set("user", "session", JSESSIONID, getBaseContext());
            Log.i("session", "保存" + JSESSIONID);
        }
        BasicClientCookie newCookie = new BasicClientCookie("userId", user.getId());
        newCookie.setVersion(1);
        newCookie.setDomain("114.215.135.153");
        newCookie.setPath("/");
        myCookieStore.addCookie(newCookie);
    }
}
