package com.olrox.chat.command;

public enum CommandType {
    SEND_MESSAGE("/message"),
    REGISTER("/register"),
    LEAVE("/leave"),
    EXIT("/exit");

    private String code;

    CommandType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
