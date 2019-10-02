package com.olrox.chat.controller.chat.util;

import org.springframework.stereotype.Component;

import java.util.StringTokenizer;

@Component
public class MessageParser {
    public String[] parseRegisterMessage(String text) {
        String[] params = new String[2];

        StringTokenizer tokenizer = new StringTokenizer(text, " ");

        // Skip /register
        tokenizer.nextToken();

        // Add role
        params[0] = tokenizer.nextToken();

        StringBuilder username = new StringBuilder();

        while (tokenizer.hasMoreTokens()) {
            username.append(tokenizer.nextToken());
        }

        params[1] = username.toString();

        return params;
    }
}
