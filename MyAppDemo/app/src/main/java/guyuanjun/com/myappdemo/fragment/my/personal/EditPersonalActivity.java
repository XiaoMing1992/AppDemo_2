package guyuanjun.com.myappdemo.fragment.my.personal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import guyuanjun.com.myappdemo.DaoActivity;
import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.dialog.LoginDialog;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.HttpUtils;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.PrefUtils;
import guyuanjun.com.myappdemo.utils.UploadUtil;
import guyuanjun.com.myappdemo.utils.Utils;
import guyuanjun.com.myappdemo.view.CircleImageView;

import static guyuanjun.com.myappdemo.utils.Constant.requestURL;

public class EditPersonalActivity extends AppCompatActivity {

    private static final String TAG = "EditPersonalActivity";
    private Context mContext = EditPersonalActivity.this;

    private RelativeLayout head;
    private Button sure;
    private CircleImageView head_icon;
    private EditText nickname;
    private ProgressBar progress_bar;

    private String picPath = null;

    private long id = -1;

    private final int UPLOAD_FAIL = 0x00;
    private final int UPLOAD_OK = 0x01;
    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPLOAD_FAIL:
                    progress_bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(mContext, "修改失败", Toast.LENGTH_SHORT).show();
                    break;

                case UPLOAD_OK:
                    progress_bar.setVisibility(View.INVISIBLE);
                    nickname.setText("");

                    EventBus.getDefault().post("modify_head_nickname"); //修改头像和昵称
                    Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_personal);
        initView();
        initData();
        listener();
    }

    private void initView() {
        head = (RelativeLayout) findViewById(R.id.head);
        sure = (Button) findViewById(R.id.sure);
        head_icon = (CircleImageView) findViewById(R.id.head_icon);
        nickname = (EditText) findViewById(R.id.nickname);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
//        nickname.setText("热更新热更新");
//        nickname.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                nickname.setText("热更新热更新11111");
//            }
//        });
    }

    private void initData() {
        if (!Utils.getInstance().has_login(mContext)) {
            LoginDialog loginDialog = new LoginDialog(mContext);
            loginDialog.show();
            return;
        }
        id = PrefUtils.getLong(Constant.SAVE_USER_INFO_NAME, Constant.USER_ID_KEY, mContext);
        LogUtil.d(TAG, "user id = " + id);
    }

    private void listener() {
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /***
                 * 这个是调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的
                 */
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //上传图片
                if (picPath == null) {
                    Toast.makeText(mContext, "请选择图片！", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("picPath", picPath);
                    //上传昵称
                    final String name = nickname.getText().toString().trim();
                    if (!TextUtils.isEmpty(name)) {
                        Toast.makeText(mContext, "您的昵称是：" + name, Toast.LENGTH_SHORT).show();

                        final File file = new File(picPath);
                        if (file != null) {
                            progress_bar.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    //int request = UploadUtil.uploadFile(file, requestURL);
                                    int request = UploadUtil.uploadToLocal(mContext, file, id);
                                    //sure.setText(""+request);
                                    if (request == 200) {
                                        try {
                                            List<UserInfo> uses = UserInfoDaoOpe.getInstance().query(mContext, id);
                                            for (int i = 0; i < uses.size(); i++) {
                                                Log.d(TAG, "head icon path =: " + uses.get(i).getHead_path());
                                                uses.get(i).setNickname(name);
                                                UserInfoDaoOpe.getInstance().save(mContext, uses.get(i));
                                                Log.d(TAG, "nickname =: " + uses.get(i).getNickname());
                                            }
                                            mHandler.sendEmptyMessage(UPLOAD_OK);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            mHandler.sendEmptyMessage(UPLOAD_FAIL);
                                        }
                                    } else {
                                        mHandler.sendEmptyMessage(UPLOAD_FAIL);
                                    }
                                }
                            }).start();
                        }
                    } else {
                        Toast.makeText(mContext, "昵称不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            Uri uri = data.getData();
            Log.e(TAG, "uri = " + uri);
            try {
                String[] pojo = {MediaStore.Images.Media.DATA};

                Cursor cursor = managedQuery(uri, pojo, null, null, null);
                if (cursor != null) {
                    ContentResolver cr = this.getContentResolver();
                    int colunm_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(colunm_index);
                    /***
                     * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，
                     * 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
                     */
                    if (path.endsWith("jpg") || path.endsWith("png")) {
                        picPath = path;
                        Bitmap bitmap = BitmapFactory.decodeStream(cr
                                .openInputStream(uri));
                        head_icon.setImageBitmap(bitmap);
                    } else {
                        alert();
                    }
                } else {
                    alert();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void alert() {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您选择的不是有效的图片")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        picPath = null;
                    }
                }).create();
        dialog.show();
    }
}
