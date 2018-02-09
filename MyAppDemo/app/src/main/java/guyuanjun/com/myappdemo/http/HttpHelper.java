package guyuanjun.com.myappdemo.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by HP on 2017-1-19.
 */

public class HttpHelper {
    private static volatile HttpHelper instance;

    private RequestQueue requestQueue;
    private ImageLoader mImageLoader;
    private Context mContext;
    private LruBitmapCache cache;

    private static File fileCache;//本地缓存

    private HttpHelper(Context context) {
        mContext = context;

        if (fileCache == null) {
            fileCache = context.getCacheDir();
        }

        requestQueue = getRequestQueue();
        if (mImageLoader == null) {
            cache = new LruBitmapCache();
            mImageLoader = new ImageLoader(requestQueue, cache);
        }
    }

    public static HttpHelper getInstance(Context context) {

        if (fileCache == null) {
            fileCache = context.getCacheDir();
        }

        if (instance == null) {
            synchronized (HttpHelper.class) {
                if (instance == null) {
                    instance = new HttpHelper(context);
                }
            }
        }
        return instance;
    }

    /**
     * 通过url读取图片,注意ImageView的getTag方法已占用；不再拼接相对地址;不写入自定义的磁盘缓存
     *
     * @param imgView    要加载图片的view
     * @param loadingRes 读取中的默认图,0设置为空白
     * @param failRes    读取失败的默认图,0设置为空白
     * @param url        图片url地址
     * @return 判断是否可以使用本方法
     */
    public void loadImage(final ImageView imgView, final int loadingRes,
                          final int failRes, final String url, final boolean needDownload) {

        // getImageListener（imageView控件对象，默认图片地址，失败图片地址）;
        //ImageLoader.ImageListener listener = ImageLoader.getImageListener(imgView, loadingRes, failRes);

        //final String pictureName = splitUrl(url);
        final String pictureName = hashKeyFormUrl(url);

        if (!needDownload) {
            if (cache.getBitmap(pictureName) == null) {
                Bitmap bitmap = getBitmapFromMemCache(pictureName);
                if (fileCache != null && fileCache.exists() && bitmap != null) {
                    imgView.setImageBitmap(bitmap);
                    Log.d("FromCache", "------- isCache -------");
                    return;
                }
                Log.d("FromCache", "------- isCache -------" + fileCache + " " + fileCache.exists() + " " + bitmap);
            } else {
                Log.d("FromCache", "------- NO -------");
                imgView.setImageBitmap(cache.getBitmap(pictureName));
                return;
            }
        }

        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean b) {
                if (response.getBitmap() != null) {
                    Log.d("FromCache", "------- onResponse -------");
                    writeToLocalCache(pictureName, response.getBitmap());
                }
                if (imgView != null) {
                    Bitmap bitmap = response.getBitmap();
                    imgView.setImageBitmap(compressImage(bitmap));
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (imgView != null)
                    imgView.setImageResource(failRes);
            }
        };
        // get(图片地址，listener，宽，高)；自动帮你处理图片的宽高再也不怕大图片的oom了
        mImageLoader.get(url, listener, imgView.getWidth(), imgView.getHeight());
    }


    /**
     * 通过url读取图片,注意ImageView的getTag方法已占用；不再拼接相对地址;不写入自定义的磁盘缓存
     *
     * @param imgView    要加载图片的view
     * @param loadingRes 读取中的默认图,0设置为空白
     * @param failRes    读取失败的默认图,0设置为空白
     * @param url        图片url地址
     * @return 判断是否可以使用本方法
     */
    public void loadAdvertisement(final ImageView imgView, final int loadingRes,
                          final int failRes, final String url, final boolean needDownload) {

        // getImageListener（imageView控件对象，默认图片地址，失败图片地址）;
        //ImageLoader.ImageListener listener = ImageLoader.getImageListener(imgView, loadingRes, failRes);

        //final String pictureName = splitUrl(url);
        final String pictureName = hashKeyFormUrl(url);

        if (!needDownload) {
            if (cache.getBitmap(pictureName) == null) {
                Bitmap bitmap = getBitmapFromMemCache(pictureName);
                if (fileCache != null && fileCache.exists() && bitmap != null) {
                    //imgView.setImageBitmap(bitmap);
                    imgView.setBackground(convertBitmap2Drawable(bitmap));
                    Log.d("FromCache", "------- isCache -------");
                    return;
                }
                Log.d("FromCache", "------- isCache -------" + fileCache + " " + fileCache.exists() + " " + bitmap);
            } else {
                Log.d("FromCache", "------- NO -------");
                imgView.setBackground(convertBitmap2Drawable(cache.getBitmap(pictureName)));
                //imgView.setImageBitmap(cache.getBitmap(pictureName));
                return;
            }
        }

        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean b) {
                if (response.getBitmap() != null) {
                    Log.d("FromCache", "------- onResponse -------");
                    writeToLocalCache(pictureName, response.getBitmap());
                }
                if (imgView != null) {
                    Bitmap bitmap = response.getBitmap();
                    imgView.setBackground(convertBitmap2Drawable(bitmap));
                    //imgView.setImageBitmap(compressImage(bitmap));
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (imgView != null)
                    imgView.setBackgroundResource(failRes);
                    //imgView.setImageResource(failRes);
            }
        };
        // get(图片地址，listener，宽，高)；自动帮你处理图片的宽高再也不怕大图片的oom了
        mImageLoader.get(url, listener, imgView.getWidth(), imgView.getHeight());
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext);
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public Bitmap getBitmapFromMemCache(String url) {

        try {
            if (fileCache == null)
                return null;
            File file = new File(fileCache, url);
            if (!file.exists())
                file.createNewFile();
            Log.d("FromCache", " path= " + file.getPath());
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);//完整文件路径
            //2读取之后放入内存,提高效率
            cache.putBitmap(url, bitmap);
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("FromCache", "getBitmapFromMemCache 0000000000000000000000000000000000" + url);
        return null;
    }

    public void clearCache(String url) {
        getRequestQueue().getCache().remove(url);
    }

    public void clearAllCache() {
        getRequestQueue().getCache().clear();
    }

    private void writeToLocalCache(final String imageUrl, final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (bitmap == null)
                    return;
                try {
                    File file = new File(fileCache, imageUrl);
                    try {
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    //写入文件的操作(1图片类型2图片质量当为100时表示不压缩3文件流)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private synchronized Bitmap compressImage(Bitmap image) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, os);

            if (os.toByteArray().length / 1024 > 256) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
                BitmapFactory.Options newOpts = new BitmapFactory.Options();

                os.reset();//重置baos即清空baos
                newOpts.inSampleSize = 4;
                image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中
                ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
                newOpts.inPreferredConfig = Bitmap.Config.RGB_565;

                return BitmapFactory.decodeStream(is, null, newOpts);
            } else {
                return image;
            }
        } catch (Exception e) {
            return image;
        }

    }

    public String splitUrl(String url) {
        String name = url.substring(url.lastIndexOf("/") + 1, url.length());
        Log.d("name", name);
        //String nameStr = name.substring(0, name.indexOf("."));
        //Log.d("nameStr", nameStr);
        return name;
    }

    private String hashKeyFormUrl(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    // Bitmap → Drawable
    public static Drawable convertBitmap2Drawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
         // 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
        return bd;
    }


}
