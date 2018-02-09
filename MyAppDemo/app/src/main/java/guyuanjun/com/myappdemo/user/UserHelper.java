package guyuanjun.com.myappdemo.user;

/**
 * Created by HP on 2017-6-20.
 */

public class UserHelper {
    private static volatile UserHelper instance = null;

    private UserHelper(){

    }

    public static UserHelper getInstance(){
        if (instance == null){
            synchronized (UserHelper.class){
                instance = new UserHelper();
            }
        }
        return instance;
    }

    private String userName;
    private long userId;
    private boolean isLogin;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean getLogin() {
        return isLogin;
    }
}
