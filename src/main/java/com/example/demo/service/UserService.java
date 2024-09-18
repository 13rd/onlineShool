package com.example.demo.service;


import com.example.demo.model.Course;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;

import com.example.demo.requests.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseService courseService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("Cant find user with username: " + username);
        }
        return user.map(MyUserDetails::new).orElseThrow((() -> new UsernameNotFoundException(username + " not found")));
    }
    public boolean saveUser(UserModel user) {
        Optional<UserModel> userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB.isPresent()) {
            return false;
        }
//        user.setRole("ROLE_USER");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public void subscribeToCourse(UserModel user, String courseId) {
        Optional<UserModel> userFromDB = userRepository.findByUsername(user.getUsername());
        Optional<Course> courseFromDB = courseService.loadCourseById(courseId);
         if (userFromDB.isPresent() && courseFromDB.isPresent()) {
             user.addCourse(courseFromDB.get());
             userRepository.save(user);
        }

    }


    public boolean deleteUser(String userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }



    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserModel> getUserById(String id) {
        return userRepository.findById(id);
    }

//    public void register(User user) {
//        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
//        userRepository.save(user);
//    }


}
