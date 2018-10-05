package com.mnnyang.gzuclassschedule.app;

import android.app.Application;
import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.ScreenUtils;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import okhttp3.OkHttpClient;

/**
 * Created by mnnyang on 17-11-1.
 * TODO 登录页面细节intent
 */

public class app extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initOkHttp();
        initUtils();


        Cache.instance().init(mContext);
        //TODO 不该在这里
        //AppUtils.copyOldData(this);
    }


    private void initOkHttp() {
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        Cache.instance().setCookieJar(cookieJar);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
                .followSslRedirects(false)
                //.cookieJar(new LocalCookieJar())   //为OkHttp设置自动携带Cookie的功能
                .addInterceptor(new LoggerInterceptor("TAG"))
                //.cookieJar(new CookieJarImpl(new PersistentCookieStore(getBaseContext()))) //要在内存Cookie前
                //.cookieJar(new CookieJarImpl(new MemoryCookieStore()))//内存Cookie
                .cookieJar(cookieJar)
                .cache(null)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    //static class LocalCookieJar implements CookieJar {
    //    List<Cookie> cookies;
    //
    //    @Override
    //    public List<Cookie> loadForRequest(HttpUrl arg0) {
    //        if (cookies != null)
    //            return cookies;
    //        return new ArrayList<Cookie>();
    //    }
    //
    //    @Override
    //    public void saveFromResponse(HttpUrl arg0, List<Cookie> cookies) {
    //        System.out.println("----------------saveFromResponse");
    //        for (Cookie cookie : cookies) {
    //            System.out.println(cookie);
    //        }
    //        this.cookies = cookies;
    //    }
    //
    //}

    private void initUtils() {
        ToastUtils.init(mContext);
        Preferences.init(mContext);
        ScreenUtils.init(mContext);
    }
}
