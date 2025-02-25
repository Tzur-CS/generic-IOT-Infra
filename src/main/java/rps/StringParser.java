package rps;

import java.util.HashMap;
import java.util.Map;

public class StringParser implements RPSParser<Map<String, String>, String> {
    /*
    protocol:
    command&key@value#key@value
    example: regCompany&companyNAme@Apple#companyID@1234
    */
    @Override
    public Map<String, String> parse(String str) {
        Map<String, String> map = new HashMap<>();

        String[] requestSplit = str.split("&");
        map.put("commandName", requestSplit[0]);
        if (requestSplit.length == 1) {
            return null;
        }

        String[] requestsSplitArg = requestSplit[1].split("#");
        for (String argNameAndArgValue : requestsSplitArg) {
            String[] areNameAndArgsValueSpited = argNameAndArgValue.split("@");
            map.put(areNameAndArgsValueSpited[0], areNameAndArgsValueSpited[1]);
        }

        return map;
    }
}