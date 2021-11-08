package Server.Docker;

import Server.Http.Request.HttpRequest;
import Server.Http.Response.HttpResponse;

import java.io.IOException;

public interface ServletInterface {

    @GetMapping
    void doGet(HttpRequest request, HttpResponse response) throws IOException;

    default void doPost(HttpRequest request, HttpResponse response) {

    }

    default void doPut(HttpRequest request, HttpResponse response) {

    }

    default void doDelete(HttpRequest request, HttpResponse response) {

    }
}