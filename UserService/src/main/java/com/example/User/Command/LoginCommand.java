package com.example.User.Command;

import com.example.User.Service.UserService;

public class LoginCommand implements Command {
    private final UserService userService;
    private final String email;
    private final String password;

    public LoginCommand(UserService userService, String email, String password) {
        this.userService = userService;
        this.email = email;
        this.password = password;
    }

    @Override
    public String execute() {
        return userService.login(email, password).getBody();
    }
}


