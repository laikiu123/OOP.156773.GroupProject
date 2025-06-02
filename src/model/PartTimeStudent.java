// File: SVBK/model/PartTimeStudent.java
package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Sinh viên hệ niên chế (Part-time), tính CPA thang 10.
 * Sử dụng enrollments từ lớp cha để quản lý đăng ký môn và điểm.
 */
public class PartTimeStudent extends Student {
    /** Chương trình đào tạo gồm các học phần cố định. */
    private Program program;

    /**
     * Constructor khởi tạo sinh viên hệ niên chế.
     *
     * @param studentID   Mã sinh viên
     * @param studentName Tên sinh viên
     * @param program     Chương trình đào tạo
     */
    public PartTimeStudent(String studentID, String studentName, Program program) {
        super(studentID, studentName, "Part-time");
        this.program = program;
    }

    // ===== Getter / Setter =====
    public Program getProgram() { return program; }
    public void setProgram(Program program) { this.program = program; }

    // ===== Business Methods =====

    /**
     * Đăng ký học phần giống CreditBasedStudent dành cho admin sử dụng.
     * Chỉ cho phép các học phần thuộc program.
     *
     * @param course Học phần cần đăng ký
     * @return true nếu đăng ký thành công; false nếu không thỏa
     */
    public boolean registerCourse(Course course) {
        if (course == null) return false;
        boolean inProgram = program.getRequiredCourses().stream()
            .anyMatch(c -> c.getCourseID().equalsIgnoreCase(course.getCourseID()));
        if (!inProgram) return false;
        return enrollCourse(course);
    }

    /**
     * Tính điểm trung bình (CPA) theo thang 10 dựa trên các Enrollment đã nhập điểm
     * cho những Course nằm trong requiredCourses. Bao gồm cả môn chưa đạt (điểm < 4).
     *
     * @return CPA thang 10
     */
    @Override
    public double calculateFinalGrade() {
        List<Course> required = program.getRequiredCourses();
        // Lấy tất cả Enrollment đã nhập đủ điểm
        List<Enrollment> graded = getGradedEnrollments();
        double sum = 0;
        int count = 0;
        for (Course c : required) {
            for (Enrollment e : graded) {
                if (e.getCourse().getCourseID().equalsIgnoreCase(c.getCourseID())) {
                    sum += e.calculateFinalGrade();
                    count++;
                    break;
                }
            }
        }
        return count == 0 ? 0.0 : sum / count;
    }

    /**
     * Kiểm tra điều kiện tốt nghiệp:
     * - Hoàn thành tất cả requiredCourses
     * - CPA thang 10 >= 5.0
     *
     * @return true nếu đủ, false nếu chưa
     */
    @Override
    public boolean checkGraduation() {
        // 1) required
        for (Course c : program.getRequiredCourses()) {
            boolean done = getCompletedEnrollments().stream()
                .anyMatch(e -> e.getCourse().getCourseID().equalsIgnoreCase(c.getCourseID()));
            if (!done) return false;
        }
        // 2) CPA thang 10
        return calculateFinalGrade() >= 5.0;
    }
}