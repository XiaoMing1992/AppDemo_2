package guyuanjun.com.myappdemo.utils;

/**
 * Created by cdy on 2016-8-12.
 */
public class StringUtils {

    public static boolean areNotEmpty(String key, String value) {
        if(key!=null && value!=null && !key.equals("") && !value.equals(""))
            return true;
        else
            return false;
    }
}
