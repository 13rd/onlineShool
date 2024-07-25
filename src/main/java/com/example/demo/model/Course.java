package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Document(collection = "courses")
@Data
public class Course {
    @Id
    private String id;
    private String title;
    private String description;
    private double price;
    private String creatorId;
    private int studentsCount;
    private List<String> lessons;
    private Date createdAt;

    // getters and setters
}
