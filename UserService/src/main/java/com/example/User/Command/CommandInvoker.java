package com.example.User.Command;

import org.springframework.stereotype.Component;

@Component
public class CommandInvoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public String executeCommand() {
        return command.execute();
    }
}
