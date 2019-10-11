package com.olrox.chat.chatcontroller.util;

import java.util.StringTokenizer;

public class LoginCommandParser {
    private String parsedName;
    private String parsedPassword;

    public void parse(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, " ");

        // Skip /login
        tokenizer.nextToken();

        parsedName = tokenizer.nextToken();

        parsedPassword = tokenizer.nextToken();
    }

    public String getParsedName() {
        return parsedName;
    }

    public String getParsedPassword() {
        return parsedPassword;
    }
}
