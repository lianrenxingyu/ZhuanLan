package io.bxbxbai.zhuanlan.core;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xuebin on 15/12/16.
 *
 * 抽象类，重写抽象方法onResponse
 */
public abstract class SimpleCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onResponse(response.body(), response.code(), response.message());
        } else {
            onResponseFail();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }

    /**
     *抽象类 这是一个必须实现的抽象方法，onResponse
     * on response return
     *
     * @param result result
     * @param code   http code
     * @param msg    http msg
     */
    public abstract void onResponse(final T result, int code, String msg);

    public void onResponseFail() {

    }
}
