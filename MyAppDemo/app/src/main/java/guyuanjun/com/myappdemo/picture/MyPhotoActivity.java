package guyuanjun.com.myappdemo.picture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.Utils;

public class MyPhotoActivity extends AppCompatActivity {
    private final String TAG = MyPhotoActivity.class.getSimpleName();
    private ArrayList<View> listViews = null;
    private ViewPager pager;
    private MyPageAdapter adapter;
    private TextView position;
    private int count;

    public List<Bitmap> bmp = new ArrayList<Bitmap>();
    //public List<String> drr = new ArrayList<String>();
    //public List<String> del = new ArrayList<String>();
    //public int max;

    RelativeLayout photo_relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_photo);

        photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
        photo_relativeLayout.setBackgroundColor(0xff000000);
        position = (TextView) findViewById(R.id.position);

//        for (int i = 0; i < Bimp.bmp.size(); i++) {
//            bmp.add(Bimp.bmp.get(i));
//        }
//        for (int i = 0; i < Bimp.drr.size(); i++) {
//            drr.add(Bimp.drr.get(i));
//        }
//        max = Bimp.max;

//        Button photo_bt_exit = (Button) findViewById(R.id.photo_bt_exit);
//        photo_bt_exit.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                finish();
//            }
//        });
//        Button photo_bt_del = (Button) findViewById(R.id.photo_bt_del);
//        photo_bt_del.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (listViews.size() == 1) {
//                    Bimp.bmp.clear();
//                    Bimp.drr.clear();
//                    Bimp.max = 0;
//                    FileUtils.deleteDir();
//                    finish();
//                } else {
//                    String newStr = drr.get(count).substring(
//                            drr.get(count).lastIndexOf("/") + 1,
//                            drr.get(count).lastIndexOf("."));
//                    bmp.remove(count);
//                    drr.remove(count);
//                    del.add(newStr);
//                    max--;
//                    pager.removeAllViews();
//                    listViews.remove(count);
//                    adapter.setListViews(listViews);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });
//        Button photo_bt_enter = (Button) findViewById(R.id.photo_bt_enter);
//        photo_bt_enter.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//
//                Bimp.bmp = bmp;
//                Bimp.drr = drr;
//                Bimp.max = max;
//                for(int i=0;i<del.size();i++){
//                    FileUtils.delFile(del.get(i)+".JPEG");
//                }
//                finish();
//            }
//        });

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        LogUtil.d(TAG, "id = "+id);

        try {
            String[] img_paths = intent.getStringArrayExtra("img_path_list");
            if (img_paths != null) {
                for (int i = 0; i < img_paths.length; i++) {
                    Bitmap bitmap = Utils.getInstance().getPicture(img_paths[i]);
                    if (bitmap != null)
                        bmp.add(bitmap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        pager = (ViewPager) findViewById(R.id.viewpager);
        //pager.setOnPageChangeListener(pageChangeListener);
        pager.addOnPageChangeListener(pageChangeListener);
        for (int i = 0; i < bmp.size(); i++) {
            initListViews(bmp.get(i));//
        }

        adapter = new MyPageAdapter(listViews);// 构造adapter
        pager.setAdapter(adapter);// 设置适配器

        pager.setCurrentItem(id);
        if (!listViews.isEmpty())
            position.setText("" + (id + 1) + "/" + count);
    }

    private void initListViews(Bitmap bm) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        ImageView img = new ImageView(this);// 构造ImageView对象
        img.setBackgroundColor(0xff000000);
        img.setImageBitmap(bm);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        listViews.add(img);// 添加view
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        public void onPageSelected(int current) {// 页面选择响应函数
            //count = arg0;
            LogUtil.d(TAG, "onPageSelected current=" + current);
            position.setText("" + (current + 1) + "/" + count);
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

        }

        public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

        }
    };

    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;// content

        private int size;// 页数

        public MyPageAdapter(ArrayList<View> listViews) {// 构造函数
            // 初始化viewpager的时候给的一个页面
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
            count = size;
        }

        public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {// 返回数量
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
            ((ViewPager) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {// 返回view对象
            try {
                ((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    @Override
    public void onBackPressed() {
        for (int i = 0; i < bmp.size(); i++) {
            bmp.get(i).recycle();
        }
        finish();
        super.onBackPressed();
    }
}
