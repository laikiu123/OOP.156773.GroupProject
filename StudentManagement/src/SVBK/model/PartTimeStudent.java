// File: SVBK/model/PartTimeStudent.java
package SVBK.model;

import java.util.List;
import SVBK.model.Course;
import SVBK.model.Enrollment;
import SVBK.model.Program;

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

    /** @return Chương trình đào tạo */
    public Program getProgram() {
        return program;
    }

    /** @param program Thiết lập chương trình đào tạo */
    public void setProgram(Program program) {
        this.program = program;
    }

    // ===== Business Methods =====

    /**
     * Đăng ký học phần (Enrollment) cho sinh viên niên chế.
     * Chỉ cho phép các học phần bắt buộc thuộc chương trình.
     *
     * @param course Học phần cần đăng ký
     * @return true nếu đăng ký thành công; false nếu course null,
     *         không thuộc requiredCourses hoặc đã đăng ký trước đó.
     */
    public boolean registerCourse(Course course) {
        if (course == null) {
            return false;
        }
        // Chỉ cho phép đăng ký khóa học bắt buộc trong program
        if (!program.isRequiredCourse(course.getCourseID())) {
            return false;
        }
        // Delegate cho lớp cha để tạo Enrollment (kiểm tra prerequisite và trùng lặp)
        return enrollCourse(course);
    }

    /**
     * Tính điểm trung bình (CPA) theo thang 10 dựa trên các Enrollment đã hoàn thành
     * của những Course nằm trong requiredCourses.
     *
     * @return CPA thang 10 hoặc 0.0 nếu chưa có kết quả
     */
    @Override
    public double calculateFinalGrade() {
        List<Course> required = program.getRequiredCourses();
        List<Enrollment> completed = getCompletedEnrollments();
        double sum = 0;
        int count = 0;
        for (Course c : required) {
            for (Enrollment e : completed) {
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
     * @return true nếu đủ điều kiện; false nếu chưa
     */
    @Override
    public boolean checkGraduation() {
        // 1) Hoàn thành tất cả requiredCourses
        for (Course c : program.getRequiredCourses()) {
            boolean done = getCompletedEnrollments().stream()
                .anyMatch(e -> e.getCourse().getCourseID()
                    .equalsIgnoreCase(c.getCourseID()));
            if (!done) {
                return false;
            }
        }
        // 2) Đảm bảo CPA >= 5.0
        return calculateFinalGrade() >= 5.0;
    }
}
