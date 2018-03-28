package guyuanjun.com.client.presenter;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by HP on 2018-3-6.
 */

public class Client {
    private volatile static Client instance = null;
    private final String HOST_IP = "192.168.0.107";
    private final int HOST_PORT = 12586;

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
        while (socket == null) {
            try {
                socket = new Socket(HOST_IP, HOST_PORT);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        return socket;
    }
}
