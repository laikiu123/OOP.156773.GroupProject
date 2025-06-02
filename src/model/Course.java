// File: SVBK/model/Course.java
package model;

import java.util.Objects;

/**
 * Đại diện cho một học phần trong hệ thống quản lý sinh viên.
 */
public class Course {
    /** Mã học phần, duy nhất trong hệ thống (ví dụ: "CS101"). */
    private String courseID;

    /** Tên học phần (ví dụ: "Nhập môn Lập trình"). */
    private String courseName;

    /** Số tín chỉ của học phần (ví dụ: 3, 4, 2, ...). */
    private int creditCount;

    /** Mã học phần tiên quyết (prerequisite). Nếu không có thì để null hoặc chuỗi rỗng. */
    private String preCourseID;

    /** Điểm giữa kỳ (midterm). */
    private Double midtermScore;

    /** Điểm cuối kỳ (final). */
    private Double finalScore;

    /**
     * Trọng số điểm cuối kỳ, dùng để tính điểm tổng kết:
     * ví dụ 0.6 nghĩa là 60% weight cho final, và 1 - finalWeight cho midterm.
     * Giá trị phải nằm trong khoảng [0.0, 1.0].
     */
    private double finalWeight;

    /**
     * Constructor mặc định, khởi tạo Enrollment hoặc khi cần tạo placeholder.
     */
    public Course() {
        // Các trường numeric mặc định là 0, và finalWeight không hợp lệ phải được set sau.
    }

    /**
     * Constructor đầy đủ tham số.
     *
     * @param courseID     Mã học phần
     * @param courseName   Tên học phần
     * @param creditCount  Số tín chỉ
     * @param preCourseID  Mã học phần tiên quyết (có thể null hoặc "")
     * @param finalWeight  Trọng số điểm cuối kỳ (phải trong [0.0, 1.0])
     * @throws IllegalArgumentException nếu finalWeight không hợp lệ
     */
    public Course(String courseID, String courseName, int creditCount,
                  String preCourseID, double finalWeight) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.creditCount = creditCount;
        this.preCourseID = preCourseID;
        // Sử dụng setter để validate finalWeight
        setFinalWeight(finalWeight);
        // midtermScore và finalScore khởi tạo là null (chưa nhập điểm)
        this.midtermScore = null;
        this.finalScore = null;
    }

    // ===== Getter và Setter =====

    public String getCourseID() {
        return courseID;
    }
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCreditCount() {
        return creditCount;
    }
    public void setCreditCount(int creditCount) {
        this.creditCount = creditCount;
    }

    public String getPreCourseID() {
        return preCourseID;
    }
    public void setPreCourseID(String preCourseID) {
        this.preCourseID = preCourseID;
    }

    public Double getMidtermScore() {
        return midtermScore;
    }
    public void setMidtermScore(Double midtermScore) {
        this.midtermScore = midtermScore;
    }

    public Double getFinalScore() {
        return finalScore;
    }
    public void setFinalScore(Double finalScore) {
        this.finalScore = finalScore;
    }

    public double getFinalWeight() {
        return finalWeight;
    }
    /**
     * Thiết lập trọng số điểm cuối kỳ. Giá trị phải trong [0.0, 1.0].
     *
     * @param finalWeight giá trị trọng số
     * @throws IllegalArgumentException nếu finalWeight không thuộc [0.0, 1.0]
     */
    public void setFinalWeight(double finalWeight) {
        if (finalWeight < 0.0 || finalWeight > 1.0) {
            throw new IllegalArgumentException(
                "finalWeight must be between 0.0 and 1.0");
        }
        this.finalWeight = finalWeight;
    }

    // ===== Phương thức tiện ích =====

    /**
     * Tính điểm tổng kết (final grade) theo công thức:
     * grade = midtermScore * (1 - finalWeight) + finalScore * finalWeight
     *
     * @return Điểm tổng kết; 0.0 nếu chưa nhập đủ điểm
     */
    public double calculateFinalGrade() {
        if (midtermScore == null || finalScore == null) {
            return 0.0;
        }
        return midtermScore * (1.0 - finalWeight)
             + finalScore    * finalWeight;
    }

    /**
     * Kiểm tra xem học phần có prerequisite hay không.
     *
     * @return true nếu đã có học phần tiên quyết, false nếu không
     */
    public boolean hasPrerequisite() {
        return preCourseID != null && !preCourseID.trim().isEmpty();
    }

    @Override
    public String toString() {
        return String.format(
            "%s - %s | Tín chỉ: %d | Tiên quyết: %s | Trọng số CK: %.2f",
            courseID, courseName,
            creditCount,
            (preCourseID == null || preCourseID.isEmpty() ? "Không" : preCourseID),
            finalWeight
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseID, course.courseID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseID);
    }
}