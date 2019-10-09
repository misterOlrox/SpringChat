package com.olrox.chat.chatcontroller.util;

import java.util.StringTokenizer;

public class RegisterCommandParser {
    private String parsedRole;
    private String parsedName;
    private String parsedPassword;

    public void parse(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, " ");

        // Skip /register
        tokenizer.nextToken();

        parsedRole = tokenizer.nextToken();

        parsedName = tokenizer.nextToken();

        parsedPassword = tokenizer.nextToken();
    }

    public String getParsedRole() {
        return parsedRole;
    }

    public String getParsedName() {
        return parsedName;
    }

    public String getParsedPassword() {
        return parsedPassword;
    }
}
