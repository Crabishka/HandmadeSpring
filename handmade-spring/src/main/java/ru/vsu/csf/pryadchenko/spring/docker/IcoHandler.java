package ru.vsu.csf.george.pryadchenko.server.docker;

import ru.vsu.csf.george.pryadchenko.server.dockerLogic.ContentType;
import ru.vsu.csf.george.pryadchenko.server.dockerLogic.Controller;
import ru.vsu.csf.george.pryadchenko.server.dockerLogic.GetMapping;
import ru.vsu.csf.george.pryadchenko.server.logic.GetProperties;
import ru.vsu.csf.george.pryadchenko.server.logic.ServerService;

import java.io.File;
import java.io.IOException;

@Controller
public class IcoHandler {

    @GetMapping
    @ContentType("image/jpeg")
    public static String returnIco() throws IOException {
        return ServerService.readAllBytes(new File(GetProperties.getProperty("resources_path") + "/" + "favicon.ico"));
    }
}
