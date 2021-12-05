<<<<<<< HEAD:handmade-spring/src/main/java/ru/vsu/csf/pryadchenko/spring/dockerLogic/ContentType.java
package ru.vsu.csf.pryadchenko.spring.dockerLogic;
=======
package ru.vsu.csf.server.dockerLogic.annotation;
>>>>>>> BROKEN:handmade-spring/src/main/java/ru/vsu/csf/server/dockerLogic/annotation/ContentType.java

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentType {
    String value() default "";
}
