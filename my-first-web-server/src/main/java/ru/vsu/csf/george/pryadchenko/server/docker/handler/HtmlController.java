package ru.vsu.csf.george.pryadchenko.server.docker.handler;

import ru.vsu.csf.george.pryadchenko.server.dockerLogic.ContentType;
import ru.vsu.csf.george.pryadchenko.server.dockerLogic.Controller;
import ru.vsu.csf.george.pryadchenko.server.dockerLogic.GetMapping;
import ru.vsu.csf.george.pryadchenko.server.dockerLogic.Param;
import ru.vsu.csf.george.pryadchenko.server.logic.GetProperties;
import ru.vsu.csf.george.pryadchenko.server.logic.ServerService;

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

    @GetMapping("pics")
    @ContentType("image/jpeg")
    public static String doPic(@Param(name = "path") String path) throws IOException {
        String str = GetProperties.getProperty("resources_path") + "/" + path;
        return new String(ServerService.readAllBytes(new File(str)));
    }
}
