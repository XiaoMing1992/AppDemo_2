package guyuanjun.com.myappdemo.admin.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.admin.AdminHomeActivity;
import guyuanjun.com.myappdemo.admin.AdminLoginActivity;
import guyuanjun.com.myappdemo.admin.Constant;
import guyuanjun.com.myappdemo.admin.Utils;
import guyuanjun.com.myappdemo.bean.AdminInfo;
import guyuanjun.com.myappdemo.db.AdminInfoDaoOpe;
import guyuanjun.com.myappdemo.utils.PrefUtils;

/**
 * Created by Administrator on 2017/8/26 0026.
 */

public class AdminCenterFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    private Button logout;
    private TextView email;
    private RelativeLayout modify_password;
    private RelativeLayout modify_email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.admin_center, container, false);
        email = (TextView)view.findViewById(R.id.email);
        modify_password = (RelativeLayout)view.findViewById(R.id.modify_password);
        modify_email = (RelativeLayout)view.findViewById(R.id.modify_email);
        logout = (Button)view.findViewById(R.id.btn_logout);
        clickListener();
        initData();
        return view;
    }

    private void initData(){
        String name = Utils.getString(Constant.SAVE_ADMIN_INFO_NAME, Constant.ADMIN_NAME_KEY, getActivity());
        long id = Utils.getLong(Constant.SAVE_ADMIN_INFO_NAME, Constant.ADMIN_ID_KEY, getActivity());
        List<AdminInfo> adminInfos = AdminInfoDaoOpe.getInstance().query(getActivity(), id);
        if (adminInfos != null){
            email.setText(adminInfos.get(0).getEmail());
        }
    }

    private void clickListener(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.remove(Constant.ADMIN_SESSION_FILE, Constant.ADMIN_SESSION_ID, getActivity()); //删除会话
                Utils.remove(Constant.SAVE_ADMIN_INFO_NAME, Constant.ADMIN_NAME_KEY, getActivity());
                Utils.remove(Constant.SAVE_ADMIN_INFO_NAME, Constant.ADMIN_ID_KEY, getActivity());

                Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(getActivity(), AdminLoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        modify_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        modify_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
