package com.iknow.module.datasource.network.convert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.iknow.module.datasource.network.support.RemoveShell;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * A {@linkplain Converter.Factory converter} which uses Gson for JSON.
 * <p>
 * Because Gson is so flexible in the types it supports, this converter assumes that it can handle
 * all types. If you are mixing JSON serialization with something else (such as protocol buffers),
 * you must {@linkplain Retrofit.Builder#addConverterFactory(Converter.Factory) add this instance}
 * last to allow the other converters a chance to see their types.
 */
public final class IknowConverterFactory extends Converter.Factory {
    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static IknowConverterFactory create() {
//        return create(new Gson());//默认是不序列化null对象的
        return create(new GsonBuilder().serializeNulls().create());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static IknowConverterFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new IknowConverterFactory(gson);
    }

    private final Gson gson;

    private IknowConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        boolean isRemoveShell = false;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == RemoveShell.class) {
                isRemoveShell = true;
                break;
            }
        }
        if (isRemoveShell) {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
            return new IknowResponseBodyConverter<>(gson, adapter, type);
        } else {
            return null;
        }
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new IknowRequestBodyConverter<>(gson, adapter);
    }
}
