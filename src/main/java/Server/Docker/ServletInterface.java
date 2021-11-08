package Server.Docker;

import Server.Docker.GetMapping;
import Server.Http.Request.HttpRequest;
import Server.Http.Response.HttpResponse;

public interface ServletInterface {

    @GetMapping
    void doGet(HttpRequest request, HttpResponse response);

    /*
    There are methods to not use reflection
     */
    default void doPost(HttpRequest request, HttpResponse response) {

    }

    default void doPut(HttpRequest request, HttpResponse response) {

    }

    default void doDelete(HttpRequest request, HttpResponse response) {

    }
}
