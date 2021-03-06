package guyuanjun.com.myappdemo.data;

import android.util.Log;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import guyuanjun.com.myappdemo.utils.LogUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by HP on 2017-6-3.
 */

public class Json {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String post_req(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url).post(body)
                .build();

        request.header("");

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public static String post_req(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("platform", "android")
                .add("name", "bug")
                .add("subject", "XXXXXXXXXXXXXXX")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            LogUtil.d("post_req", response.body().string());
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 通过post方式来提交json数据
     * @param url
     * @param data
     * @return
     */
    public static String post(String url, Map<String, String> data) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();

        Iterator it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            System.out.println("key=" + key + " value=" + value);
            builder.add(key, value);
        }

/*        RequestBody formBody = new FormBody.Builder()
                .add("mobile", "13631257723")
                .build();*/
        RequestBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println("code="+response.code());

            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void asy_post_req(String url) throws IOException {

        RequestBody body = new FormBody.Builder()
                .add("platform", "android")
                .add("name", "bug")
                .add("subject", "XXXXXXXXXXXXXXX")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.d("asy_post_req", response.body().string());
            }
        });
    }

    public static String get_req(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                LogUtil.d("get_req", response.body().string());
                return response.body().string();
            } else {
                Log.d("","Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
