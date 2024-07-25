package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String type;
    private String username;
    private String email;
    private String passwordHash;
    private String name;
    private String surname;
    private List<String> coursesEnrolled;

    // getters and setters
}