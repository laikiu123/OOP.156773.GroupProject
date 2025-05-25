// File: SVBK/model/PartTimeStudent.java
package SVBK.model;

import java.util.List;

/**
 * Sinh viên hệ niên chế (Part-time), tính CPA thang 10.
 */
public class PartTimeStudent extends Student {
    /** Chương trình đào tạo gồm các học phần cố định. */
    private Program program;

    /**
     * Constructor khởi tạo sinh viên hệ niên chế.
     */
    public PartTimeStudent(String studentID, String studentName, Program program) {
        super(studentID, studentName, "Part-time");
        this.program = program;
    }

    // ===== Getter / Setter =====
    public Program getProgram() { return program; }
    public void setProgram(Program program) { this.program = program; }

    /**
     * Tính điểm trung bình dựa trên thang 10 của các requiredCourses.
     * @return CPA thang 10
     */
    @Override
    public double calculateFinalGrade() {
        List<Course> list = program.getRequiredCourses();
        if (list.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (Course c : list) {
            sum += c.calculateFinalGrade();
        }
        return sum / list.size();
    }

    /**
     * Kiểm tra tốt nghiệp:
     * - Hoàn thành tất cả requiredCourses
     * - CPA thang 10 >= 5.0
     *
     * @return true nếu đủ, false nếu chưa
     */
    @Override
    public boolean checkGraduation() {
        for (Course c : program.getRequiredCourses()) {
            if (!getCompletedCourseIDs().contains(c.getCourseID())) {
                return false;
            }
        }
        return calculateFinalGrade() >= 5.0;
    }
}