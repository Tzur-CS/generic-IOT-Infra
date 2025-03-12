package dynamicjarloader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class DynamicJarLoader {
    private final Class<?> interfaceClass;


    public DynamicJarLoader(String implementedInterface) throws ClassNotFoundException {
        interfaceClass = Class.forName(implementedInterface);
    }

    public List<Class<?>> load(String jarPath) {
        List<Class<?>> loadedClasses = new ArrayList<>();

        try (JarFile jarFile = new JarFile(jarPath);
             URLClassLoader loader = new URLClassLoader(new URL[]{new URL("jar:file:" + jarPath + "!/")});) {

            Enumeration<JarEntry> jarEntries = jarFile.entries();

            while (jarEntries.hasMoreElements()) {
                String entryName = jarEntries.nextElement().getName();
                if (!entryName.endsWith(".class")) {
                    continue;
                }

                String className = entryName.substring(0, entryName.indexOf(".class"));
                className = className.replace('/', '.');

                Class<?> loadedClass;
                try {
                    loadedClass = loader.loadClass(className);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

                if (!loadedClass.isInterface() && interfaceClass.isAssignableFrom(loadedClass)) {
                    loadedClasses.add(loadedClass);
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        return loadedClasses;
    }
}
