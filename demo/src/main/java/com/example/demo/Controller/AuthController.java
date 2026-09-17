package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Studententity;
import com.example.demo.repository.StudentRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private StudentRepository studentRepository;

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Studententity studentDetails) {

        // Check username is provided
        if (studentDetails.getUsername() == null ||
            studentDetails.getUsername().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Username is required");
        }

        // Check password is provided
        if (studentDetails.getPassword() == null ||
            studentDetails.getPassword().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Password is required");
        }

        // Get all existing students
        List<Studententity> students = studentRepository.findAll();

        // Check whether username already exists
        for (Studententity student : students) {

            // IMPORTANT:
            // Check null before using equals()
            if (student.getUsername() != null &&
                student.getUsername().equals(studentDetails.getUsername())) {

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Username already exists");
            }
        }

        // Username is new, save student
        studentRepository.save(studentDetails);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Registration Successful");
    }


    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Studententity loginDetails) {

        List<Studententity> students = studentRepository.findAll();

        // Find username
        for (Studententity student : students) {

            // Check username safely
            if (student.getUsername() != null &&
                student.getUsername().equals(loginDetails.getUsername())) {

                // Username found
                if (student.getPassword() == null ||
                    !student.getPassword().equals(loginDetails.getPassword())) {

                    return ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED)
                            .body("Invalid Password");
                }

                // Username and password correct
                return ResponseEntity
                        .ok("Login Successful. Welcome " + student.getName());
            }
        }

        // Username was not found
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Username not found");
    }
}