package com.example.User.Controller;

import com.example.User.DesignPattern.Command.CommandInvoker;
import com.example.User.DesignPattern.Command.LoginCommand;
import com.example.User.DesignPattern.Command.LogoutCommand;
import com.example.User.Model.User;
import com.example.User.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final CommandInvoker commandInvoker;
    private final UserService userService;

    public UserController(UserService userService, CommandInvoker commandInvoker) {
        this.userService = userService;
        this.commandInvoker = commandInvoker;
    }

    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestBody User user){
        return ResponseEntity.ok(userService.addUser(user));
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user){
        return userService.updateUser(id, user);
    }

    @PutMapping("/activateUser/{id}")
    public ResponseEntity<String> activateUser(@PathVariable Long id){
        return userService.activateUser(id);
    }

    @PutMapping("/deactivateUser/{id}")
    public ResponseEntity<String> deactivateUser(@PathVariable Long id){
        return userService.deactivateUser(id);
    }

    @GetMapping("/findByUsername")
    public ResponseEntity<List<User>> searchUser(@RequestParam String username){
        return userService.searchUser(username);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        commandInvoker.setCommand(new LoginCommand(userService, user.getEmail(), user.getPassword()));
        return ResponseEntity.ok(commandInvoker.executeCommand());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        commandInvoker.setCommand(new LogoutCommand(userService));
        return ResponseEntity.ok(commandInvoker.executeCommand());
    }
}
