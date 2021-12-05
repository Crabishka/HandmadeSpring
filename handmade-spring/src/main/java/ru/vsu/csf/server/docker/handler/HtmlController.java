package ru.vsu.csf.server.docker.handler;

import ru.vsu.csf.server.dockerLogic.annotation.ContentType;
import ru.vsu.csf.server.dockerLogic.annotation.Controller;
import ru.vsu.csf.server.dockerLogic.annotation.GetMapping;
import ru.vsu.csf.server.dockerLogic.annotation.Param;
import ru.vsu.csf.server.logic.GetProperties;
import ru.vsu.csf.server.logic.ServerService;

import java.io.File;
import java.io.IOException;

@Controller("html")
public class HtmlController {

    @GetMapping("page")
    @ContentType("text/html; charset=UTF-8")
    public static String doHtml(@Param(name = "path") String path) throws IOException {
        String str = GetProperties.getProperty("resources_path") + "/" + path;
        return ServerService.readTextFromFile(new File(str));
    }

    @GetMapping("pic")
    @ContentType("image/jpeg")
    public static String doPic(@Param(name = "path") String path) throws IOException {
        String str = GetProperties.getProperty("resources_path") + "/" + path;
        return ServerService.readAllBytes(new File(str));
    }

    @GetMapping("css")
    @ContentType("text/css")
    public static String doCss(@Param(name = "path") String path) throws IOException {
        String str = GetProperties.getProperty("resources_path") + "/" + path;
        return ServerService.readTextFromFile(new File(str));
    }

    @GetMapping("js")
    @ContentType("application/javascript")
    public static String doJs(@Param(name = "path") String path) throws IOException {
        String str = GetProperties.getProperty("resources_path") + "/" + path;
        return ServerService.readTextFromFile(new File(str));
    }

    @GetMapping("gif")
    @ContentType("image/gif")
    public static String doGif(@Param(name = "path") String path) throws IOException {
        String str = GetProperties.getProperty("resources_path") + "/" + path;
        return  ServerService.readAllBytes(new File(str));
    }
}
