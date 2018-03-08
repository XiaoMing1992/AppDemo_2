package guyuanjun.com.mychat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import guyuanjun.com.mychat.adapter.MyAdapter;

public class MainActivity extends AppCompatActivity {

    private Button left;
    private Button right;
    private ListView listView;
    private MyAdapter myAdapter;
    private List<Map<String, ?>> data;

    private int i=0;
    private int j=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        listener();
        initData();
    }

    private void initView(){
        left = (Button)findViewById(R.id.left);
        right = (Button)findViewById(R.id.right);
        listView = (ListView)findViewById(R.id.content);
    }

    private void initData(){
        left.setText("left"+Utils.getIPAddress(MainActivity.this));
        right.setText("right"+Utils.getIPAddress(MainActivity.this));

        data = new ArrayList<>();
        myAdapter = new MyAdapter(MainActivity.this, data);
        listView.setAdapter(myAdapter);
    }

    private void listener(){
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map = new HashMap();
                map.put("content", "leftleftleftleftleftleftleft"+(i++));
                map.put("id", 0);
                data.add(map);
                myAdapter.notifyDataSetChanged();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map = new HashMap();
                map.put("content", "rightrightrightright"+(j++));
                map.put("id", 1);
                data.add(map);
                myAdapter.notifyDataSetChanged();
            }
        });
    }
}
