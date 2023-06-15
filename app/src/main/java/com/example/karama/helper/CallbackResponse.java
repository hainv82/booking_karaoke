package com.example.karama.helper;

import retrofit2.Response;

public interface CallbackResponse {
    public void Success(Response<?> response);

    public void Error(String error);
}
