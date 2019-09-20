package com.olrox.chat.controller.util.command;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.StringTokenizer;

@Component
public class CommandParser {
    public ParsedCommand parse(String string){
        if(string == null) {
            return new ParsedCommand(Type.EXIT);
        }

        if(!string.startsWith("/")) {
            ArrayList<String> params = new ArrayList<>();
            params.add(string);
            return new ParsedCommand(Type.SEND_MESSAGE, params);
        }

        if(string.startsWith("/register ")){
            ArrayList<String> params = new ArrayList<>();
            StringTokenizer tokenizer = new StringTokenizer(string, " ");
            while (tokenizer.hasMoreTokens()){
                params.add(tokenizer.nextToken());
            }

            return new ParsedCommand(Type.REGISTER, params);
        }

        if(string.equals("/leave")){
            return new ParsedCommand(Type.LEAVE);
        }

        if(string.equals("/exit")){
            return new ParsedCommand(Type.EXIT);
        }

        return new ParsedCommand(Type.UNKNOWN);
    }
}
