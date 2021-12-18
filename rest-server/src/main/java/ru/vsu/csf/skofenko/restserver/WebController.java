package ru.vsu.csf.skofenko.restserver;

import ru.vsu.csf.pryadchenko.server.dockerLogic.ResourceManager;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.ContentType;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Controller;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.GetMapping;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Param;
import ru.vsu.csf.pryadchenko.server.logic.ServerService;

import java.io.File;
import java.io.IOException;

@Controller
public class WebController {

    @GetMapping("html")
    @ContentType("text/html; charset=UTF-8")
    public static String doHtml(@Param(name = "path") String path) throws IOException {
        File file = ResourceManager.get(WebController.class, path);
        return ServerService.readTextFromFile(file);
    }

    @GetMapping("pic")
    @ContentType("image/jpeg")
    public static String doPic(@Param(name = "path") String path) throws IOException {
        File file = ResourceManager.get(WebController.class, path);
        return ServerService.readAllBytes(file);
    }

    @GetMapping("css")
    @ContentType("text/css")
    public static String doCss(@Param(name = "path") String path) throws IOException {
        File file = ResourceManager.get(WebController.class, path);
        return ServerService.readTextFromFile(file);
    }

    @GetMapping("js")
    @ContentType("application/javascript")
    public static String doJs(@Param(name = "path") String path) throws IOException {
        File file = ResourceManager.get(WebController.class, path);
        return ServerService.readTextFromFile(file);
    }

    @GetMapping("gif")
    @ContentType("image/gif")
    public static String doGif(@Param(name = "path") String path) throws IOException {
        File file = ResourceManager.get(WebController.class, path);
        return ServerService.readAllBytes(file);
    }
}
