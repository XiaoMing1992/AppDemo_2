package guyuanjun.com.mychat.view;

import android.content.Intent;
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

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import guyuanjun.com.mychat.R;
import guyuanjun.com.mychat.Utils;
import guyuanjun.com.mychat.adapter.MyAdapter;
import guyuanjun.com.mychat.model.MyMessage;
import guyuanjun.com.mychat.model.MySQLiteUtil;
import guyuanjun.com.mychat.presenter.Server;
import guyuanjun.com.mychat.presenter.ServerService;

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
    //private List<Map<String, ?>> data;
    List<MyMessage> data;

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
        //insertData();
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

        //开启服务，监听客户端的连接请求
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ServerService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);

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
                    //final Socket socket;
                    Log.d("server", "已经开启socket连接");
                    System.out.println("server" + "已经开启socket连接");
                    Socket socket = null;
                    while (true) {
                        try {
                            //final Socket socket = serverSocket.accept();
                            socket = serverSocket.accept();

                            Log.d("server", "已经开启socket连接=============");
                            System.out.println("server" + "已经开启socket连接=============");
                            if (socket != null && socket.isConnected() && !socket.isClosed()) {
                                Log.d("server", "socket连接成功" + socket.getInetAddress().getHostAddress());
                                System.out.println("server " + "socket连接成功" + socket.getInetAddress().getHostAddress());

/*                                PrintWriter out = null;
                                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);
                                out.write("hello " + socket.getInetAddress().getHostAddress());
                                //out.write(msg.getBytes());
                                out.flush();*/

                                //out.close();
                                //socket.close();

//                                OutputStream outputStream = socket.getOutputStream();
//                                outputStream.write(("hello "+socket.getInetAddress().getHostAddress()).getBytes("utf-8"));

                                //if (!mClientList.contains(socket))
                                mClientList.add(socket); //记录成功连接的客户端

                                Map ip_map = new HashMap();
                                ip_map.put("ip", socket.getInetAddress().getHostAddress());
                                ips.add(ip_map);

                                mExecutorService.execute(new SocketRunnable(socket));
/*                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Log.d("server", "socket连接成功" + socket.getInetAddress().getHostAddress());
                                            mClientList.add(socket); //记录成功连接的客户端
                                            Map ip_map = new HashMap();
                                            ip_map.put("ip", socket.getInetAddress().getHostAddress());
                                            ips.add(ip_map);

                                            //BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
                                            InputStreamReader reader = new InputStreamReader(socket.getInputStream(), "utf-8");
                                            BufferedReader bufferedReader = new BufferedReader(reader);

                                            //byte[] b = new byte[4*1024];
                                            StringBuffer buffer = new StringBuffer();
                                            String str = null;
                                            while ((str = bufferedReader.readLine()) != null) {
                                                System.out.println(str);//此时str就保存了一行字符串
                                                buffer.append(str);
                                            }
//                                while (bufferedReader.read() != -1) {
//                                    buffer.append(bufferedReader.readLine());
//                                }
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
//                                MyMessage myMessage = new Gson().fromJson(buffer.toString(), MyMessage.class);
//                                String to_id = myMessage.getTo();
//                                String msg = myMessage.getMsg();
//                                Log.d("fromClient", "to_id = " + to_id + " msg=" + msg);


                                            JSONObject msgJson = new JSONObject(buffer.toString());
                                            String to_id = msgJson.getString("to");
                                            String msg = msgJson.getString("msg");
                                            Log.d("fromClient", "to_id = " + to_id + " msg=" + msg);
                                            if (to_id != null) {
                                                boolean flag = false;
                                                for (Socket client : mClientList) {
                                                    if (flag) break;
                                                    for (Map my_ip_map : ips) {
                                                        Log.d("fromClient", "my_ip_map = " + my_ip_map.get("ip"));
                                                        if (to_id.equals(my_ip_map.get("ip"))) {
                                                            Log.d("toClient", "to_id = " + to_id + "  msg=" + msg + "  start");
                                                            PrintWriter out = null;
                                                            out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "utf-8"), true);
                                                            out.write(msg);
                                                            //out.write(msg.getBytes());
                                                            out.flush();
                                                            flag = true;
                                                            Log.d("toClient", "to_id = " + to_id + "  msg=" + msg + "  end");
                                                            break;
                                                        }
                                                    }
                                                }
                                            } else {
                                                Log.d("server", "ip 不能为空");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } finally {

                                        }

                                    }
                                }).start();*/

                            } else {
                                Log.d("server", "socket连接失败");
                            }
                        }
//                        catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();


    }

    class SocketRunnable implements Runnable {
        Socket mSocket;

        public SocketRunnable(Socket socket) {
            mSocket = socket;
        }

        @Override
        public void run() {
            try {
                //BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
                InputStreamReader reader = new InputStreamReader(mSocket.getInputStream(), "utf-8");
                BufferedReader bufferedReader = new BufferedReader(reader);

                //byte[] b = new byte[4*1024];
                StringBuffer buffer = new StringBuffer();
                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                    System.out.println(str);//此时str就保存了一行字符串
                    buffer.append(str);
                }
//                                while (bufferedReader.read() != -1) {
//                                    buffer.append(bufferedReader.readLine());
//                                }
                Log.d("fromClient", "" + buffer.toString() + " ip=" + mSocket.getInetAddress().getHostAddress());
                System.out.println("fromClient" + "" + buffer.toString() + " ip=" + mSocket.getInetAddress().getHostAddress());

                if (buffer.toString().isEmpty()) return;

//                Map map = new HashMap();
//                map.put("content", "" + buffer.toString());
//                map.put("id", 0);
//                data.add(map);
//                handler.sendEmptyMessage(StatusCode.SUCCESS);

//                                PrintWriter out = null;
//                                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);
//                                out.write(buffer.toString());
//                                //out.write(msg.getBytes());
//                                out.flush();
//                                MyMessage myMessage = new Gson().fromJson(buffer.toString(), MyMessage.class);
//                                String to_id = myMessage.getTo();
//                                String msg = myMessage.getMsg();
//                                Log.d("fromClient", "to_id = " + to_id + " msg=" + msg);


                JSONObject msgJson = new JSONObject(buffer.toString());
                String from_id = msgJson.getString("from");
                String to_id = msgJson.getString("to");
                String msg = msgJson.getString("msg");


                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                String timeStr = sdf.format(date);
                MyMessage myMessage = new MyMessage();
                myMessage.setFrom(from_id);
                myMessage.setTo(to_id);
                myMessage.setMsg(msg);
                myMessage.setTime(timeStr);
                long id = MySQLiteUtil.insert(MainActivity.this, myMessage); //插入到数据库里面
                //if (id == -1) return;

//                Map map = new HashMap();
//                map.put("content", "" + buffer.toString());
//                map.put("id", 0);
//                data.add(map);

                data.add(myMessage);
                handler.sendEmptyMessage(StatusCode.SUCCESS);

                Log.d("fromClient", timeStr+"from_id = " + from_id +" to_id = " + to_id + " msg=" + msg);
                System.out.println("fromClient " +timeStr+ " from_id = " + from_id+" to_id = " + to_id + " msg=" + msg);

                Map ip_map = new HashMap();
                ip_map.put("ip", from_id);
                ips.add(ip_map);

                if (mSocket != null && mSocket.isConnected() && !mSocket.isClosed()) {
                    Log.d("fromServer", "向from_id = " + from_id +" 写 msg=" + msg);
                    System.out.println("=======================  向from_id = " + from_id +" 写 msg=" + msg+" start");

                    PrintWriter out = null;
                    out = new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream(), "utf-8"), true);
                    //封装成json
                    JSONObject json = new JSONObject();
                    json.put("id", id);
                    json.put("from", from_id);
                    json.put("to", to_id);
                    json.put("msg", msg);
                    json.put("time", timeStr);

                    //通过BufferedWriter对象向服务器写数据
                    out.write(json.toString() + "\n");

                    out.flush();
                    out.close();
                    mSocket.close();
                    mSocket = null;
                    System.out.println("=======================  向from_id = " + from_id +" 写 msg=" + msg+" end");
                }

                if (to_id != null) {
                    boolean flag = false;
                    System.out.println("size=" + mClientList.size());
                    for (int i = 0; i < mClientList.size(); i++) {
                        Socket client = mClientList.get(i);

/*                        if (client != null && client.isConnected() && !client.isClosed()) {
                            System.out.println("i=" + i + "client isClose=" + client.isClosed() + " isConnect=" + client.isConnected() + client.getInetAddress().getHostAddress());


                            PrintWriter out = null;
                            out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "utf-8"), true);
                            out.write(msg);
                            //out.write(msg.getBytes());
                            out.flush();
                            out.close();
                            client.close();
                            client = null;
                        }*/


                        //for (Socket client : mClientList) {
                        //if (flag) break;

                        if (client != null && client.isConnected() && !client.isClosed()) {
                            System.out.println("i=" + i + "client isClose=" + client.isClosed() + " isConnect=" + client.isConnected() + client.getInetAddress().getHostAddress());


                            //for (Map my_ip_map : ips) {
                            for (int j=0;j<ips.size();j++){
                                Map my_ip_map = ips.get(j);
                                Log.d("fromClient", "my_ip_map = " + my_ip_map.get("ip"));
                                System.out.println("fromClient" + "my_ip_map = " + my_ip_map.get("ip"));
                                if (to_id.equals(my_ip_map.get("ip"))) {
//                                    String[] arr_str = ((String) my_ip_map.get("ip")).split("_");
//                                    if (arr_str.length <= 0) continue;
//                                    if (!client.getInetAddress().getHostAddress().equals(arr_str[0])){
//                                        continue;
//                                    }

                                    if (!client.getInetAddress().getHostAddress().equals(to_id)) {
                                        continue;
                                    }

                                    Log.d("toClient", "from_id = " + from_id +"to_id = " + to_id + "  msg=" + msg + "  start");
                                    System.out.println("toClient" + " from_id = " + from_id +" to_id = " + to_id + "  msg=" + msg + "  start");
//                                if (client == null || client.isClosed()) {
//                                    Log.d("toClient", "to_id = " + to_id + "  msg=" + msg + "  continue");
//                                    System.out.println("toClient" + "to_id = " + to_id + "  msg=" + msg + "  continue");
//                                    mClientList.remove(i);
//                                    i = i > 0 ? (i - 1) : 0;
//
//                                    continue;
//                                }


                                    PrintWriter out = null;
                                    out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "utf-8"), true);
                                    //out.write(msg);
                                    //out.write(msg.getBytes());

                                    //封装成json
                                    JSONObject json = new JSONObject();
                                    json.put("id", id);
                                    json.put("from", from_id);
                                    json.put("to", to_id);
                                    json.put("msg", msg);
                                    json.put("time", timeStr);
                                    //通过BufferedWriter对象向服务器写数据
                                    out.write(json.toString() + "\n");

                                    out.flush();
                                    out.close();
                                    client.close();
                                    client = null;
                                    break;
                                }
                                Log.d("toClient", " from_id = " + from_id +"to_id = " + to_id + "  msg=" + msg + "  end");
                                System.out.println("toClient" + " from_id = " + from_id +"to_id = " + to_id + "  msg=" + msg + "  end");
//
//                                PrintWriter out = null;
//                                out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "utf-8"), true);
//                                out.write(msg);
//                                //out.write(msg.getBytes());
//                                out.flush();
                                //out.close();
                                //client.close();
                                //mClientList.remove(i);


                                //flag = true;
//                                Log.d("toClient", "to_id = " + to_id + "  msg=" + msg + "  end");
//                                System.out.println("toClient" + "to_id = " + to_id + "  msg=" + msg + "  end");
                                //break;
                            }
                        }
                        //}
                    }
                } else {
                    Log.d("server", "ip 不能为空");
                }

                //mSocket.close();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                mClientList.remove(mSocket);
            }

        }
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
