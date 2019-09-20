package com.olrox.chat.controller.util.command;

import java.util.Collections;
import java.util.List;

public class ParsedCommand {
    private Type type;
    private List<String> params;

    public ParsedCommand() {
    }

    public ParsedCommand(Type type) {
        this.type = type;
        this.params = Collections.emptyList();
    }

    public ParsedCommand(Type type, List<String> params) {
        this.type = type;
        this.params = params;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }
}
