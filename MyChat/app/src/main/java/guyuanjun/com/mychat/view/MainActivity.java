package guyuanjun.com.mychat.view;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import guyuanjun.com.mychat.R;
import guyuanjun.com.mychat.Utils;
import guyuanjun.com.mychat.adapter.MyAdapter;
import guyuanjun.com.mychat.presenter.Server;

public class MainActivity extends AppCompatActivity {

    private Button left;
    private Button right;
    private ListView listView;
    private MyAdapter myAdapter;
    private List<Map<String, ?>> data;

    private int i = 0;
    private int j = 0;

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StatusCode.FAIL:

                    break;

                case StatusCode.SUCCESS:
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //listener();
        initData();
        insertData();
    }

    private void initView() {
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);
        listView = (ListView) findViewById(R.id.content);
    }

    private void initData() {
        left.setText("left" + Utils.getIPAddress(MainActivity.this));
        right.setText("right" + Utils.getIPAddress(MainActivity.this));

        data = new ArrayList<>();
        myAdapter = new MyAdapter(MainActivity.this, data);
        listView.setAdapter(myAdapter);
    }

    private void listener() {
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map = new HashMap();
                map.put("content", "leftleftleftleftleftleftleft" + (i++));
                map.put("id", 0);
                data.add(map);
                myAdapter.notifyDataSetChanged();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map = new HashMap();
                map.put("content", "rightrightrightright" + (j++));
                map.put("id", 1);
                data.add(map);
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    private void insertData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket = Server.getInstance().getServerSocket();
                if (serverSocket != null) {
                    Socket socket = null;
                    Log.d("server", "已经开启socket连接" );
                    while (true) {
                        try {
                            socket = serverSocket.accept();
                            Log.d("server", "已经开启socket连接=============" );
                            if (socket != null && socket.isConnected()) {
                                Log.d("server", "socket连接成功" );
                                //BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
                                InputStreamReader reader = new InputStreamReader(socket.getInputStream(), "utf-8");
                                BufferedReader bufferedReader = new BufferedReader(reader);

                                //byte[] b = new byte[4*1024];
                                StringBuffer buffer = new StringBuffer();
                                while (bufferedReader.read() != -1){
                                    buffer.append(bufferedReader.readLine());
                                }
                                Log.d("server", "" + buffer.toString()+" ip="+socket.getInetAddress());

                                Map map = new HashMap();
                                map.put("content", "" + buffer.toString());
                                map.put("id", 1);
                                data.add(map);
                                handler.sendEmptyMessage(StatusCode.SUCCESS);
                            }else{
                                Log.d("server", "socket连接失败" );
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();


    }
}
