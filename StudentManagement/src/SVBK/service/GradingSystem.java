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
     * Chỉ nhập cho các học phần đã đăng ký (Enrollment chưa hoàn thành).
     *
     * @param student    Đối tượng Student
     * @param course     Đối tượng Course tương ứng
     * @param midterm    Điểm giữa kỳ (0.0–10.0)
     * @param finalScore Điểm cuối kỳ (0.0–10.0)
     * @return true nếu điểm hợp lệ và nhập thành công; false nếu không phù hợp hoặc không có Enrollment
     */
    public boolean enterGrade(Student student, Course course, double midterm, double finalScore) {
        if (student == null || course == null) return false;
        // Kiểm tra đầu vào chung trên thang 10
        if (midterm < 0.0 || midterm > 10.0 || finalScore < 0.0 || finalScore > 10.0) {
            return false;
        }
        // Delegate nhập điểm vào Enrollment của Student
        return student.enterGrade(course.getCourseID(), midterm, finalScore);
    }

    /**
     * Tính CPA (delegate cho Student.calculateFinalGrade()).
     *
     * @param student Đối tượng Student
     * @return CPA tương ứng (thang 4 hoặc 10)
     */
    public double calculateCPA(Student student) {
        if (student == null) return 0.0;
        return student.calculateFinalGrade();
    }
}
