package plugandplay;

import command.Command;
import dynamicjarloader.DynamicJarLoader;
import factory.Factory;
import observer.Callback;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class PlugAndPlay {
    private final Factory<String, Command, Map<String, String>> factory;
    private final DirWatcher dirWatcher;
    private final DynamicJarLoader dynamicJarLoader;
    private final String folderPath;

    public PlugAndPlay(String folderPath, Factory<String, Command, Map<String, String>> factory) throws ClassNotFoundException {
        this.factory = factory;
        this.folderPath = folderPath;

        try {
            dirWatcher = new DirWatcher(folderPath);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        dynamicJarLoader = new DynamicJarLoader(Command.class.getName());
    }

    public void start() {

        //System.out.println(((Path) event.context()).getFileName());
        dirWatcher.registerCallback(new Callback<>(this::handleEvent, () -> System.out.println("Stopped Service")));

        dirWatcher.start();
    }

    public void handleEvent(List<WatchEvent<?>> events) {
        Set<Path> setPath = new HashSet<>();
        for (WatchEvent<?> event : events) {
            if (event.kind() != OVERFLOW){
                setPath.add((Path)event.context());
            }
        }

        for (Path eventPath : setPath) {
            String fullPath = folderPath + "/" + eventPath;

            if (!JarValidator.validate(fullPath)) {
                continue;
            }

            List<Class<?>> loadedClasses;
            try {
                loadedClasses = dynamicJarLoader.load(fullPath);
            } catch (Exception e) {
                throw new RuntimeException("Error loading classes from JAR: " + fullPath, e);
            }

            for (Class<?> clazz : loadedClasses) {
                factory.add(clazz.getName(), (args) -> {
                    try {
                        return (Command) clazz.getDeclaredConstructor(Map.class).newInstance(args);
                    } catch (Exception e) {
                        throw new RuntimeException("Error creating instance of class: " + clazz.getName(), e);
                    }
                });

            }
        }
    }

    public void stop() {
        try {
            dirWatcher.stopService();
        } catch (IOException | InterruptedException  ignore) {
        }
    }

}
