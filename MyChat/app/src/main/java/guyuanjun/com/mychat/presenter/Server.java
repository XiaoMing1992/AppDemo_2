package guyuanjun.com.mychat.presenter;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by HP on 2018-3-6.
 */

public class Server {
    private volatile static Server instance = null;
    private final String HOST_IP = "";
    private final int HOST_PORT = 12586;

    private Server(){}
    public static Server getInstance(){
        if (instance == null){
            synchronized (Server.class){
                if (instance == null){
                    instance = new Server();
                }
            }
        }
        return instance;
    }

    public ServerSocket getServerSocket(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(HOST_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverSocket;
    }
}
