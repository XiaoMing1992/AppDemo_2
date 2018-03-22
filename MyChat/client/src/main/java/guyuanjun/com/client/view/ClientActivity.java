package guyuanjun.com.client.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import guyuanjun.com.client.R;
import guyuanjun.com.client.adapter.MyAdapter;
import guyuanjun.com.client.presenter.IPresenter;
import guyuanjun.com.client.presenter.PresenterComp;

public class ClientActivity extends AppCompatActivity implements View.OnClickListener, IView{
    private TextView ip;
    private EditText to_id;
    private Button send;
    private EditText input;
    private ListView listView;
//    private MyAdapter myAdapter;
//    private List<Map<String, ?>> data;

    private IPresenter iPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        initView();
        init();
    }

    private void initView(){
        ip = (TextView)findViewById(R.id.ip);
        input = (EditText)findViewById(R.id.input);
        to_id = (EditText)findViewById(R.id.to_id);
        send = (Button)findViewById(R.id.send);
        listView = (ListView) findViewById(R.id.content);
        send.setOnClickListener(this);

    }

    private void init(){
        iPresenter = new PresenterComp(this);
        listView.setAdapter(iPresenter.getMyAdapter());
        ip.setText(iPresenter.getIp());
        iPresenter.getServerMsg(); //开启线程监听服务器发送来的信息

//        data = new ArrayList<>();
//        myAdapter = new MyAdapter(ClientActivity.this, data);
//        listView.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.send:
                iPresenter.sengMsg(to_id.getText().toString().trim(), input.getText().toString().trim());
                Log.d("client", input.getText().toString().trim());
//                boolean res = iPresenter.sengMsg(input.getText().toString().trim());
//                if (res) {
//                    iPresenter.clear();
//                }
                iPresenter.clear();
                break;
        }
    }

    @Override
    public void onClearText() {
        Toast.makeText(ClientActivity.this, input.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}
