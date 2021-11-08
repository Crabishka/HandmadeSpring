package Server.Docker.Servlet;

import Server.Docker.GetMapping;
import Server.Docker.Param;
import Server.Docker.RequestMapping;

@RequestMapping("/adder")
public class Adder {

    @GetMapping
    public static String doGet(@Param(name = "a") String a, @Param(name = "b") String b) {
        if (a == null || b == null || a.equals("") || b.equals("")) return "error";
        return String.valueOf(Integer.parseInt(a) + Integer.parseInt(b));
    }
}
