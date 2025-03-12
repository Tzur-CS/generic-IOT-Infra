package rps;

import command.*;
import connection.connectionserviceutils.Status;
import factory.Factory;
import gatewayserver.Message;
import threadpool.ThreadPool;

import java.util.Map;
import java.util.NoSuchElementException;

public class RPS {
    private final ThreadPool threadPool = new ThreadPool();
    private final Factory<String, Command, Map<String, String>> factory = new Factory<>();

    public RPS() {
        factory.add("RegCompany", RegCompany::new);
        factory.add("RegProduct", RegProduct::new);
        factory.add("RegDevice", RegDevice::new);
        factory.add("RegUpdate", RegUpdate::new);
    }

//    public void handleRequest(String request) {
//        threadPool.submit(() -> {
//            Map<String, String> requestArgs = parser.parse(request);
//
//            Command command = factory.create(requestArgs.get("commandName"), requestArgs);
//            command.execute();
//            return null;
//        });
//    }

    public void handleRequest(Message message) {
        threadPool.submit(() -> {
            Map<String, String> requestArgs = new JSONParserGson().parse(message.getRequest());
            if (requestArgs == null){
                message.sendFeedback("invalid request", Status.BAD_REQUEST);
                return null;
            }
            try {
                Command command = factory.create(requestArgs.get("command"), requestArgs);
                command.execute();
            }
            catch (NoSuchElementException e){
                message.sendFeedback("no such request", Status.REQUEST_NOT_FOUND);
                return null;
            }
            message.sendFeedback("request fulfilled", Status.SUCCESS);
            return null;
        });
    }
}