package Server.Docker.Servlet;

import Server.Docker.GetMapping;
import Server.Docker.Param;
import Server.Docker.RequestMapping;

@RequestMapping("/multiply")
public class Multiply{

    @GetMapping
    public static String doGet(@Param(name = "a") String a, @Param(name = "b") String b) {
        if (a == null || b == null) return "error";
        return String.valueOf(Integer.parseInt(a) * Integer.parseInt(b));
    }
}
