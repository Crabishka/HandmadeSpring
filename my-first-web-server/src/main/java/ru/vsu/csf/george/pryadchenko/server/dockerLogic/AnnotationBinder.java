package ru.vsu.csf.george.pryadchenko.server.dockerLogic;

import java.lang.annotation.Annotation;
import java.util.Objects;

public class AnnotationBinder {

    Class<? extends Annotation> type;

    String value;

    AnnotationBinder(Annotation annotation, String value) {
        type = annotation.annotationType();
        this.value = value;
    }

    public AnnotationBinder(Annotation annotation) {
        this(annotation, "");
        try {
            if (annotation.annotationType().equals(GetMapping.class)) {
                this.value = ((GetMapping) annotation).value();
            } else if (annotation.annotationType().equals(ContentType.class)) {
                this.value = ((ContentType) annotation).value();
            } else if (annotation.annotationType().equals(Controller.class)) {
                this.value = ((Controller) annotation).value();
            } else if (annotation.annotationType().equals(Repository.class)) {
                this.value = ((Repository) annotation).value();
            } else if (annotation.annotationType().equals(Service.class)) {
                this.value = ((Service) annotation).value();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AnnotationBinder(Class<? extends Annotation> annotation) {
        this.type = annotation;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationBinder that = (AnnotationBinder) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
