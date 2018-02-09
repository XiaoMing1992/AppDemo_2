package guyuanjun.com.myappdemo.utils;

import android.content.Context;

import com.tencent.stat.StatService;

import java.util.Properties;

/**
 * Created by HP on 2017-10-11.
 */

public class TencentCloud {
    public static final String APP_KEY = "AY913QKT6WIR";

    //====================【次数统计】Key-Value参数的事件
    /**
     *
     * @param ctx 页面的设备上下文
     * @param event_id 事件标识
     * @param properties Key-Value参数对，key和value都是String类型
     */
    public static void trackCustomKVEvent(Context ctx, String event_id, Properties properties){
        StatService.trackCustomKVEvent(ctx, event_id, properties);
    }

    //====================【次数统计】带任意参数的事件
    /**
     *
     * @param ctx 页面的设备上下文
     * @param event_id 事件标识
     * @param args 事件参数
     */
    public static void trackCustomEvent(Context ctx, String event_id, String... args){
        StatService.trackCustomEvent(ctx, event_id, args);
    }

    //====================【时长统计】Key-Value参数事件
    /**
     *
     * @param ctx 页面的设备上下文
     * @param event_id 事件标识
     * @param properties Key-Value参数对，key和value都是String类型
     */
    public static void trackCustomBeginKVEvent(Context ctx, String event_id, Properties properties){
        StatService.trackCustomBeginKVEvent(ctx, event_id, properties);
    }

    /**
     *
     * @param ctx 页面的设备上下文
     * @param event_id 事件标识
     * @param properties Key-Value参数对，key和value都是String类型
     */
    public static void trackCustomEndKVEvent(Context ctx, String event_id, Properties properties){
        StatService.trackCustomEndKVEvent(ctx, event_id, properties);
    }

    //====================【时长统计】带有统计时长的自定义参数事件
    //注意：trackCustomBeginEvent和trackCustomEndEvent必须成对出现，且参数列表完全相同，才能正常上报事件。
    /**
     *
     * @param ctx 页面的设备上下文
     * @param event_id 事件标识
     * @param args 事件参数
     */
    public static void trackCustomBeginEvent(Context ctx, String event_id, String... args){
        StatService.trackCustomBeginEvent(ctx, event_id, args);
    }

    /**
     *
     * @param ctx 页面的设备上下文
     * @param event_id 事件标识
     * @param args 事件参数
     */
    public static void trackCustomEndEvent(Context ctx, String event_id, String... args){
        StatService.trackCustomEndEvent(ctx, event_id, args);
    }
}
