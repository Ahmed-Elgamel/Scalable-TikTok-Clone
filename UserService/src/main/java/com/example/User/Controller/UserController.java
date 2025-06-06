package com.example.User.Controller;

import com.example.User.Command.CommandInvoker;
import com.example.User.Command.LoginCommand;
import com.example.User.Command.LogoutCommand;
import com.example.User.Model.User;
import com.example.User.Security.JwtUtil;
import com.example.User.Service.UserService;
import com.example.User.seed.DatabaseSeeder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final CommandInvoker commandInvoker;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final DatabaseSeeder databaseSeeder;

    public UserController(UserService userService, CommandInvoker commandInvoker, JwtUtil jwtUtil, DatabaseSeeder databaseSeeder) {
        this.userService = userService;
        this.commandInvoker = commandInvoker;
        this.jwtUtil = jwtUtil;
        this.databaseSeeder = databaseSeeder;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        UUID userId = jwtUtil.extractUserId(token);
        return userService.getUser(userId);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        UUID userId = jwtUtil.extractUserId(token);
        return userService.deleteUser(userId);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestHeader("Authorization") String authHeader, @RequestBody User user){
        String token = authHeader.substring(7);
        UUID userId = jwtUtil.extractUserId(token);
        return userService.updateUser(userId, user);
    }

    @PutMapping("/activateUser")
    public ResponseEntity<String> activateUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        UUID userId = jwtUtil.extractUserId(token);
        return userService.activateUser(userId);
    }

    @PutMapping("/deactivateUser")
    public ResponseEntity<String> deactivateUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        UUID userId = jwtUtil.extractUserId(token);
        return userService.deactivateUser(userId);
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
            User existingUser = userService.findByEmail(user.getEmail());
            String token = jwtUtil.generateToken(existingUser.getId());
            return ResponseEntity.ok("Bearer " + token + "   ID: " + existingUser.getId());
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

    @GetMapping("/seed")
    public String seed() {
        databaseSeeder.seed();
        return "Database seeded!!";
    }

    @GetMapping("/seedWithUUIDs")
    public String seedWithUUID() {
        databaseSeeder.seedWithUUIDs();
        return "Database seeded with UUID!!";
    }
}
