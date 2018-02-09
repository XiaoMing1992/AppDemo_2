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
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.admin.dialog.PreviewNews;
import guyuanjun.com.myappdemo.bean.BeanConstant;
import guyuanjun.com.myappdemo.bean.NewsItemInfo;
import guyuanjun.com.myappdemo.db.NewsItemInfoDaoOpe;
import guyuanjun.com.myappdemo.fragment.news.ItemViewer;
import guyuanjun.com.myappdemo.utils.UploadUtil;

public class PushNewsActivity extends AppCompatActivity {

    private static final String TAG = "PushNewsActivity";
    private Context mContext = PushNewsActivity.this;

    private Spinner type = null;
    private EditText title;
    private EditText word;
    private EditText where;
    private ImageView img;
    private Button submit;
    private Button preview;

    private LinearLayout layout_img;
    private LinearLayout layout_word;

    private int flag = -1;
    private ProgressBar progress_bar;

    private String picPath = null;

    private final int UPLOAD_FAIL = 0x00;
    private final int UPLOAD_OK = 0x01;

    private long user_id = 1;

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPLOAD_FAIL:
                    progress_bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(mContext, "提交失败", Toast.LENGTH_SHORT).show();
                    break;

                case UPLOAD_OK:
                    progress_bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
                    //测试
                    Intent intent = new Intent();
                    intent.setClass(mContext, PushWeitoutiao.class);
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_news);
        initView();
        listener();
    }

    private void initView() {
        type = (Spinner) findViewById(R.id.type);
        title = (EditText) findViewById(R.id.title);
        word = (EditText) findViewById(R.id.word);
        where = (EditText) findViewById(R.id.where);
        img = (ImageView) findViewById(R.id.img);
        layout_img = (LinearLayout) findViewById(R.id.layout_img);
        layout_word = (LinearLayout) findViewById(R.id.layout_word);
        submit = (Button) findViewById(R.id.submit);
        preview = (Button) findViewById(R.id.preview);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void listener() {
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner   view：显示文字的TextView
            // position：下拉选项的位置从0开始,
            // 0表示: 纯文字，
            // 1表示: 图片，
            // 2表示:文字+图片
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
/*           TextView tvResult = (TextView) findViewById(R.id.tvResult);
             //获取Spinner控件的适配器
             ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
             tvResult.setText(adapter.getItem(position));*/
                if (position == 0) {
                    layout_word.setVisibility(View.VISIBLE);
                    layout_img.setVisibility(View.GONE);
                    flag = BeanConstant.WORD_TYPE;
                } else if (position == 1) {
                    layout_word.setVisibility(View.GONE);
                    layout_img.setVisibility(View.VISIBLE);
                    flag = BeanConstant.IMG_TYPE;
                } else if (position == 2) {
                    flag = BeanConstant.WORD_IMG_TYPE;
                    layout_word.setVisibility(View.VISIBLE);
                    layout_img.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == BeanConstant.INVALID_TYPE) {
                    Toast.makeText(mContext, "请选择内容类型", Toast.LENGTH_SHORT).show();
                } else {
                    final String _title = title.getText().toString();
                    if (TextUtils.isEmpty(_title)) {
                        Toast.makeText(mContext, "标题不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final String _where = where.getText().toString();
                    if (TextUtils.isEmpty(_where)) {
                        Toast.makeText(mContext, "来源不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final Date date = new Date();

                    if (flag == BeanConstant.WORD_TYPE) {
                        String _word = word.getText().toString();
                        if (TextUtils.isEmpty(_word)) {
                            Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        progress_bar.setVisibility(View.VISIBLE);
                        NewsItemInfo newsItemInfo = new NewsItemInfo(user_id, _title, _where, date, flag);
                        newsItemInfo.setContent(_word);
                        NewsItemInfoDaoOpe.getInstance().insertData(mContext, newsItemInfo);
                        mHandler.sendEmptyMessage(UPLOAD_OK);
                    } else if (flag == BeanConstant.IMG_TYPE) {

                        //上传图片
                        if (picPath == null) {
                            Toast.makeText(mContext, "请选择图片！", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("picPath", picPath);
                            final File file = new File(picPath);
                            if (file != null) { //开始写进数据库
                                progress_bar.setVisibility(View.VISIBLE);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        NewsItemInfo newsItemInfo = new NewsItemInfo(user_id, _title, _where, date, flag);
                                        //NewsItemInfoDaoOpe.getInstance().insertData(mContext, newsItemInfo);

                                        int request = UploadUtil.uploadToServer(mContext, file, newsItemInfo);
                                        if (request == 200) {
                                            mHandler.sendEmptyMessage(UPLOAD_OK);
                                        } else {
                                            mHandler.sendEmptyMessage(UPLOAD_FAIL);
                                        }
                                    }
                                }).start();
                            }
                        }

                    } else if (flag == BeanConstant.WORD_IMG_TYPE) {
                        final String _word = word.getText().toString();
                        if (TextUtils.isEmpty(_word)) {
                            Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (picPath == null) {
                            Toast.makeText(mContext, "请选择图片！", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Log.d("picPath", picPath);
                        final File file = new File(picPath);
                        if (file != null) { //开始写进数据库
                            progress_bar.setVisibility(View.VISIBLE);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    NewsItemInfo newsItemInfo = new NewsItemInfo(user_id, _title, _where, date, flag);
                                    newsItemInfo.setContent(_word);
                                    //NewsItemInfoDaoOpe.getInstance().insertData(mContext, newsItemInfo);

                                    int request = UploadUtil.uploadToServer(mContext, file, newsItemInfo); //上传图片
                                    if (request == 200) {
                                        mHandler.sendEmptyMessage(UPLOAD_OK);
                                    } else {
                                        mHandler.sendEmptyMessage(UPLOAD_FAIL);
                                    }
                                }
                            }).start();
                        }
                    }

                }
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == BeanConstant.INVALID_TYPE) {
                    Toast.makeText(mContext, "请选择内容类型", Toast.LENGTH_SHORT).show();
                } else {
                    final String _title = title.getText().toString();
                    if (TextUtils.isEmpty(_title)) {
                        Toast.makeText(mContext, "标题不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final String _where = where.getText().toString();
                    if (TextUtils.isEmpty(_where)) {
                        Toast.makeText(mContext, "来源不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    PreviewNews previewNews = null; //声明
                    if (flag == BeanConstant.WORD_TYPE) {
                        String _word = word.getText().toString();
                        if (TextUtils.isEmpty(_word)) {
                            Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        previewNews = new PreviewNews(mContext, _title, _where, flag);
                        previewNews.setContent(_word);
                    } else if (flag == BeanConstant.IMG_TYPE) {
                        //上传图片
                        if (picPath == null) {
                            Toast.makeText(mContext, "请选择图片！", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("picPath", picPath);
                            previewNews = new PreviewNews(mContext, _title, _where, flag);
                            previewNews.setImgPath(picPath);
                        }
                    } else if (flag == BeanConstant.WORD_IMG_TYPE) {
                        String _word = word.getText().toString();
                        if (TextUtils.isEmpty(_word)) {
                            Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //上传图片
                        if (picPath == null) {
                            Toast.makeText(mContext, "请选择图片！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d("picPath", picPath);
                        previewNews = new PreviewNews(mContext, _title, _where, flag);
                        previewNews.setContent(_word);
                        previewNews.setImgPath(picPath);
                    }

                    if (previewNews != null) {
                        previewNews.show();

                        WindowManager windowManager = getWindowManager();
                        Display display = windowManager.getDefaultDisplay();
                        WindowManager.LayoutParams lp = previewNews.getWindow()
                                .getAttributes();
                        lp.width = (int) (display.getWidth()); // 设置宽度
                        lp.height = (int) display.getHeight();
                        previewNews.getWindow().setAttributes(lp);
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
                    //if (path.endsWith("jpg") || path.endsWith("png")) {
                    picPath = path;
                    Bitmap bitmap = BitmapFactory.decodeStream(cr
                            .openInputStream(uri));
                    img.setImageBitmap(bitmap);
                    //} else {
                    //alert();
                    // }
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(mContext, AdminHomeActivity.class);
        intent.putExtra("fragment_type", 0);
        startActivity(intent);
    }
}
