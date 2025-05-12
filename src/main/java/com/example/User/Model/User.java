package com.example.User.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(generator = "random-id")
    @GenericGenerator(name = "random-id", strategy = "com.example.User.Config.RandomIdGenerator")
    @Column(name = "id",updatable = false, nullable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    private boolean isActive = true;

    public User() {
    }

    public User(UserBuilder builder) {
        this.username = builder.username;
        this.email = builder.email;
        this.password = builder.password;
        this.isActive = builder.isActive;
    }

    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public static class UserBuilder {

        private Long id;
        private String username;
        private String email;
        private String password;
        private boolean isActive;

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}


