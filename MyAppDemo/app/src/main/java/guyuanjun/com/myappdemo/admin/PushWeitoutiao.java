package guyuanjun.com.myappdemo.admin;

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
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.Date;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.admin.dialog.PreviewWTT;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.bean.WeitoutiaoInfo;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.db.WeitoutiaoInfoDaoOpe;
import guyuanjun.com.myappdemo.user.ForgetpwSecond;
import guyuanjun.com.myappdemo.utils.UploadUtil;

public class PushWeitoutiao extends AppCompatActivity {
    private static final String TAG = "PushWeitoutiao";
    private Context mContext = PushWeitoutiao.this;

    private ImageView picture;
    private EditText content;
    private Button submit;
    private Button preview;
    private ProgressBar progress_bar;

    private String img_path = null;
    private long id;

    private final int UPLOAD_FAIL = 0x00;
    private final int UPLOAD_OK = 0x01;

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPLOAD_FAIL:
                    progress_bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(mContext, "发表失败", Toast.LENGTH_SHORT).show();
                    break;

                case UPLOAD_OK:
                    progress_bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(mContext, "发表成功", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_weitoutiao);
        initView();
        initData();
        listener();
    }

    private void initData() {
        id = 1;
    }

    private void initView() {
        picture = (ImageView) findViewById(R.id.picture);
        content = (EditText) findViewById(R.id.content);
        submit = (Button) findViewById(R.id.submit);
        preview = (Button) findViewById(R.id.preview);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void listener() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String _content = content.getText().toString().trim();
                if (!TextUtils.isEmpty(_content)) {
                    //上传图片
                    if (img_path == null) {
                        Toast.makeText(mContext, "请选择图片！", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("img_path", img_path);
                        final File file = new File(img_path);
                        if (file != null) {
                            progress_bar.setVisibility(View.VISIBLE);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    //int request = UploadUtil.uploadFile(file, requestURL);

                                    int request = UploadUtil.uploadToWeitoutiaoServer(mContext, file, id);
                                    //sure.setText(""+request);
                                    if (request == 200) {
                                        try {
                                            List<UserInfo> uses = UserInfoDaoOpe.getInstance().query(mContext, id);
                                            if (uses.size() == 0)
                                                return;

                                            //String head_path = uses.get(0).getHead_path();
                                            String username = uses.get(0).getUsername();

                                            WeitoutiaoInfo info = new WeitoutiaoInfo(id, username, new Date(), 0);
                                            info.setContent(_content);
                                            info.setImg_path(img_path);
                                            WeitoutiaoInfoDaoOpe.getInstance().insertData(mContext, info);

                                            Log.d(TAG, "picture path 1=: " + file.getAbsolutePath());
                                            Log.d(TAG, "picture path 2=: " + info.getImg_path());
                                            Log.d(TAG, "Content =: " + info.getContent());
                                            Log.d(TAG, "Time =: " + info.getTime());
                                            Log.d(TAG, "Id =: " + info.getUser_id());
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
                    }
                } else {
                    Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
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

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String _content = content.getText().toString().trim();
                if (!TextUtils.isEmpty(_content)) {
                    //上传图片
                    if (img_path == null) {
                        Toast.makeText(mContext, "请选择图片！", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("img_path", img_path);
                        PreviewWTT previewWTT = new PreviewWTT(mContext);
                        previewWTT.setContent(_content);
                        previewWTT.setImgPath(img_path);
                        if (previewWTT != null) {
                            previewWTT.show();

                            WindowManager windowManager = getWindowManager();
                            Display display = windowManager.getDefaultDisplay();
                            WindowManager.LayoutParams lp = previewWTT.getWindow()
                                    .getAttributes();
                            lp.width = (int) (display.getWidth()); // 设置宽度
                            lp.height = (int) display.getHeight();
                            previewWTT.getWindow().setAttributes(lp);
                        }
                    }
                } else {
                    Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
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
                        img_path = path;
                        Bitmap bitmap = BitmapFactory.decodeStream(cr
                                .openInputStream(uri));
                        picture.setImageBitmap(bitmap);
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
                        img_path = null;
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(mContext, AdminHomeActivity.class);
        intent.putExtra("fragment_type", 0);
        startActivity(intent);
    }
}
