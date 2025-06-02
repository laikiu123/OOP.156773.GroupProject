// File: SVBK/model/Enrollment.java
package model;

/**
 * Đại diện một bản ghi đăng ký học phần và điểm số của sinh viên.
 * Mỗi Enrollment gắn liền với một Course, lưu trữ điểm giữa kỳ và cuối kỳ.
 */
public class Enrollment {
    /** Học phần được đăng ký */
    private Course course;
    /** Điểm giữa kỳ; null nếu chưa nhập */
    private Double midtermScore;
    /** Điểm cuối kỳ; null nếu chưa nhập */
    private Double finalScore;

    /**
     * Khởi tạo Enrollment cho một Course, chưa có điểm.
     * @param course khóa học được đăng ký
     */
    public Enrollment(Course course) {
        this.course = course;
        this.midtermScore = null;
        this.finalScore = null;
    }

    // ===== Getter / Setter =====
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Double getMidtermScore() { return midtermScore; }
    public void setMidtermScore(Double midtermScore) { this.midtermScore = midtermScore; }

    public Double getFinalScore() { return finalScore; }
    public void setFinalScore(Double finalScore) { this.finalScore = finalScore; }

    // ===== Business Methods =====

    /**
     * Kiểm tra xem hai điểm đã được nhập hay chưa.
     * @return true nếu midtermScore và finalScore đều khác null
     */
    public boolean isCompleted() {
        return midtermScore != null && finalScore != null;
    }

    /**
     * Kiểm tra xem học phần đã đạt hay chưa (điểm tổng kết >= 4.0).
     * Phải đã nhập đủ điểm và calculateFinalGrade() >= 4.0.
     * @return true nếu đạt, false nếu chưa hoặc chưa hoàn thành điểm
     */
    public boolean isPassed() {
        return isCompleted() && calculateFinalGrade() >= 4.0;
    }

    /**
     * Tính điểm tổng kết môn dựa trên điểm Enrollment và trọng số của Course.
     * grade = midterm * (1 - finalWeight) + final * finalWeight.
     * Nếu chưa hoàn thành, trả về 0.0.
     *
     * @return điểm tổng kết môn
     */
    public double calculateFinalGrade() {
        if (!isCompleted()) {
            return 0.0;
        }
        double weight = course.getFinalWeight();
        return midtermScore * (1.0 - weight)
             + finalScore   * weight;
    }

    @Override
    public String toString() {
        return String.format(
            "%s - %s | GK: %s | CK: %s | KQ: %.2f",
            course.getCourseID(), course.getCourseName(),
            midtermScore == null ? "--" : midtermScore.toString(),
            finalScore  == null ? "--" : finalScore.toString(),
            calculateFinalGrade()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Enrollment)) return false;
        Enrollment other = (Enrollment) obj;
        return course != null
            && course.getCourseID().equalsIgnoreCase(other.getCourse().getCourseID());
    }

    @Override
    public int hashCode() {
        return course == null
            ? 0
            : course.getCourseID().toLowerCase().hashCode();
    }
}