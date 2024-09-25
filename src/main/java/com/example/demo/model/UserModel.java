package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class UserModel{
    @Id
    private String id;
    private String type;
    private String role;
    private String username;
    private String email;
    private String passwordHash;
    private String name;
    private String surname;
    private List<String> coursesEnrolled = new ArrayList<>();
    private List<String> reporstList = new ArrayList<>();

    // getters and setters


    public void setPassword(String password) {

        this.passwordHash = password;
    }


    public String getPassword() {
        return passwordHash;
    }

    public void addCourse(Course course){
        coursesEnrolled.add(course.getId());
    }

    public void addReport(Report report){
        reporstList.add(report.getId());
    }

    public void deleteCourse(Course course){
        coursesEnrolled.remove(course);
    }
}