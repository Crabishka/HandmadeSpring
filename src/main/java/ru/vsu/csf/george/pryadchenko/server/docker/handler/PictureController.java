package ru.vsu.csf.george.pryadchenko.server.docker.handler;

import ru.vsu.csf.george.pryadchenko.server.dockerLogic.GetMapping;
import ru.vsu.csf.george.pryadchenko.server.dockerLogic.Param;
import ru.vsu.csf.george.pryadchenko.server.dockerLogic.Controller;
import ru.vsu.csf.george.pryadchenko.server.logic.GetProperties;
import ru.vsu.csf.george.pryadchenko.server.logic.ServerService;

import java.io.File;
import java.io.IOException;

@Controller("/picture")
public class PictureController {


    @GetMapping
    public static String doGet(@Param(name = "path") String path) throws IOException {
        String str = GetProperties.getProperty("resources_path") + "/" + path;
        return new String(ServerService.readAllBytes(new File(str)));
    }
}
