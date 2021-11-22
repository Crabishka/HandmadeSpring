package ru.vsu.csf.george.pryadchenko.server.docker.math;

import ru.vsu.csf.george.pryadchenko.server.docker.GetMapping;
import ru.vsu.csf.george.pryadchenko.server.docker.Param;
import ru.vsu.csf.george.pryadchenko.server.docker.RequestMapping;

@RequestMapping("/multiply")
public class Multiply {

    @GetMapping
    public static String doGet(@Param(name = "a") String a, @Param(name = "b") String b) {
        if (a == null || b == null || a.equals("") || b.equals("")) return "error";
        return String.valueOf(Integer.parseInt(a) * Integer.parseInt(b));
    }
}
