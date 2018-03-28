package com.smartstudy.uskid.library.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author 王宏杰
 * @date 2018/3/23
 */
public class ApiManager {

    public static Retrofit buildRetrofit(String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new StethoInterceptor())
                .addInterceptor(logging)
                //设置超时时间
                .connectTimeout(10 * 1000, TimeUnit.SECONDS)
                .readTimeout(10 * 1000, TimeUnit.SECONDS)
                .build();
        ExclusionStrategy exclusionStrategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return clazz == Field.class || clazz == Method.class;
            }
        };

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .addSerializationExclusionStrategy(exclusionStrategy)
                .addDeserializationExclusionStrategy(exclusionStrategy)
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(new QualifiedTypeConverterFactory(ApiErrorGsonConverterFactory.create(gson),
                        new StringConverter()))
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .build();
        return retrofit;
    }

}
