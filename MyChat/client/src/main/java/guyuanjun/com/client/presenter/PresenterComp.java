package guyuanjun.com.client.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import guyuanjun.com.client.Utils;
import guyuanjun.com.client.adapter.MyAdapter;
import guyuanjun.com.client.view.ClientActivity;
import guyuanjun.com.client.view.IView;
import guyuanjun.com.client.view.StatusCode;

/**
 * Created by HP on 2018-3-14.
 */

public class PresenterComp implements IPresenter {
    IView iView;
    Handler handler;
    boolean res = false;
    List<Map<String, ?>> data;
    MyAdapter myAdapter;

    public PresenterComp(IView iView) {
        this.iView = iView;
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case StatusCode.FAIL:
                        //setSendState(false);
                        break;

                    case StatusCode.SUCCESS:
                        //setSendState(true);
                        myAdapter.notifyDataSetChanged();
                        break;

                    case 2:
                        String ip = (String) msg.obj;
                        returnIP(ip);
                        break;
                }
            }
        };

        data = new ArrayList<>();
        myAdapter = new MyAdapter((Activity) iView, data);
    }

    private void returnIP(final String ip){
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


        //return false;
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
                    msg.what = 2;
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
                Log.d("client", " 客户端开始连接服务器");
                Socket socket = Client.getInstance().getClientSocket();
                if (socket != null) {
                    Log.d("client", " ip=" + socket.getInetAddress().getHostAddress()+" 连接服务器成功");
                    while (true) {
                        try {

//                        Map map = new HashMap();
//                        map.put("content", "来自" + socket.getInetAddress() + "  " + msg);
//                        map.put("id", 1);
//                        data.add(map);
                            Log.d("client", " ip=" + socket.getInetAddress().getHostAddress()+" 连接服务器成功");

                            InputStreamReader reader = new InputStreamReader(socket.getInputStream(), "utf-8");
                            BufferedReader bufferedReader = new BufferedReader(reader);
                            //byte[] b = new byte[4*1024];
                            StringBuffer buffer = new StringBuffer();
                            String str = null;
                            while((str = bufferedReader.readLine()) != null){
                                System.out.println(str);//此时str就保存了一行字符串
                                buffer.append(str);
                            }
//                            while (bufferedReader.read() != -1) {
//                                buffer.append(bufferedReader.readLine());
//                            }
                            Log.d("fromServer", "" + buffer.toString() + " ip=" + socket.getInetAddress().getHostAddress());

                            Map map = new HashMap();
                            map.put("content", "" + buffer.toString());
                            map.put("id", 1);
                            data.add(map);
                            //handler.sendEmptyMessage(StatusCode.SUCCESS);

                        } catch (IOException e) {
                            e.printStackTrace();
                            //return false;
                            //handler.sendEmptyMessage(StatusCode.FAIL);
                        }
                        //return true;
                    }
                }
            }
        }).start();
    }
}
