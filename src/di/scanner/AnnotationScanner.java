package di.scanner;

import java.lang.annotation.Annotation;

public interface AnnotationScanner {
    void scanClasses();

    void setAnnotation(Class<? extends Annotation> annotation);
}
