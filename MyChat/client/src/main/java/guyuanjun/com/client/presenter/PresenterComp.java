package guyuanjun.com.client.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import guyuanjun.com.client.view.IView;
import guyuanjun.com.client.view.StatusCode;

/**
 * Created by HP on 2018-3-14.
 */

public class PresenterComp implements IPresenter {
    IView iView;
    Handler handler;
    boolean res = false;

    public PresenterComp(IView iView){
        this.iView = iView;
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case StatusCode.FAIL:
                        setSendState(false);
                        break;

                    case StatusCode.SUCCESS:
                        setSendState(true);
                        break;
                }
            }
        };
    }

    @Override
    public void clear() {
        iView.onClearText();
    }
    public void setSendState(boolean res){
         this.res = res;
    }

    public boolean getSendState(){
        return res;
    }

    @Override
    public void sengMsg(final String msg) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = Client.getInstance().getClientSocket();
                if (socket != null){
                    //BufferedOutputStream out = null;
                    PrintWriter out = null;
                    try {
                        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);
                        out.write(msg);
                        //out.write(msg.getBytes());
                        out.flush();
                        //handler.sendEmptyMessage(StatusCode.SUCCESS);
                    } catch (IOException e) {
                        e.printStackTrace();
                        //return false;
                        //handler.sendEmptyMessage(StatusCode.FAIL);
                    }finally {
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


}
