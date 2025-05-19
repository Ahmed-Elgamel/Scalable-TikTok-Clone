package com.example.User.seed;

import com.example.User.Model.User;
import com.example.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class DatabaseSeeder {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * This method seeds the database with a specified number of users.
     * It deletes all existing users and creates new ones with unique usernames and emails.
     */


    public static final List<UUID> USERS_IDS = Arrays.asList(
            UUID.fromString("e4eaaaf2-d142-11e1-b3e4-080027620cdd"),
            UUID.fromString("f91f9423-0f8e-4ea3-9bd7-c9d2c1c7639e"),
            UUID.fromString("4cdbd23e-4425-4c1d-8f16-871b21a379ce"),
            UUID.fromString("01f5c999-cc15-4f7a-bb9d-91e16c5eb9fb"),
            UUID.fromString("abed0a2a-fc63-4ea0-8c76-c77e68fc4cb3"),
            UUID.fromString("693a90ec-d7c2-49cc-8914-6cd205934c49"),
            UUID.fromString("eef74026-03a0-4e5a-8d0d-78800e8cb5c2"),
            UUID.fromString("908f9a15-62f7-4f92-8cb1-148b12e2d80f"),
            UUID.fromString("3e17453c-bc38-41cf-878f-2f51134b3c02"),
            UUID.fromString("53c2f5f3-5577-482b-a058-b56c0dc0ea9a")
    );


    public void seed() {
        userRepository.deleteAll();
        int numberOfUsers = 10;
        for(int i = 0; i < numberOfUsers; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@gmail.com");
            user.setPassword(passwordEncoder.encode("password" + i));
            userRepository.save(user);
        }
    }

    public void seedWithUUIDs() {
        userRepository.deleteAll();
        for (int i = 0; i < USERS_IDS.size(); i++) {
            User user = new User();
            user.setId(USERS_IDS.get(i));
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@gmail.com");
            user.setPassword(passwordEncoder.encode("password" + i));
            userRepository.save(user);
        }
    }


}
