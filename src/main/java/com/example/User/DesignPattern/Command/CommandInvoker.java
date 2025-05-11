package com.example.User.DesignPattern.Command;

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
