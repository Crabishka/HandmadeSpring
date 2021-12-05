package ru.vsu.csf.pryadchenko.server.dockerLogic;


import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Controller;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.GetMapping;
import ru.vsu.csf.pryadchenko.server.http.request.HttpRequest;
import ru.vsu.csf.pryadchenko.server.http.response.HttpResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

public class RootResourceHandler extends Servlet {

    Bean bean;

    public RootResourceHandler(String pack) {
        super(pack);
        this.bean = new Bean(ResourceHandlerController.class);
        super.factory.storage.put(new AnnotationBinder(Controller.class), new HashMap<>());
        super.factory.storage.get(new AnnotationBinder(Controller.class)).put("", bean);
    }


    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        String beanPath = "";
        String methodPath = request.getPath().split("/")[1];
        Method method = bean.getMethodsByAnnotationAndValue(new AnnotationBinder(GetMapping.class), methodPath);
        // сделать поиск метода по названию

        byte[] body = null;
        try {
            AnnotationBinder contentType = bean.getAnnotationByMethodAndName("ContentType", method);
            if (contentType != null) {
                response.putHeader("Content-Type", contentType.value);
                switch (contentType.value) {
                    case ("image/jpeg"): {
                        body = Base64.getDecoder().decode(method.invoke(null, methodPath).toString());  // FIXME
                        break;
                    }
                    case ("text/html; charset=UTF-8"): {
                        body = method.invoke(null, methodPath).toString().getBytes(StandardCharsets.UTF_8);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            response.setStatus("418 I’m a teapot");
            body = response.getStatus().getBytes(StandardCharsets.UTF_16);
            e.printStackTrace();
        }
        // Set response
        response.setBody(body);
        response.send();
    }


}


