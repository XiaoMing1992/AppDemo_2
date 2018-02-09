package guyuanjun.com.myappdemo.data;

import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by HP on 2017-6-15.
 */

public class MyRequestFactory {

    private static volatile MyRequestFactory instance;
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private MyRequestFactory(){

    }

    public static MyRequestFactory getInstance() {
        if (instance == null){
            synchronized (MyRequestFactory.class){
                if (instance == null){
                    instance = new MyRequestFactory();
                }
            }
        }
        return instance;
    }

    public Request buildGetRequest(final String url){
        Request request = new Request.Builder().url(url).build();
        return request;
    }

    public Request buildPostRequest(final String url, final String json){
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return request;
    }
}
