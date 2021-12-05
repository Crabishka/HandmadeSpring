package ru.vsu.csf.server.docker.math;

import ru.vsu.csf.server.dockerLogic.annotation.ContentType;
import ru.vsu.csf.server.dockerLogic.annotation.GetMapping;
import ru.vsu.csf.server.dockerLogic.annotation.Param;
import ru.vsu.csf.server.dockerLogic.annotation.Controller;

@Controller("multiply")
public class Multiply {

    @GetMapping("multiply")
    @ContentType("text/html; charset=UTF-8")
    public static String doGet(@Param(name = "a") String a, @Param(name = "b") String b) {
        if (a == null || b == null || a.equals("") || b.equals("")) return "error";
        return String.valueOf(Integer.parseInt(a) * Integer.parseInt(b));
    }
}
