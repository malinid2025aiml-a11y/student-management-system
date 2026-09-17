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
import com.example.demo.entity.Enrollment;
import com.example.demo.entity.Studententity;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.StudentRepository;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    // GET all enrollments
    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    // GET enrollment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable int id) {

        Optional<Enrollment> enrollment =
                enrollmentRepository.findById(id);

        if (enrollment.isPresent()) {
            return ResponseEntity.ok(enrollment.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Create enrollment
    @PostMapping
    public ResponseEntity<?> createEnrollment(
            @RequestBody Enrollment enrollment) {

        // Check student exists
        Optional<Studententity> student =
                studentRepository.findById(enrollment.getStudentId());

        if (student.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid student ID");
        }

        // Check course exists
        Optional<Course> course =
                courseRepository.findById(enrollment.getCourseId());

        if (course.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid course ID");
        }

        // Check duplicate enrollment
        if (enrollmentRepository.existsByStudentIdAndCourseId(
                enrollment.getStudentId(),
                enrollment.getCourseId())) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Duplicate enrollment");
        }

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedEnrollment);
    }

    // PUT - Update enrollment
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEnrollment(
            @PathVariable int id,
            @RequestBody Enrollment enrollmentDetails) {

        Optional<Enrollment> optionalEnrollment =
                enrollmentRepository.findById(id);

        if (optionalEnrollment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Enrollment enrollment = optionalEnrollment.get();

        enrollment.setStudentId(enrollmentDetails.getStudentId());
        enrollment.setCourseId(enrollmentDetails.getCourseId());
        enrollment.setStatus(enrollmentDetails.getStatus());

        Enrollment updatedEnrollment =
                enrollmentRepository.save(enrollment);

        return ResponseEntity.ok(updatedEnrollment);
    }

    // DELETE enrollment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(
            @PathVariable int id) {

        if (enrollmentRepository.existsById(id)) {

            enrollmentRepository.deleteById(id);

            return ResponseEntity.noContent().build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
