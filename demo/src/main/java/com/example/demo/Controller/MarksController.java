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

import com.example.demo.entity.Course;
import com.example.demo.entity.Marks;
import com.example.demo.entity.Studententity;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.MarksRepository;
import com.example.demo.repository.StudentRepository;

@RestController
@RequestMapping("/marks")
public class MarksController {

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    // GET all marks
    @GetMapping
    public List<Marks> getAllMarks() {
        return marksRepository.findAll();
    }

    // GET marks by ID
    @GetMapping("/{id}")
    public ResponseEntity<Marks> getMarksById(@PathVariable int id) {

        Optional<Marks> marks = marksRepository.findById(id);

        if (marks.isPresent()) {
            return ResponseEntity.ok(marks.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Create marks
    @PostMapping
    public ResponseEntity<?> createMarks(@RequestBody Marks marks) {

        // Check whether student exists
        Optional<Studententity> student =
                studentRepository.findById(marks.getStudentId());

        if (student.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Student not found");
        }

        // Check whether course exists
        Optional<Course> course =
                courseRepository.findById(marks.getCourseId());

        if (course.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Course not found");
        }

        // Check whether marks are negative
        if (marks.getMarks() < 0) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Marks cannot be negative");
        }

        // Check whether marks are greater than total marks
        if (marks.getMarks() > marks.getTotalMarks()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Marks cannot be greater than total marks");
        }

        Marks savedMarks = marksRepository.save(marks);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedMarks);
    }

    // PUT - Update marks
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMarks(
            @PathVariable int id,
            @RequestBody Marks marksDetails) {

        Optional<Marks> optionalMarks =
                marksRepository.findById(id);

        if (optionalMarks.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Check student exists
        Optional<Studententity> student =
                studentRepository.findById(marksDetails.getStudentId());

        if (student.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Student not found");
        }

        // Check course exists
        Optional<Course> course =
                courseRepository.findById(marksDetails.getCourseId());

        if (course.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Course not found");
        }

        // Check negative marks
        if (marksDetails.getMarks() < 0) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Marks cannot be negative");
        }

        // Check marks greater than total marks
        if (marksDetails.getMarks() > marksDetails.getTotalMarks()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Marks cannot be greater than total marks");
        }

        Marks marks = optionalMarks.get();

        marks.setStudentId(marksDetails.getStudentId());
        marks.setCourseId(marksDetails.getCourseId());
        marks.setExamName(marksDetails.getExamName());
        marks.setMarks(marksDetails.getMarks());
        marks.setTotalMarks(marksDetails.getTotalMarks());

        Marks updatedMarks = marksRepository.save(marks);

        return ResponseEntity.ok(updatedMarks);
    }

    // DELETE marks
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarks(@PathVariable int id) {

        if (marksRepository.existsById(id)) {

            marksRepository.deleteById(id);

            return ResponseEntity.noContent().build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }
}