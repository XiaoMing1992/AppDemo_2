package guyuanjun.com.myappdemo.admin.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.admin.AddAdminActivity;
import guyuanjun.com.myappdemo.admin.PushNewsActivity;
import guyuanjun.com.myappdemo.admin.PushWeitoutiao;
import guyuanjun.com.myappdemo.admin.Utils;
import guyuanjun.com.myappdemo.utils.ToastUtil;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{
    private Button add_admin;
    private Button push_weitoutiao;
    private Button push_news;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_admin_home, container, false);
        add_admin = (Button)view.findViewById(R.id.add_admin);
        push_weitoutiao = (Button)view.findViewById(R.id.push_weitoutiao);
        push_news = (Button)view.findViewById(R.id.push_news);
        add_admin.setOnClickListener(this);
        push_weitoutiao.setOnClickListener(this);
        push_news.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_admin:
                if (!Utils.isLogin(getActivity())) {
                    ToastUtil.show(getActivity(), "请先登录");
                }
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), AddAdminActivity.class);
                startActivity(intent1);
                break;

            case R.id.push_weitoutiao:
                if (!Utils.isLogin(getActivity())) {
                    ToastUtil.show(getActivity(), "请先登录");
                }
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), PushWeitoutiao.class);
                startActivity(intent2);
                break;

            case R.id.push_news:
                if (!Utils.isLogin(getActivity())) {
                    ToastUtil.show(getActivity(), "请先登录");
                }
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), PushNewsActivity.class);
                startActivity(intent3);
                break;
        }
    }

}
