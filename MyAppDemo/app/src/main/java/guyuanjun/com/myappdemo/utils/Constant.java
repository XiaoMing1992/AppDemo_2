package guyuanjun.com.myappdemo.utils;

/**
 * Created by HP on 2017-3-16.
 */

public class Constant {
    // APP_ID 替换为你的应用从官方网站申请到的合法appId
    public static final String APP_ID = "wxd930ea5d5a258f4f";
    public static final String STitle = "showmsg_title";
    public static final String SMessage = "showmsg_message";
    public static final String BAThumbData = "showmsg_thumb_data";

    //****************************************************************
    // 判断应用是否初次加载，读取SharedPreferences中的guide_activity字段
    //****************************************************************
    public static final String SHAREDPREFERENCES_NAME = "my_pref";
    public static final String KEY_GUIDE_ACTIVITY = "guide_activity";

    public static final int MESSAGE_FRAGMENT = 0;
    public static final int CONTACT_FRAGMENT = 1;
    public static final int NEWS_FRAGMENT = 2;
    public static final int MY_FRAGMENT = 3;

    public static final String PACKET_NAME = "guyuanjun.com.myappdemo";

    /**
     * 服务器地址
     */
    public static final String SERVER_ADDR = "http://www.baidu.com";

    public  static final String LOGIN="user/login";
    public  static final String REGISTER="user/register";

    public  static final String USER_INFO="user/info";
/*    public  static final String REGISTER="user/register";*/

    public  static final String requestURL = "http://192.168.1.212:8011/pd/upload/fileUpload.do";
    public  static final String SAVE_PICTURE_URL = "/data/data/guyuanjun.com.myappdemo/pictures/";

    public static final String MESSAGE_RECEIVED_ACTION = "guyuanjun.com.myappdemo.receiver.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    //保存歌词的路径
    public static final String DIRECTORY_LYRIC = "/data/data/guyuanjun.com.myappdemo/song/word/";

    //是否下载
    public static boolean mCanGetBitmapFromNetWork = true;

    //
    public static  final String ENVIROMENT_DIR_CACHE = "/data/data/guyuanjun.com.myappdemo/cache/";
    public static  final String ENVIROMENT_DIR_SAVE = "/data/data/guyuanjun.com.myappdemo/pictures/";

    public static final String APK_NAME = "temp.apk";

    //版本下载路径链接
    public static final String VERSION_ADDR = "http://www.baidu.com";

    //是否登录
    public static boolean isLogin = false;

    public static final String SAVE_USER_INFO_NAME = "user";
    public static final String USER_NAME_KEY = "user_name";
    public static final String USER_ID_KEY = "user_id";

    public static final String TYPE_NEWS = "news";
    public static final String TYPE_WEITOUTIAO = "weitoutiao";

    public static final String USER_READ_TIME_FILE_NAME = "user_read_time_file_name";
    public static final String USER_READ_TIME_START = "user_read_time_start";
    public static final String USER_READ_TIME_VALUE = "user_read_time_value";
    public static long USER_READ_TIME_TEMP = 0;
}
