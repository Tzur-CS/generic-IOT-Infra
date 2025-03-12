package plugandplay;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class DirWatcherTest {

    @Test
    void start() throws IOException, InterruptedException {
        DirWatcher watcher = new DirWatcher("/home/tzur/git/java/projects/src/il/co/ilrd/command");
/* fix to a list of events
        watcher.registerCallback(new Callback<>((d)->{
            System.out.println(((Path) d.context()).getFileName());
        }, ()->{
            System.out.println("Stopped Service");
        }));
*/
        watcher.start();

        Thread.sleep(15000);

        watcher.stopService();

    }
}
