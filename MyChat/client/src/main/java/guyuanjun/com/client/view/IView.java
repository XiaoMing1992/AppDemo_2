package guyuanjun.com.client.view;

/**
 * Created by HP on 2018-3-14.
 */

public interface IView {
    void onClearText();
    void getIp(String IP);
    boolean handleInput(String inputStr);
    void showInfo(final String content);
}
