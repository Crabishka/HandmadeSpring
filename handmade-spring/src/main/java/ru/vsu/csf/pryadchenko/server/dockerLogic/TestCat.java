package ru.vsu.csf.pryadchenko.server.dockerLogic;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class TestCat {
    public String name;
    public int age;
    public int weight;

    public TestCat() {
    }
}
