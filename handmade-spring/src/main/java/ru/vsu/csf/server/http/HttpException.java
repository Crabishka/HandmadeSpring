<<<<<<< HEAD:handmade-spring/src/main/java/ru/vsu/csf/pryadchenko/spring/http/HttpException.java
package ru.vsu.csf.pryadchenko.spring.http;
=======
package ru.vsu.csf.server.http;
>>>>>>> BROKEN:handmade-spring/src/main/java/ru/vsu/csf/server/http/HttpException.java

public class HttpException extends RuntimeException{

    public HttpException(String message) {
        super(message);
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
