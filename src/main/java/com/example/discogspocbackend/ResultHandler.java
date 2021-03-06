package com.example.discogspocbackend;

public interface ResultHandler<T> {
    void completed(T result);

    void failed(Throwable error);
}
