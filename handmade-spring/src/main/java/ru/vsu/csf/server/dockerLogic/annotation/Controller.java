<<<<<<< HEAD:handmade-spring/src/main/java/ru/vsu/csf/pryadchenko/spring/dockerLogic/Controller.java
package ru.vsu.csf.pryadchenko.spring.dockerLogic;
=======
package ru.vsu.csf.server.dockerLogic.annotation;
>>>>>>> BROKEN:handmade-spring/src/main/java/ru/vsu/csf/server/dockerLogic/annotation/Controller.java

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

public @interface Controller {
    String value() default "";
}
