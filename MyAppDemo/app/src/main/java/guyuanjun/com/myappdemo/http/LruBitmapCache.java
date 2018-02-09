package guyuanjun.com.myappdemo.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

public class LruBitmapCache implements ImageLoader.ImageCache {
    private LruCache<String, Bitmap> mCache;

    private  int getDefaultLruCacheSize(){
        final int maxMemory=(int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize=maxMemory/8;
        return cacheSize;
    }

    private  int MAX_SIZE = getDefaultLruCacheSize();

    public LruBitmapCache() {

        mCache = new LruCache<String, Bitmap>(MAX_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight()
                        + key.getBytes().length;
            }

        };

    }

    /**
     * 单位字节
     * @param size
     */
    public LruBitmapCache(int size) {

        mCache = new LruCache<String, Bitmap>(size) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }

        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        if(url == null || bitmap == null){
            return;
        }
        mCache.put(url, bitmap);
    }

    /**
     * 单位字节,不调用时max size默认 10MB
     * @return
     */
    public static int getCacheSize(Context context){
        long totalMem= Runtime.getRuntime().maxMemory();
        Log.i("getCacheSize","cache使用内存:"+totalMem/8);
        return (int)totalMem/8;
    }
}
