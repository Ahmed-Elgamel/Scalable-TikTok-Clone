package com.example.User.Controller;

import com.example.User.Command.CommandInvoker;
import com.example.User.Command.LoginCommand;
import com.example.User.Command.LogoutCommand;
import com.example.User.Model.User;
import com.example.User.Security.JwtUtil;
import com.example.User.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final CommandInvoker commandInvoker;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, CommandInvoker commandInvoker, JwtUtil jwtUtil) {
        this.userService = userService;
        this.commandInvoker = commandInvoker;
        this.jwtUtil = jwtUtil;
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
        String result = commandInvoker.executeCommand();
        if (result.equalsIgnoreCase("Login successful")) {
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok("Bearer " + token);
        }
        else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        commandInvoker.setCommand(new LogoutCommand(userService));
        return ResponseEntity.ok(commandInvoker.executeCommand());
    }
}
