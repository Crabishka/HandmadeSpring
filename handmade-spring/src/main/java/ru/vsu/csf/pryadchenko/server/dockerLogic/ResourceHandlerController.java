package ru.vsu.csf.pryadchenko.server.dockerLogic;

import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.ContentType;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Controller;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.GetMapping;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Param;
import ru.vsu.csf.pryadchenko.server.logic.GetProperties;
import ru.vsu.csf.pryadchenko.server.logic.ServerService;

import java.io.File;
import java.io.IOException;

/**
 * developer controlled class
 * Doesn't comply with the Controller contract
 */
@Controller
public class ResourceHandlerController {


    @GetMapping("favicon.ico")
    @ContentType("image/jpeg")
    public static String ico(@Param(name = "path") String path) throws IOException {
        String str = GetProperties.getProperty("resources_path") + "/" + path;
        return ServerService.readAllBytes(new File(str));
    }
}
