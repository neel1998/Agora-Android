package com.neel.agora;

public interface AsyncCallback<T> {
    public void success(T t);
    public void error(Exception e);
}
