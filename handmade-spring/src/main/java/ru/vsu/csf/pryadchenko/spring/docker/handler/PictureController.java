package ru.vsu.csf.pryadchenko.spring.docker.handler;

import ru.vsu.csf.pryadchenko.spring.dockerLogic.GetMapping;
import ru.vsu.csf.pryadchenko.spring.dockerLogic.Param;
import ru.vsu.csf.pryadchenko.spring.dockerLogic.Controller;
import ru.vsu.csf.pryadchenko.spring.logic.GetProperties;
import ru.vsu.csf.pryadchenko.spring.logic.ServerService;

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
