package com.olrox.chat.service;

import com.olrox.chat.command.CommandType;
import org.springframework.stereotype.Service;

@Service
public class CommandParser {
    public CommandType parse(String commandString) {
        if(commandString == null) {
            return CommandType.EXIT;
        }

        if(!commandString.startsWith("/")) {
            return CommandType.SEND_MESSAGE;
        }

        for(CommandType command : CommandType.values()){
            if(commandString.startsWith(command.getCode())) {
                return command;
            }
        }

        return CommandType.SEND_MESSAGE;
    }
}
