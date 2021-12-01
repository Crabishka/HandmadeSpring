package ru.vsu.csf.george.pryadchenko.server.http;

public class HttpException extends RuntimeException{

    public HttpException(String message) {
        super(message);
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
