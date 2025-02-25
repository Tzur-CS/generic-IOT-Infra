package plugandplay;

import observer.Callback;
import observer.Dispatcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class DirWatcher {
    private final Dispatcher<List<WatchEvent<?>>> dispatcher;
    private final WatchService watcher;
    private volatile boolean isThreadWorking = false;
    private Thread workingThread;

    public DirWatcher(String jarFolder) throws IOException {
        jarFolder = Objects.requireNonNull(jarFolder, "jar folder must not be null");
        dispatcher = new Dispatcher<>();
        watcher = FileSystems.getDefault().newWatchService();

        File tempJarFile = new File(jarFolder);
        if (!tempJarFile.isDirectory()) {
            throw new IllegalArgumentException("must provide a folder");
        }
        Path jar = Paths.get(jarFolder);

        jar.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
    }

    public void registerCallback(Callback<List<WatchEvent<?>>> call) {
        dispatcher.subscribe(call);
    }

    public void stopService() throws IOException, InterruptedException {
        isThreadWorking = false;
        dispatcher.stopService();
        watcher.close();

        workingThread.join();

    }

    public void start() {
         workingThread = new Thread(new WorkingThread());
         workingThread.start();
    }

    private class WorkingThread implements Runnable {
        @Override
        public void run() {
            isThreadWorking = true;
            while (isThreadWorking) {
                WatchKey key = null;
                try {
                    key = watcher.take();
                } catch (InterruptedException e) {
                    continue;
                } catch (ClosedWatchServiceException e) {
                    break;
                }

                dispatcher.publish(key.pollEvents());

                if (!key.reset()) {
                    try {
                        stopService();
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
        }
    }
}


