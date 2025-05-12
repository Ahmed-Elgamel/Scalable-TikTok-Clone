package com.example.User.Service;

import com.example.User.Model.User;
import com.example.User.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> addUser(User user) {
        User newUser = new User.UserBuilder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .isActive(true)
                .build();

        userRepository.save(newUser);
        return ResponseEntity.ok("User added successfully");
    }

    public ResponseEntity<User> getUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<String> deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<String> updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            if(user.getUsername() != null) existingUser.setUsername(user.getUsername());
            if(user.getEmail() != null) existingUser.setEmail(user.getEmail());
            if(user.getPassword() != null) existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(existingUser);
            return ResponseEntity.ok("User updated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<String> activateUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setActive(true);
            userRepository.save(user);
            return ResponseEntity.ok("User activated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<String> deactivateUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setActive(false);
            userRepository.save(user);
            return ResponseEntity.ok("User deactivated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<List<User>> searchUser(String name) {
        List<User> users = userRepository.findByUsername(name);
        users = users.stream().distinct().toList();
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<String> login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.ok("Login successful");
        }  else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout successful");
    }
}
