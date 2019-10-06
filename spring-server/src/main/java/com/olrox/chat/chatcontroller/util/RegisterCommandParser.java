package com.olrox.chat.chatcontroller.util;

import java.util.StringTokenizer;

public class RegisterCommandParser {
    private String parsedRole;
    private String parsedName;

    public void parse(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, " ");

        // Skip /register
        tokenizer.nextToken();

        parsedRole = tokenizer.nextToken();

        StringBuilder username = new StringBuilder();

        while (tokenizer.hasMoreTokens()) {
            username.append(tokenizer.nextToken());
        }

        parsedName = username.toString();
    }

    public String getParsedRole() {
        return parsedRole;
    }

    public String getParsedName() {
        return parsedName;
    }
}
