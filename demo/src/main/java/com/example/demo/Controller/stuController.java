package com.example.demo.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Studententity;
import com.example.demo.repository.StudentRepository;

@RestController

@RequestMapping("/students")
public class stuController {

    @Autowired
    private StudentRepository studentRepository;

    // 1. Get all students
    @GetMapping
    public List<Studententity> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/hello")
    public String hello(){
        return "welcome to spring class";
    }

    // 2. Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Studententity> getStudentById(@PathVariable int id) {
        Optional<Studententity> student = studentRepository.findById(id);
        return student.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. Create a new student
    @PostMapping
    public ResponseEntity<Studententity> createStudent(@RequestBody Studententity student) {
        Studententity savedStudent = studentRepository.save(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    // 4. Update an existing student
@PutMapping("/{id}")
public Studententity updateStudent(
        @PathVariable int id,
        @RequestBody Studententity studentDetails) {

    Studententity student = studentRepository.findById(id).orElse(null);

    if (student == null) {
        return null;
    }

    student.setName(studentDetails.getName());
    student.setDepartment(studentDetails.getDepartment());
    student.setAge(studentDetails.getAge());

    return studentRepository.save(student);
}

    // 5. Delete a student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}