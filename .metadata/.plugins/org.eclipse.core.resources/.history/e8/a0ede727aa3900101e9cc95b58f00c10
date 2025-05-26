// File: SVBK/model/Student.java
package SVBK.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Lớp trừu tượng đại diện cho sinh viên chung (Credit-based hoặc Part-time).
 * Thông tin ngành được lưu trong {@link Program}, không lặp lại ở đây.
 */
public abstract class Student {
    /**
     * Mã sinh viên duy nhất (ví dụ: "SV001").
     */
    private String studentID;

    /**
     * Họ và tên sinh viên (ví dụ: "Nguyễn Văn A").
     */
    private String studentName;

    /**
     * Loại sinh viên: "Credit-based" hoặc "Part-time".
     */
    private String studentType;

    /**
     * Danh sách mã học phần đã hoàn thành, dùng để kiểm tra prerequisite và tính tín chỉ.
     */
    private List<String> completedCourseIDs;

    /**
     * Constructor mặc định, khởi tạo danh sách completedCourseIDs.
     */
    public Student() {
        this.completedCourseIDs = new ArrayList<>();
    }

    /**
     * Constructor đầy đủ tham số.
     *
     * @param studentID   Mã sinh viên
     * @param studentName Họ và tên sinh viên
     * @param studentType Loại sinh viên
     */
    public Student(String studentID, String studentName, String studentType) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentType = studentType;
        this.completedCourseIDs = new ArrayList<>();
    }

    // ===== Getter / Setter =====

    /** @return Mã sinh viên */
    public String getStudentID() {
        return studentID;
    }

    /** @param studentID Mã sinh viên */
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    /** @return Họ và tên sinh viên */
    public String getStudentName() {
        return studentName;
    }

    /** @param studentName Họ và tên sinh viên */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /** @return Loại sinh viên */
    public String getStudentType() {
        return studentType;
    }

    /** @param studentType Loại sinh viên */
    public void setStudentType(String studentType) {
        this.studentType = studentType;
    }

    /** @return Danh sách mã học phần đã hoàn thành */
    public List<String> getCompletedCourseIDs() {
        return completedCourseIDs;
    }

    /** @param completedCourseIDs Danh sách mã học phần đã hoàn thành */
    public void setCompletedCourseIDs(List<String> completedCourseIDs) {
        this.completedCourseIDs = completedCourseIDs;
    }

    // ===== Quản lý completedCourseIDs =====

    /**
     * Thêm mã học phần vào danh sách nếu chưa tồn tại.
     *
     * @param courseID Mã học phần
     */
    public void addCompletedCourseID(String courseID) {
        if (courseID != null && !courseID.trim().isEmpty() &&
            !completedCourseIDs.contains(courseID)) {
            completedCourseIDs.add(courseID);
        }
    }

    /**
     * Loại bỏ mã học phần khỏi danh sách.
     *
     * @param courseID Mã học phần
     */
    public void removeCompletedCourseID(String courseID) {
        completedCourseIDs.remove(courseID);
    }

    // ===== Phương thức trừu tượng =====

    /**
     * Tính điểm trung bình (CPA) của sinh viên.
     * - Với Part-time: thang 10
     * - Với Credit-based: quy đổi về thang 4
     *
     * @return Điểm CPA theo hệ tương ứng
     */
    public abstract double calculateFinalGrade();

    /**
     * Kiểm tra điều kiện tốt nghiệp của sinh viên.
     * Logic chi tiết do các lớp con triển khai.
     *
     * @return true nếu đủ điều kiện; false nếu chưa
     */
    public abstract boolean checkGraduation();

    @Override
    public String toString() {
        return "Student{" +
               "studentID='" + studentID + '\'' +
               ", studentName='" + studentName + '\'' +
               ", studentType='" + studentType + '\'' +
               ", completedCourseIDs=" + completedCourseIDs +
               '}';
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