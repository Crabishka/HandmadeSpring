<<<<<<< HEAD:handmade-spring/src/main/java/ru/vsu/csf/pryadchenko/spring/docker/math/Adder.java
package ru.vsu.csf.pryadchenko.spring.docker.math;

import ru.vsu.csf.pryadchenko.spring.dockerLogic.ContentType;
import ru.vsu.csf.pryadchenko.spring.dockerLogic.GetMapping;
import ru.vsu.csf.pryadchenko.spring.dockerLogic.Param;
import ru.vsu.csf.pryadchenko.spring.dockerLogic.Controller;
=======
package ru.vsu.csf.server.docker.math;

import ru.vsu.csf.server.dockerLogic.annotation.ContentType;
import ru.vsu.csf.server.dockerLogic.annotation.GetMapping;
import ru.vsu.csf.server.dockerLogic.annotation.Param;
import ru.vsu.csf.server.dockerLogic.annotation.Controller;
>>>>>>> BROKEN:handmade-spring/src/main/java/ru/vsu/csf/server/docker/math/Adder.java

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
