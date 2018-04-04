package guyuanjun.com.client.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import guyuanjun.com.client.Utils;
import guyuanjun.com.client.adapter.MyAdapter;
import guyuanjun.com.client.model.MyMessage;
import guyuanjun.com.client.model.MySQLiteUtil;
import guyuanjun.com.client.view.IView;
import guyuanjun.com.client.view.StatusCode;

/**
 * Created by HP on 2018-3-14.
 */

public class PresenterComp implements IPresenter {
    IView iView;
    static Handler handler;
    boolean res = false;
    //List<Map<String, ?>> data;
    List<MyMessage> data;
    MyAdapter myAdapter;

    ExecutorService mExecutorService;
    Socket socket = null;
    String ip = null;

    MyBroadcastReceiver myBroadcastReceiver;

    public PresenterComp(final IView iView) {
        mExecutorService = Executors.newCachedThreadPool();
        this.iView = iView;
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case StatusCode.FAIL:
                        //setSendState(false);
                        //Toast.makeText((Activity) iView, "发送数据失败", Toast.LENGTH_SHORT).show();
                        broadcastNetwork("发送数据失败");
                        break;

                    case StatusCode.SUCCESS:
                        //setSendState(true);
                        myAdapter.notifyDataSetChanged();
                        break;

                    case StatusCode.CONNECT_FAIL:
                        //Toast.makeText((Activity) iView, "连接服务器失败", Toast.LENGTH_SHORT).show();
                        broadcastNetwork("连接服务器失败");
                        break;

                    case StatusCode.CONNECT_SUCCESS:
                        //Toast.makeText((Activity) iView, "连接服务器成功", Toast.LENGTH_SHORT).show();
                        broadcastNetwork("连接服务器成功");
                        break;

                    case StatusCode.GET_IP_SUCCESS:
                        ip = (String) msg.obj;
                        returnIP(ip);
                        break;


                    case StatusCode.CONNECT_NETWORK_SUCCESS:
                        getIp();
                        if (Utils.getConnect())
                            broadcastNetwork("网络连接成功");
                        // if (getApplication() != null && getApplication().getApplicationContext()!=null)
                        //Toast.makeText(((Activity) iView).getApplicationContext(), "网络连接成功", Toast.LENGTH_SHORT).show();
                        break;
                    case StatusCode.CONNECT_NETWORK_FAIL:
                        getIp();
                        if (!Utils.getConnect())
                            broadcastNetwork("网络已经断开");
                        //if (getApplication() != null && getApplication().getApplicationContext()!=null)
                        //Toast.makeText(((Activity) iView).getApplicationContext(), "网络已经断开", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        };

        data = new ArrayList<>();
        myAdapter = new MyAdapter((Activity) iView, data);

        myBroadcastReceiver = new MyBroadcastReceiver();
        registerNetworkListener((Activity) iView);

        getIp();
        getLocalMsgs(); //获取存储在本地的聊天记录
        getServerMsg(); //开启线程监听服务器发送来的信息
    }

    private void returnIP(final String ip) {
        iView.getIp(ip);
    }

    @Override
    public void clear() {
        iView.onClearText();
    }

    public void setSendState(boolean res) {
        this.res = res;
    }

    public boolean getSendState() {
        return res;
    }

    @Override
    public MyAdapter getMyAdapter() {
        return myAdapter;
    }

