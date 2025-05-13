//package com.example.VideoService.controller;
//
//
//import com.example.VideoService.seeder.DatabaseSeeder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("/videos/seed")
//public class SeederController {
//
//    private final DatabaseSeeder databaseSeeder;
//
//    public SeederController(DatabaseSeeder databaseSeeder) {
//        this.databaseSeeder = databaseSeeder;
//    }
//
//    @PostMapping
//    public ResponseEntity<String> seed() {
//        try {
//            String response = databaseSeeder.seedVideos();  // If you want this to return something, you should change it
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Seeding failed: " + e.getMessage());
//        }
//    }
//
//
//
//}
