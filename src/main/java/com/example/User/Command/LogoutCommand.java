package com.example.User.Command;

import com.example.User.Service.UserService;

public class LogoutCommand implements Command {
    private final UserService userService;

    public LogoutCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute() {
        return userService.logout().getBody();
    }
}