    @Override
    public void sengMsg(final String to_id, final String msg) {
        if (!iView.handleInput(to_id) || !iView.handleInput(msg)) return;

        String from_id = getFromIp();
        System.out.println("=============  sengMsg()  from_id=" + from_id);
        mExecutorService.execute(new SendRunnable(socket, from_id, to_id, msg));
/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = Client.getInstance().getClientSocket();
                if (socket != null) {
                    Log.d("client", " ip=" + socket.getInetAddress().getHostAddress()+" 连接服务器成功");
                    //BufferedOutputStream out = null;
                    PrintWriter out = null;
                    try {
                        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);
                        //封装成json
                        JSONObject json = new JSONObject();
                        json.put("to", to_id);
                        json.put("msg", msg);
                        //通过BufferedWriter对象向服务器写数据
                        out.write(json.toString() + "\n");
                        out.flush();

                        Log.d("client", " ip=" + socket.getInetAddress().getHostAddress()+" 写完"+json.toString());

                        //out.write(msg);
                        //out.write(msg.getBytes());
                        //out.flush();
                        //handler.sendEmptyMessage(StatusCode.SUCCESS);

//                        Map map = new HashMap();
//                        map.put("content", "来自" + socket.getInetAddress() + "  " + msg);
//                        map.put("id", 1);
//                        data.add(map);

//                        InputStreamReader reader = new InputStreamReader(socket.getInputStream(), "utf-8");
//                        BufferedReader bufferedReader = new BufferedReader(reader);
//                        //byte[] b = new byte[4*1024];
//                        StringBuffer buffer = new StringBuffer();
//                        while (bufferedReader.read() != -1){
//                            buffer.append(bufferedReader.readLine());
//                        }
//                        Log.d("fromServer", "" + buffer.toString()+" ip="+socket.getInetAddress());
//                        Map map = new HashMap();
//                        map.put("content", "" + buffer.toString());
//                        map.put("id", 1);
//                        data.add(map);
                        //handler.sendEmptyMessage(StatusCode.SUCCESS);

                    } catch (IOException e) {
                        e.printStackTrace();
                        //return false;
                        //handler.sendEmptyMessage(StatusCode.FAIL);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (out != null) {
                            //try {
                            out.close();
                            //} catch (IOException e) {
                            //e.printStackTrace();
                            //}
                        }
                    }
                    //return true;
                }
            }
        }).start();

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Socket socket = Client.getInstance().getClientSocket();
//                if (socket != null){
//                    //BufferedOutputStream out = null;
//                    PrintWriter out = null;
//                    try {
//                        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);
//                        out.write(msg);
//                        //out.write(msg.getBytes());
//                        out.flush();
//                        handler.sendEmptyMessage(StatusCode.SUCCESS);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        //return false;
//                        handler.sendEmptyMessage(StatusCode.FAIL);
//                    }finally {
//                        if (out != null) {
//                            //try {
//                            out.close();
//                            //} catch (IOException e) {
//                            //e.printStackTrace();
//                            //}
//                        }
//                    }
//                    //return true;
//                }
//            }
//        }, 0);


        //return false;*/
    }

    @Override
    public void getIp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //try {
                // String ip = InetAddress.getLocalHost().getHostAddress();
                String ip = Utils.getIP((Activity) iView);
                Message msg = new Message();
                msg.what = StatusCode.GET_IP_SUCCESS;
                msg.obj = ip;
                handler.sendMessage(msg);
                //}
            }
        }).start();

    }

    @Override
    public void getServerMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Socket socket = Client.getInstance().getClientSocket();
                System.out.println("client" + " 客户端开始连接服务器");
                Log.d("client", " 客户端开始连接服务器");
                //Socket socket = Client.getInstance().getClientSocket();
                socket = Client.getInstance().getClientSocket();
                if (socket != null) {
                    handler.sendEmptyMessage(StatusCode.CONNECT_SUCCESS);
                    System.out.println("client" + " ip=" + socket.getInetAddress().getHostAddress() + " 连接服务器成功");
                    Log.d("client", " ip=" + socket.getInetAddress().getHostAddress() + " 连接服务器成功");
                    mExecutorService.execute(new ReceiveRunnable());
//
//                    while (true) {
//                        try {
//
////                        Map map = new HashMap();
////                        map.put("content", "来自" + socket.getInetAddress() + "  " + msg);
////                        map.put("id", 1);
////                        data.add(map);
//                            Log.d("client", " ip=" + socket.getInetAddress().getHostAddress() + " 连接服务器成功");
//
//                            InputStreamReader reader = new InputStreamReader(socket.getInputStream(), "utf-8");
//                            BufferedReader bufferedReader = new BufferedReader(reader);
//                            //byte[] b = new byte[4*1024];
//                            StringBuffer buffer = new StringBuffer();
//                            String str = null;
//                            while ((str = bufferedReader.readLine()) != null) {
//                                System.out.println(str);//此时str就保存了一行字符串
//                                buffer.append(str);
//                            }
////                            while (bufferedReader.read() != -1) {
////                                buffer.append(bufferedReader.readLine());
////                            }
//                            Log.d("fromServer", "" + buffer.toString() + " ip=" + socket.getInetAddress().getHostAddress());
//
//                            Map map = new HashMap();
//                            map.put("content", "" + buffer.toString());
//                            map.put("id", 1);
//                            data.add(map);
//                            //handler.sendEmptyMessage(StatusCode.SUCCESS);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            //return false;
//                            //handler.sendEmptyMessage(StatusCode.FAIL);
//                        }
//                        //return true;
//                    }
                } else {
                    handler.sendEmptyMessage(StatusCode.CONNECT_FAIL);
                }
            }
        }).start();
    }

    class SendRunnable implements Runnable {
        //Socket mSocket;
        String from_id;
        String to_id;
        String msg;

        public SendRunnable(Socket socket, String from_id, String To_id, String Msg) {
            //mSocket = socket;
            this.from_id = from_id;
            this.to_id = To_id;
            this.msg = Msg;
        }

        @Override
        public void run() {
            Socket socket = Client.getInstance().getClientSocket();
            if (socket != null && !socket.isClosed() && !socket.isOutputShutdown()) {
                System.out.println("client" + " ip=" + socket.getInetAddress().getHostAddress() + " 连接服务器成功");
                Log.d("client", " ip=" + socket.getInetAddress().getHostAddress() + " 连接服务器成功");
                //BufferedOutputStream out = null;
                PrintWriter out = null;
                try {
                    out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);
                    //out.write("hello, server" + "\n");
                    //封装成json
                    JSONObject json = new JSONObject();
                    json.put("from", from_id);
                    json.put("to", to_id);
                    json.put("msg", msg);
                    //通过BufferedWriter对象向服务器写数据
                    out.write(json.toString() + "\n");
                    out.flush();

                    System.out.println("client" + " ip=" + socket.getInetAddress().getHostAddress() + " 写完" + json.toString());
                    Log.d("client", " ip=" + socket.getInetAddress().getHostAddress() + " 写完" + json.toString());

                    //out.write(msg);
                    //out.write(msg.getBytes());
                    //out.flush();
                    //handler.sendEmptyMessage(StatusCode.SUCCESS);

//                        Map map = new HashMap();
//                        map.put("content", "来自" + socket.getInetAddress() + "  " + msg);
//                        map.put("id", 1);
//                        data.add(map);

//                        InputStreamReader reader = new InputStreamReader(socket.getInputStream(), "utf-8");
//                        BufferedReader bufferedReader = new BufferedReader(reader);
//                        //byte[] b = new byte[4*1024];
//                        StringBuffer buffer = new StringBuffer();
//                        while (bufferedReader.read() != -1){
//                            buffer.append(bufferedReader.readLine());
//                        }
//                        Log.d("fromServer", "" + buffer.toString()+" ip="+socket.getInetAddress());
//                        Map map = new HashMap();
//                        map.put("content", "" + buffer.toString());
//                        map.put("id", 1);
//                        data.add(map);
                    //handler.sendEmptyMessage(StatusCode.SUCCESS);

                } catch (IOException e) {
                    e.printStackTrace();
                    //return false;
                    //handler.sendEmptyMessage(StatusCode.FAIL);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        //try {
                        out.close();
                        //} catch (IOException e) {
                        //e.printStackTrace();
                        //}
                    }
                    if (socket != null && !socket.isClosed()){
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                //return true;
            } else {
                handler.sendEmptyMessage(StatusCode.CONNECT_FAIL);
            }
        }

    }


    class ReceiveRunnable implements Runnable {
        //Socket mSocket;

        public ReceiveRunnable() {
            //mSocket = socket;
        }

        @Override
        public void run() {
            while (true) {
                InputStreamReader reader = null;
                BufferedReader bufferedReader = null;
                Socket socket = null;
                try {

//                        Map map = new HashMap();
//                        map.put("content", "来自" + socket.getInetAddress() + "  " + msg);
//                        map.put("id", 1);
//                        data.add(map);
                    socket = Client.getInstance().getClientSocket();
                    Log.d("client", " ip=" + socket.getInetAddress().getHostAddress() + " 连接服务器成功");
                    System.out.println("-------client" + " ip=" + socket.getInetAddress().getHostAddress() + " 连接服务器成功");

                    System.out.println("-------fromServer" + " ip=" + socket.getInetAddress().getHostAddress());

                    reader = new InputStreamReader(socket.getInputStream(), "utf-8");
                    bufferedReader = new BufferedReader(reader);
                    //byte[] b = new byte[4*1024];
                    StringBuffer buffer = new StringBuffer();
                    String str = null;
                    while (!socket.isClosed() && !socket.isInputShutdown() && (str = bufferedReader.readLine()) != null) {
                        System.out.println("str = " + str);//此时str就保存了一行字符串
                        buffer.append(str);
                    }
//                            while (bufferedReader.read() != -1) {
//                                buffer.append(bufferedReader.readLine());
//                            }


                    System.out.println("-------fromServer" + buffer.toString() + " ip=" + socket.getInetAddress().getHostAddress());

                    //Log.d("fromServer", "" + buffer.toString() + " ip=" + socket.getInetAddress().getHostAddress());
                    System.out.println("-------fromServer" + "" + buffer.toString() + " ip=" + socket.getInetAddress().getHostAddress());

                    try {
                        JSONObject msgJson = new JSONObject(buffer.toString());
                        long info_id = msgJson.getLong("id");
                        String from_id = msgJson.getString("from");
                        String to_id = msgJson.getString("to");
                        String msg = msgJson.getString("msg");
                        String timeStr = msgJson.getString("time");

                        if (info_id == -1) {
                            handler.sendEmptyMessage(StatusCode.FAIL);
                            continue;
                        }
                        MyMessage myMessage = new MyMessage();
                        myMessage.setInfoId(info_id);
                        myMessage.setFrom(from_id);
                        myMessage.setTo(to_id);
                        myMessage.setMsg(msg);
                        myMessage.setTime(timeStr);

                        if (ip != null && from_id != null && from_id.equals(ip)) {
                            myMessage.setType(1);
                        } else {
                            myMessage.setType(0);
                        }

//                        Map map = new HashMap();
//                        map.put("content", msg);
//                        if (ip != null && from_id !=null && from_id.equals(ip))
//                            map.put("id", 1);
//                        else
//                            map.put("id", 0);
//                        data.add(map);

                        long local_id = MySQLiteUtil.insert((Activity) iView, myMessage); //插入到数据库里面
                        System.out.println("-------local_id" + local_id);
                        if (local_id == -1) {
                            continue;
                        }

                        data.add(myMessage);
                        handler.sendEmptyMessage(StatusCode.SUCCESS);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


/*                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/


                } catch (IOException e) {
                    e.printStackTrace();
                    //return false;
                    //handler.sendEmptyMessage(StatusCode.FAIL);
                }finally {
                    try {
                        if (reader != null)
                            reader.close();
                        if (bufferedReader != null)
                        bufferedReader.close();
                        if (socket!=null && !socket.isClosed())
                            socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //return true;
            }
        }
    }

    private int count = 0;

    @Override
    public String getFromIp() {
        if (ip == null) return null;
        return ip;
        //return ip + "_"+(count++);
    }

    @Override
    public void getLocalMsgs() {
        List<MyMessage> msgs = MySQLiteUtil.query((Activity) iView);
        if (msgs != null) {
            for (int i = 0; i < msgs.size(); i++) {
                data.add(msgs.get(i));
            }
        }
    }

    @Override
    public void registerNetworkListener(Context context) {
        System.out.println("------- 注册广播");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        context.registerReceiver(myBroadcastReceiver, filter);
    }

    @Override
    public void unregisterNetworkListener(Context context) {
        context.unregisterReceiver(myBroadcastReceiver);
    }

    //@Override
    public void broadcastNetwork(String content) {

        //String content = "";
        //if (Utils.getConnect())
        //iView.showInfo("网络连接成功");
        //content = "网络连接成功";
        //else
        //iView.showInfo("网络没有连接");
        //content = "网络没有连接";

        iView.showInfo(content);
    }
}
