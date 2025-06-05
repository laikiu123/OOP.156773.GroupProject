// File: SVBK/model/Student.java
package SVBK.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Lớp trừu tượng đại diện cho sinh viên chung (Credit-based hoặc Part-time).
 * Thông tin ngành được lưu trong {@link Program}, không lặp lại ở đây.
 * Quản lý danh sách Enrollment chứa các học phần đang học và đã hoàn thành.
 */
public abstract class Student {
    /** Mã sinh viên duy nhất (ví dụ: "SV001"). */
    private String studentID;
    /** Họ và tên sinh viên (ví dụ: "Nguyễn Văn A"). */
    private String studentName;
    /** Loại sinh viên: "Credit-based" hoặc "Part-time". */
    private String studentType;
    /** Danh sách Enrollment: các học phần đăng ký, lưu điểm GK, CK. */
    private List<Enrollment> enrollments;

    /**
     * Constructor mặc định, khởi tạo danh sách enrollments.
     */
    public Student() {
        this.enrollments = new ArrayList<>();
    }

    /**
     * Constructor đầy đủ tham số.
     *
     * @param studentID   Mã sinh viên
     * @param studentName Họ và tên sinh viên
     * @param studentType Loại sinh viên
     */
    public Student(String studentID, String studentName, String studentType) {
        this.studentID   = studentID;
        this.studentName = studentName;
        this.studentType = studentType;
        this.enrollments = new ArrayList<>();
    }

    // ===== Getter / Setter =====

    public String getStudentID() {
        return studentID;
    }
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentType() {
        return studentType;
    }
    public void setStudentType(String studentType) {
        this.studentType = studentType;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }
    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    // ===== Enrollment management =====

    /**
     * Đăng ký học phần nếu:
     *  1) Course != null
     *  2) Chưa đăng ký/chưa hoàn thành trước đó
     *  3) Nếu course có prerequisite thì student đã hoàn thành khóa prerequisite
     *
     * @param course Học phần cần đăng ký
     * @return true nếu đăng ký thành công; false nếu không hợp lệ
     */
    public boolean enrollCourse(Course course) {
        if (course == null) return false;

        for (Enrollment e : enrollments) {
            if (e.getCourse().getCourseID().equalsIgnoreCase(course.getCourseID())) {
                return false;
            }
        }

        if (course.hasPrerequisite()) {
            String preID = course.getPreCourseID().trim();
            boolean passed = false;
            for (Enrollment e : getCompletedEnrollments()) {
                if (e.getCourse().getCourseID().equalsIgnoreCase(preID)) {
                    passed = true;
                    break;
                }
            }
            if (!passed) return false;
        }

        enrollments.add(new Enrollment(course));
        return true;
    }

    /**
     * Nhập điểm cho một học phần đã đăng ký.
     *
     * @param courseID   Mã học phần
     * @param midterm    Điểm giữa kỳ
     * @param finalScore Điểm cuối kỳ
     * @return true nếu cập nhật thành công; false nếu không tìm thấy Enrollment
     */
    public boolean enterGrade(String courseID, double midterm, double finalScore) {
        for (Enrollment e : enrollments) {
            if (e.getCourse().getCourseID().equalsIgnoreCase(courseID)) {
                e.setMidtermScore(midterm);
                e.setFinalScore(finalScore);
                return true;
            }
        }
        return false;
    }

    /**
     * Lấy danh sách môn đã hoàn thành (đã đạt: điểm tổng kết >= 4.0).
     *
     * @return List of Enrollment đã pass
     */
    public List<Enrollment> getCompletedEnrollments() {
        List<Enrollment> passed = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.isPassed()) {
                passed.add(e);
            }
        }
        return passed;
    }

    /**
     * Lấy danh sách môn đang học (chưa pass):
     * - Không nhập đủ điểm, hoặc đã nhập nhưng điểm < 4.0.
     *
     * @return List of Enrollment chưa pass
     */
    public List<Enrollment> getInProgressEnrollments() {
        List<Enrollment> inProgress = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (!e.isPassed()) {
                inProgress.add(e);
            }
        }
        return inProgress;
    }

    // ===== Abstract methods =====

    public abstract double calculateFinalGrade();

    public abstract boolean checkGraduation();

    @Override
    public String toString() {
        return String.format("%s - %s [%s]\n  Đang học: %s\n  Đã hoàn thành: %s",
            studentID, studentName, studentType,
            getInProgressEnrollments(), getCompletedEnrollments());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student s = (Student) o;
        return Objects.equals(studentID, s.studentID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentID);
    }
}
