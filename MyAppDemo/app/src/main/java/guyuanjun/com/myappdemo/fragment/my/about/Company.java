package guyuanjun.com.myappdemo.fragment.my.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import guyuanjun.com.myappdemo.R;

public class Company extends AppCompatActivity {
    private final String TAG = "Company";

    private ImageView back_company;
    private Context mContext = Company.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_company);

        initView();
        clickListener();
    }

    private void initView(){
        back_company = (ImageView)findViewById(R.id.back_company);
    }

    private void clickListener(){
        back_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, AboutActivity.class);
                startActivity(intent);
            }
        });
    }
}
