package com.smartstudy.uskid.library.api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Api异常处理处理
 *
 * @author 王宏杰
 * @date 2018/3/23
 */

public class ApiErrorGsonConverterFactory extends Converter.Factory {

    private final Converter.Factory mDelegateFactory;

    public ApiErrorGsonConverterFactory(Converter.Factory delegateFactory) {
        mDelegateFactory = delegateFactory;
    }

    public static ApiErrorGsonConverterFactory create(Gson gson) {
        return new ApiErrorGsonConverterFactory(GsonConverterFactory.create(gson));
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return mDelegateFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        final Converter<ResponseBody, ?> apiErrorConverter = mDelegateFactory.responseBodyConverter(ApiError.class, annotations, retrofit);
        final Converter<ResponseBody, ?> delegateConverter = mDelegateFactory.responseBodyConverter(type, annotations, retrofit);
        return (Converter<ResponseBody, Object>) value -> {
            MediaType mediaType = value.contentType();
            String stringBody = value.string();
            try {
                Object apiError = apiErrorConverter.convert(ResponseBody.create(mediaType, stringBody));
                if (apiError instanceof ApiError && ((ApiError) apiError).isError()) {
                    throw (ApiError) apiError;
                }
            } catch (JsonSyntaxException notApiError) {
            }
            return delegateConverter.convert(ResponseBody.create(mediaType, stringBody));
        };
    }
}