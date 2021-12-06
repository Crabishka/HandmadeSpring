package ru.vsu.csf.skofenko.restserver;

import ru.vsu.csf.pryadchenko.server.Application;
import ru.vsu.csf.pryadchenko.server.dockerLogic.TestCat;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.*;
import ru.vsu.csf.pryadchenko.server.logic.ServerService;

import java.io.File;
import java.io.IOException;

@Controller
public class WebController {

    @GetMapping("html")
    @ContentType("text/html; charset=UTF-8")
    public static String doHtml(@Param(name = "path") String path) throws IOException {
        String str = Application.RESOURCE_PATH + "/static/" + path;
        return ServerService.readTextFromFile(new File(str));
    }

    @GetMapping("pic")
    @ContentType("image/jpeg")
    public static String doPic(@Param(name = "path") String path) throws IOException {
        String str = Application.RESOURCE_PATH + "/static/" + path;
        return ServerService.readAllBytes(new File(str));
    }

    @GetMapping("css")
    @ContentType("text/css")
    public static String doCss(@Param(name = "path") String path) throws IOException {
        String str = Application.RESOURCE_PATH + "/static/" + path;
        return ServerService.readTextFromFile(new File(str));
    }

    @GetMapping("js")
    @ContentType("application/javascript")
    public static String doJs(@Param(name = "path") String path) throws IOException {
        String str = Application.RESOURCE_PATH + "/static/" + path;
        return ServerService.readTextFromFile(new File(str));
    }

    @GetMapping("gif")
    @ContentType("image/gif")
    public static String doGif(@Param(name = "path") String path) throws IOException {
        String str = Application.RESOURCE_PATH + "/static/" + path;
        return ServerService.readAllBytes(new File(str));
    }

    @PostMapping()
    @ContentType("application/json")
    public static boolean doPost(@Param(requestBody = true, type = TestCat.class) TestCat body, @Param(name = "a") String a) {
        return true;
    }
}
