package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@Document(collection = "lessons")
public class Lesson {
    @Id
    private String id;
    private String courseId;
    private String title;
    private String description;
    private String videoUrl;
    private int duration;
    private Date createdAt;

    // getters and setters
}
