package guyuanjun.com.mychat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by HP on 2018-3-6.
 */

public class Client {
    private volatile static Client instance = null;
    private final String HOST_IP = "";
    private final int HOST_PORT = 12580;

    private Client(){}
    public static Client getInstance(){
        if (instance == null){
            synchronized (Client.class){
                if (instance == null){
                    instance = new Client();
                }
            }
        }
        return instance;
    }

    public Socket getClientSocket(){
        Socket socket = null;
        try {
            socket = new Socket(HOST_IP, HOST_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }
}
