package plugandplay;

import command.Command;
import factory.Factory;
import rps.RPSParser;
import rps.StringParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.lang.Thread.sleep;

public class PlugAndPlayTest {

    private PlugAndPlay plugAndPlay;
    private Factory<String, Command, Map<String, String>> factory;

    @BeforeEach
    public void setUp() throws ClassNotFoundException {
        factory = new Factory<>();
        plugAndPlay = new PlugAndPlay("/home/tzur/git/java/projects/src/il/co/ilrd/command", factory);
    }


    @Test
    public void testStartAndStopCalls() {
        plugAndPlay.start();

        // Call stop
        plugAndPlay.stop();
    }

    @Test
    public void testLoadCommandFromJar() throws Exception {
        plugAndPlay.start();


        try {
            sleep(10000);
            sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



        RPSParser<Map<String, String>, String> parser = new StringParser();
        Map<String, String> requestArgs = parser.parse("command.RegCompany&companyName@Infinity");
      //  for (String key : factory.factory.keySet()){
       //     System.out.println(key);
//
       // }
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Command command = factory.create(requestArgs.get("commandName"), requestArgs);
        //command.execute();



        plugAndPlay.stop();
    }
}
