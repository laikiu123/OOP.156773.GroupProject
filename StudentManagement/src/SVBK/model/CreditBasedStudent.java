// File: SVBK/model/CreditBasedStudent.java
package SVBK.model;

import java.util.List;

/**
 * Sinh viên hệ tín chỉ.
 * Điểm học phần tính theo thang 10, sau đó quy đổi từng học phần sang thang 4
 * rồi tính CPA (trung bình có trọng số theo tín chỉ) trên thang 4.
 */
public class CreditBasedStudent extends Student {
    /** Chương trình đào tạo kèm điều kiện tín chỉ. */
    private Program program;

    /**
     * Constructor khởi tạo sinh viên hệ tín chỉ.
     *
     * @param studentID   Mã sinh viên
     * @param studentName Tên sinh viên
     * @param program     Chương trình đào tạo
     */
    public CreditBasedStudent(String studentID, String studentName, Program program) {
        super(studentID, studentName, "Credit-based");
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
     * Kiểm tra điều kiện prerequisite trước khi đăng ký.
     *
     * @param course Học phần cần kiểm tra
     * @return true nếu không có prerequisite hoặc đã hoàn thành prerequisite
     */
    public boolean checkPrerequisites(Course course) {
        String preID = course.getPreCourseID();
        if (preID == null || preID.trim().isEmpty()) {
            return true;
        }
        // Đã hoàn thành mô đun prerequisite
        return getCompletedEnrollments().stream()
            .anyMatch(e -> e.getCourse().getCourseID().equalsIgnoreCase(preID));
    }

    /**
     * Đăng ký học phần (Enrollment) nếu đủ điều kiện và có trong chương trình.
     *
     * @param course Học phần cần đăng ký
     * @return true nếu thêm mới; false nếu khóa không thuộc chương trình
     *         hoặc chưa thỏa prerequisite hoặc đã đăng ký trước đó
     */
    public boolean registerCourse(Course course) {
        if (course == null) {
            return false;
        }
        // Kiểm tra khóa học có trong chương trình này (bắt buộc hoặc tự chọn)
        if (!program.containsCourse(course.getCourseID())) {
            return false;
        }
        // Kiểm tra điều kiện tiên quyết (nếu có)
        if (!checkPrerequisites(course)) {
            return false;
        }
        // Delegate cho lớp cha để tạo Enrollment nếu chưa có
        return enrollCourse(course);
    }

    /**
     * Tính CPA: quy đổi từng học phần đã hoàn thành sang thang 4 rồi tính trung bình có trọng số.
     *
     * @return CPA thang 4
     */
    @Override
    public double calculateFinalGrade() {
        List<Enrollment> completed = getCompletedEnrollments();
        double totalPoints = 0;
        int totalCredits = 0;
        for (Enrollment e : completed) {
            // Lấy điểm tổng kết môn từ Enrollment (đã tính theo finalWeight)
            double grade10 = e.calculateFinalGrade();
            // Quy đổi grade sang thang 4
            double grade4;
            if (grade10 >= 8.5)      grade4 = 4.0;
            else if (grade10 >= 7.0) grade4 = 3.0;
            else if (grade10 >= 5.5) grade4 = 2.0;
            else if (grade10 >= 4.0) grade4 = 1.0;
            else                     grade4 = 0.0;

            totalPoints += grade4 * e.getCourse().getCreditCount();
            totalCredits += e.getCourse().getCreditCount();
        }
        return totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
    }

    /**
     * Kiểm tra điều kiện tốt nghiệp:
     * 1) Hoàn thành tất cả requiredCourses
     * 2) Tích lũy đủ tín chỉ tự chọn
     * 3) Tích lũy đủ tổng tín chỉ chương trình
     *
     * @return true nếu thỏa
     */
    @Override
    public boolean checkGraduation() {
        // 1) required
        for (Course rc : program.getRequiredCourses()) {
            boolean done = getCompletedEnrollments().stream()
                .anyMatch(e -> e.getCourse().getCourseID()
                    .equalsIgnoreCase(rc.getCourseID()));
            if (!done) {
                return false;
            }
        }
        // 2) elective credits
        int electiveCredits = getCompletedEnrollments().stream()
            .filter(e -> program.isElectiveCourse(e.getCourse().getCourseID()))
            .mapToInt(e -> e.getCourse().getCreditCount())
            .sum();
        if (electiveCredits < program.getElectiveCreditRequirement()) {
            return false;
        }
        // 3) total credits
        int totalCredits = getCompletedEnrollments().stream()
            .mapToInt(e -> e.getCourse().getCreditCount())
            .sum();
        return totalCredits >= program.getTotalCreditRequirement();
    }
}
