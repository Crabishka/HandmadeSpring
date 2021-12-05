package ru.vsu.csf.pryadchenko.server.docker.math;

import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.ContentType;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Controller;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.GetMapping;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Param;

@Controller("")
public class Adder {

    @GetMapping("")
    @ContentType("text/html; charset=UTF-8")
    public static String doGet(@Param(name = "a") String a, @Param(name = "b") String b) {
        if (a == null || b == null || a.equals("") || b.equals("")) return "error";
        return String.valueOf(Integer.parseInt(a) + Integer.parseInt(b));
    }

    @GetMapping("minus")
    @ContentType("text/html; charset=UTF-8")
    public static String doGet1(@Param(name = "a") String a, @Param(name = "b") String b) {
        if (a == null || b == null || a.equals("") || b.equals("")) return "error";
        return String.valueOf(Integer.parseInt(a) - Integer.parseInt(b));
    }

}
