// File: SVBK/model/Enrollment.java
package SVBK.model;

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

    /** @return Course đã đăng ký */
    public Course getCourse() {
        return course;
    }

    /** @param course thiết lập Course đã đăng ký */
    public void setCourse(Course course) {
        this.course = course;
    }

    /** @return Điểm giữa kỳ (có thể null nếu chưa nhập) */
    public Double getMidtermScore() {
        return midtermScore;
    }

    /** @param midtermScore thiết lập điểm giữa kỳ */
    public void setMidtermScore(Double midtermScore) {
        this.midtermScore = midtermScore;
    }

    /** @return Điểm cuối kỳ (có thể null nếu chưa nhập) */
    public Double getFinalScore() {
        return finalScore;
    }

    /** @param finalScore thiết lập điểm cuối kỳ */
    public void setFinalScore(Double finalScore) {
        this.finalScore = finalScore;
    }

    // ===== Business Methods =====

    /**
     * Kiểm tra xem học phần đã hoàn thành (đã nhập đủ hai điểm) chưa.
     * @return true nếu midtermScore và finalScore đều khác null
     */
    public boolean isCompleted() {
        return midtermScore != null && finalScore != null;
    }

    /**
     * Tính điểm tổng kết môn (sử dụng phương thức của Course).
     * Nếu chưa hoàn thành, trả về 0.0.
     * @return điểm tổng kết môn
     */
    public double calculateFinalGrade() {
        if (!isCompleted()) {
            return 0.0;
        }
        return course.calculateFinalGrade();
    }

    @Override
    public String toString() {
        return String.format("%s - %s | GK: %s | CK: %s | KQ: %.2f",
            course.getCourseID(), course.getCourseName(),
            midtermScore == null ? "--" : midtermScore,
            finalScore  == null ? "--" : finalScore,
            isCompleted() ? calculateFinalGrade() : 0.0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Enrollment)) return false;
        Enrollment other = (Enrollment) obj;
        return course != null && course.getCourseID().equalsIgnoreCase(other.getCourse().getCourseID());
    }

    @Override
    public int hashCode() {
        return course == null ? 0 : course.getCourseID().toLowerCase().hashCode();
    }
}
