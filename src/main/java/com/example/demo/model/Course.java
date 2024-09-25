package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

import java.util.*;

@Document(collection = "courses")
@Data
@NoArgsConstructor
public class Course {
    @Id
    private String id;
    private String title;
    private String description;
    private String creatorId;
    private int maxStudents;
    private int studentsCount = 0;
    private List<String> lessons = new ArrayList<>();
    private Date createdAt = new Date();

//    public Course(){
//        this.lessons = new ArrayList<>();
//        Date date = new Date();
//        System.out.println("Текущая дата и время: " + date.toString());
//        this.createdAt = date;
//    }

    public void addLesson(Lesson lesson){
        lessons.add(lesson.getId());
    }

    // getters and setters
}
