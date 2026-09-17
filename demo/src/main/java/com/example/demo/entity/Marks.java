package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Marks {

    @Id
    private int markId;

    private int studentId;
    private int courseId;
    private String examName;
    private double marks;
    private double totalMarks;

    public Marks() {
    }

    public Marks(int markId, int studentId, int courseId,
                 String examName, double marks, double totalMarks) {
        this.markId = markId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.examName = examName;
        this.marks = marks;
        this.totalMarks = totalMarks;
    }

    public int getMarkId() {
        return markId;
    }

    public void setMarkId(int markId) {
        this.markId = markId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(double totalMarks) {
        this.totalMarks = totalMarks;
    }
}