package di.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static di.container.DIConfigurator.getAllClasses;

public class InjectAnnotationScanner implements AnnotationScanner {
    private Class<? extends Annotation> annotation;

    private List<Class<?>> classesWithInjectAnnotation;
    private List<Object> singletons;

    @Override
    public void scanClasses() {
        for (Class<?> clazz: getAllClasses()) {
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                Annotation[][] paramsAnnotations = constructor.getParameterAnnotations();
                Class<?>[] parameterTypes = constructor.getParameterTypes();

                for (int i = 0; i < paramsAnnotations.length; i++) {
                    for (int j = 0; j < paramsAnnotations[i].length; j++) {
                        if (paramsAnnotations[i][j].annotationType() == this.annotation) {
                            Class<?> injectableType = parameterTypes[i];

                            System.out.println(injectableType + " " + paramsAnnotations[i][j].annotationType());
                        }
                    }
                }
            }
        }
    }

    private void getInjectParamTypes(Class<?> clazz) {

    }

    public InjectAnnotationScanner(Class<? extends Annotation> annotation) {
        this.annotation = annotation;

        classesWithInjectAnnotation = new ArrayList<>();
        singletons = new ArrayList<>();
    }

    @Override
    public void setAnnotation(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }
}
