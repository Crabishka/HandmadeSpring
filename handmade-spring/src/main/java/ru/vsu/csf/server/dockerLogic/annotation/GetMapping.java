<<<<<<< HEAD:handmade-spring/src/main/java/ru/vsu/csf/pryadchenko/spring/dockerLogic/GetMapping.java
package ru.vsu.csf.pryadchenko.spring.dockerLogic;
=======
package ru.vsu.csf.server.dockerLogic.annotation;
>>>>>>> BROKEN:handmade-spring/src/main/java/ru/vsu/csf/server/dockerLogic/annotation/GetMapping.java

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetMapping {
    String value() default "";
}


