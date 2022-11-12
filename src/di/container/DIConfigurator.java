package di.container;

import di.annotations.inject.Inject;
import di.annotations.inject.Injectable;
import di.scanner.AnnotationScanner;
import di.scanner.InjectAnnotationScanner;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class DIConfigurator {

    private static List<Class<?>> annotatedClasses;
    private static List<Class<?>> allClasses;

    public static List<Class<?>> getAnnotatedClasses() {
        return annotatedClasses;
    }

    public static List<Class<?>> getAllClasses() {
        return allClasses;
    }

    private static String path;

    public static void run(String basePackage) throws ClassNotFoundException, URISyntaxException, IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        annotatedClasses = new LinkedList<>();
        allClasses = new ArrayList<>();

        path = basePackage.replace('.', '\\');

        Iterator<URL> resources = classLoader.getResources(path).asIterator();

        while (resources.hasNext()) {
            URL resource = resources.next();

            File file = new File(resource.toURI());

            for (File classFile: file.listFiles()) {
                if (classFile.isDirectory()) {
                    getAnnotatedClassesFromPackage(classFile, Injectable.class);
                } else {
                    addAnnotatedClass(classFile, Injectable.class);
                }
            }
        }

        AnnotationScanner annotationScanner = new InjectAnnotationScanner(Inject.class);

        annotationScanner.scanClasses();
    }

    private static void addAnnotatedClass(File classFile, Class<? extends Annotation> annotation) throws ClassNotFoundException {
        if (classFile.getName().endsWith(".class")) {
            String classPath = classFile.getPath();

            String pathToClass = classPath.substring(classPath.indexOf(path));

            pathToClass = pathToClass.replace('\\', '.');

            Class<?> clazz = Class.forName(pathToClass.substring(0, pathToClass.lastIndexOf('.')));

            allClasses.add(clazz);

            if (clazz.isAnnotationPresent(annotation)) {
                annotatedClasses.add(clazz);
            }
        }
    }

    private static void getAnnotatedClassesFromPackage(File file, Class<? extends Annotation> annotation) {
        try {
            for(File file1 : Objects.requireNonNull(file.listFiles())) {
                if (file1.isDirectory()) {
                    getAnnotatedClassesFromPackage(file1, annotation);
                } else {
                    addAnnotatedClass(file1, annotation);
                }
            }
        } catch (NullPointerException | ClassNotFoundException exception) {
            System.err.println(Arrays.toString(exception.getStackTrace()));
        }
    }
}
