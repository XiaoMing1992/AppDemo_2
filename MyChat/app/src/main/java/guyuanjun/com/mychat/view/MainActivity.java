package guyuanjun.com.mychat.view;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import guyuanjun.com.mychat.R;
import guyuanjun.com.mychat.Utils;
import guyuanjun.com.mychat.adapter.MyAdapter;
import guyuanjun.com.mychat.presenter.Server;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Socket> mClientList = new ArrayList<Socket>(); //记录连接上服务器的客户端
    private ExecutorService mExecutorService;                             //创建线程池来管理
    private ArrayList<Map<String, String>> ips = new ArrayList<>(); //记录IP地址

    private Button send;
    private EditText input;
    private Button left;
    private Button right;
    private ListView listView;
    private MyAdapter myAdapter;
    private List<Map<String, ?>> data;

    private int i = 0;
    private int j = 0;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
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
        listener();
        initData();
        insertData();
    }

    private void initView() {
        input = (EditText) findViewById(R.id.input);
        send = (Button) findViewById(R.id.send);
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

        mExecutorService = Executors.newCachedThreadPool();
    }

    private void listener() {
//        left.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Map map = new HashMap();
//                map.put("content", "leftleftleftleftleftleftleft" + (i++));
//                map.put("id", 0);
//                data.add(map);
//                myAdapter.notifyDataSetChanged();
//            }
//        });
//
//        right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Map map = new HashMap();
//                map.put("content", "rightrightrightright" + (j++));
//                map.put("id", 1);
//                data.add(map);
//                myAdapter.notifyDataSetChanged();
//            }
//        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                    Log.d("server", "已经开启socket连接");
                    while (true) {
                        try {
                            socket = serverSocket.accept();
                            Log.d("server", "已经开启socket连接=============");
                            if (socket != null && socket.isConnected()) {
                                Log.d("server", "socket连接成功");
                                mClientList.add(socket); //记录成功连接的客户端
                                Map ip_map = new HashMap();
                                ip_map.put("ip", socket.getInetAddress().getHostAddress());
                                ips.add(ip_map);

                                //BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
                                InputStreamReader reader = new InputStreamReader(socket.getInputStream(), "utf-8");
                                BufferedReader bufferedReader = new BufferedReader(reader);

                                //byte[] b = new byte[4*1024];
                                StringBuffer buffer = new StringBuffer();
                                while (bufferedReader.read() != -1) {
                                    buffer.append(bufferedReader.readLine());
                                }
                                Log.d("fromClient", "" + buffer.toString() + " ip=" + socket.getInetAddress().getHostAddress());

                                Map map = new HashMap();
                                map.put("content", "" + buffer.toString());
                                map.put("id", 0);
                                data.add(map);
                                handler.sendEmptyMessage(StatusCode.SUCCESS);

//                                PrintWriter out = null;
//                                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);
//                                out.write(buffer.toString());
//                                //out.write(msg.getBytes());
//                                out.flush();

                                JSONObject msgJson = new JSONObject(buffer.toString());
                                String to_id = msgJson.getString("to");
                                String msg = msgJson.getString("msg");
                                if (to_id != null) {
                                    boolean flag = false;
                                    for (Socket client : mClientList) {
                                        if (flag) break;
                                        for (Map my_ip_map : ips) {
                                            if (to_id.equals(my_ip_map.get("ip"))) {
                                                PrintWriter out = null;
                                                out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "utf-8"), true);
                                                out.write(msg);
                                                //out.write(msg.getBytes());
                                                out.flush();
                                                flag = true;
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    Log.d("server", "ip 不能为空");
                                }
                            } else {
                                Log.d("server", "socket连接失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();


    }

    //发送消息给每一个连接的客户端
//    private void sendMessageToClient() {
//        try {
//            for (Socket client : mClientList) {
//                printWriter = new PrintWriter(client.getOutputStream());
//                printWriter.println(message);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
