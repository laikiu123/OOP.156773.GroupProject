// File: SVBK/service/GradingSystem.java
package SVBK.service;

import SVBK.model.Student;
import SVBK.model.Course;

/**
 * Nhập điểm và tính CPA cho sinh viên.
 * Điểm nhập luôn theo thang 10 cho cả Credit-based và Part-time.
 */
public class GradingSystem {
    /**
     * Nhập điểm giữa kỳ và cuối kỳ (thang 10) cho sinh viên.
     *
     * @return true nếu điểm hợp lệ (0.0–10.0) và nhập thành công
     */
    public boolean enterGrade(Student student, Course course, double midterm, double finalScore) {
        if (student == null || course == null) return false;
        // Kiểm tra đầu vào chung trên thang 10
        if (midterm < 0.0 || midterm > 10.0 || finalScore < 0.0 || finalScore > 10.0) {
            return false;
        }
        course.setMidtermScore(midterm);
        course.setFinalScore(finalScore);
        return true;
    }

    /**
     * Tính CPA (delegation cho Student.calculateFinalGrade()).
     */
    public double calculateCPA(Student student) {
        if (student == null) return 0.0;
        return student.calculateFinalGrade();
    }
}
