package guyuanjun.com.myappdemo.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.adapter.item.GridViewAdapter;
import guyuanjun.com.myappdemo.bean.BeanConstant;
import guyuanjun.com.myappdemo.bean.NewsItemInfo;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.bean.WeitoutiaoInfo;
import guyuanjun.com.myappdemo.db.NewsItemInfoDaoOpe;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.db.WeitoutiaoInfoDaoOpe;
import guyuanjun.com.myappdemo.picture.Bimp;
import guyuanjun.com.myappdemo.picture.FileUtils;
import guyuanjun.com.myappdemo.picture.PhotoActivity;
import guyuanjun.com.myappdemo.picture.TestPicActivity;
import guyuanjun.com.myappdemo.utils.LoadCode;
import guyuanjun.com.myappdemo.utils.LogUtil;

public class MyPushWeitoutiaoActivity extends AppCompatActivity {
    private static final String TAG = MyPushWeitoutiaoActivity.class.getSimpleName();
    private Context mContext = MyPushWeitoutiaoActivity.this;

    private GridView noScrollgridview;
    private GridAdapter adapter;
    private TextView activity_selectimg_send;
    private EditText tv_content;
    //private EditText title;
    //private EditText where;
    private ProgressBar progress_bar;

    private long id = 1;

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LoadCode.LOADING:
                    progress_bar.setVisibility(View.VISIBLE);
                    break;

                case LoadCode.FAIL:
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "提交失败", Toast.LENGTH_SHORT).show();
                    break;

                case LoadCode.SUCCESS:
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
                    //删除本地保存数据库的数据
                    editor.clear();
                    editor.commit();

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

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_push_weitoutiao);
        sharedPreferences = mContext.getSharedPreferences("save_weitoutiao", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void Init() {
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.bmp.size()) {
                    new PopupWindows(MyPushWeitoutiaoActivity.this, noScrollgridview);
                } else {
                    Intent intent = new Intent(MyPushWeitoutiaoActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
        activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);
        activity_selectimg_send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final String img_path_list;
                String temp_img_path_list = "";
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < Bimp.drr.size(); i++) {
                    String Str = Bimp.drr.get(i).substring(
                            Bimp.drr.get(i).lastIndexOf("/") + 1,
                            Bimp.drr.get(i).lastIndexOf("."));

                    //String time = ""+System.currentTimeMillis();
                    //list.add(FileUtils.SDPATH+Str+time+".jpeg");
                    //list.add(FileUtils.SDPATH + FileUtils.WEITOUTIAOPATH + time + ".jpeg");
                    list.add(FileUtils.SDPATH + FileUtils.WEITOUTIAOPATH + Str + ".jpeg");
                    //temp_img_path_list += FileUtils.SDPATH+Str+".jpeg";
                    //temp_img_path_list += FileUtils.SDPATH + FileUtils.WEITOUTIAOPATH + time + ".jpeg";
                    temp_img_path_list += FileUtils.SDPATH + FileUtils.WEITOUTIAOPATH + Str + ".jpeg";
                    if (i != Bimp.drr.size() -1){
                        temp_img_path_list += GridViewAdapter.SPLIT_STR;
                    }
                }
                img_path_list = temp_img_path_list;

                final String word = tv_content.getText().toString().trim();
                //final String _title = title.getText().toString().trim();
                //final String _where = where.getText().toString().trim();
                Log.i("word", word);
                //Log.i("_title", _title);
                //Log.i("_where", _where);
                Log.i("picture_path ", list.toString());
                Log.i("img_path_list ", img_path_list);
                for (int i = 0; i < list.size(); i++) {
                    File file = new File(list.get(i));
                    Log.i("picture_path", file.getAbsolutePath());
                }

                if (TextUtils.isEmpty(word) && img_path_list.isEmpty()) {

                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(LoadCode.LOADING);

                            List<UserInfo> uses = UserInfoDaoOpe.getInstance().query(mContext, id);
                            if (uses.size() == 0)
                                return;
                            //String head_path = uses.get(0).getHead_path();
                            String username = uses.get(0).getUsername();
                            WeitoutiaoInfo info = new WeitoutiaoInfo(id, username, new Date(), 0);
                            info.setContent(word);
                            info.setImg_path_list(img_path_list);
                            WeitoutiaoInfoDaoOpe.getInstance().insertData(mContext, info);

                            //清空数据
                            Bimp.max = 0;
                            Bimp.act_bool = true;
                            Bimp.drr.clear();
                            Bimp.bmp.clear();
                            mHandler.sendEmptyMessage(LoadCode.SUCCESS);
                        }
                    }).start();
                }


                // 高清的压缩图片全部就在  list 路径里面了
                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
                // 完成上传服务器后 .........
                //FileUtils.deleteDir();
            }
        });
        tv_content = (EditText)findViewById(R.id.tv_content);
        //title = (EditText) findViewById(R.id.title);
        //where = (EditText) findViewById(R.id.where);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        String save_word = sharedPreferences.getString("word", null);
        if (save_word != null && !save_word.isEmpty()){
            tv_content.setText(save_word);

            tv_content.setSelection(save_word.length());//EditText光标移动到文本框末尾
            tv_content.setFocusable(true);
            tv_content.setFocusableInTouchMode(true);
            tv_content.requestFocus();
        }
    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        private int selectedPosition = -1;// 选中的位置
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            return (Bimp.bmp.size() + 1);
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        /**
         * ListView Item设置
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            final int coord = position;
            ViewHolder holder = null;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.bmp.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.bmp.get(position));
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.drr.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            try {
                                String path = Bimp.drr.get(Bimp.max);
                                System.out.println(path);
                                Bitmap bm = Bimp.revitionImageSize(path);
                                Bimp.bmp.add(bm);
                                String newStr = path.substring(
                                        path.lastIndexOf("/") + 1,
                                        path.lastIndexOf("."));
                                FileUtils.saveBitmap(bm, FileUtils.WEITOUTIAOPATH, "" + newStr);
                                Bimp.max += 1;
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.item_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    photo();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    editor.putString("word", tv_content.getText().toString()); //保存内容到本地
                    editor.commit();

                    Intent intent = new Intent(MyPushWeitoutiaoActivity.this,
                            TestPicActivity.class);
                    intent.putExtra("activity", "MyPushWeitoutiaoActivity");
                    startActivity(intent);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory()
                + "/myimage/", String.valueOf(System.currentTimeMillis())
                + ".jpg");
        path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.drr.size() < 9 && resultCode == -1) {
                    Bimp.drr.add(path);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        for (int i = 0; i < Bimp.drr.size(); i++) {
            String Str = Bimp.drr.get(i).substring(
                    Bimp.drr.get(i).lastIndexOf("/") + 1,
                    Bimp.drr.get(i).lastIndexOf("."));

            if (FileUtils.deleteFile(FileUtils.SDPATH + FileUtils.WEITOUTIAOPATH + Str + ".jpeg")){
                LogUtil.d(TAG, "pushWeitoutiao delete "+FileUtils.SDPATH + FileUtils.WEITOUTIAOPATH + Str + ".jpeg" + "    OK!");
            }
        }
    }
}
