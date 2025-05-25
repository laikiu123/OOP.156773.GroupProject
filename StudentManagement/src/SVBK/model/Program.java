// File: SVBK/model/Program.java
package SVBK.model;

import java.util.List;

/**
 * Đại diện cho chương trình đào tạo của một ngành học.
 * Chứa học phần bắt buộc, tự chọn và các ngưỡng tín chỉ.
 */
public class Program {
    /** Tên ngành (ví dụ: "Computer Science"). */
    private String majorName;

    /** Danh sách học phần bắt buộc. */
    private List<Course> requiredCourses;

    /** Danh sách học phần tự chọn. */
    private List<Course> electiveCourses;

    /** Ngưỡng tín chỉ tối thiểu phải tích lũy từ electiveCourses. */
    private int electiveCreditRequirement;

    /** Tổng tín chỉ yêu cầu tốt nghiệp (bắt buộc + tự chọn). */
    private int totalCreditRequirement;

    /**
     * Constructor đầy đủ tham số.
     *
     * @param majorName                Tên ngành
     * @param requiredCourses          Danh sách học phần bắt buộc
     * @param electiveCourses          Danh sách học phần tự chọn
     * @param electiveCreditRequirement Ngưỡng tín chỉ tự chọn
     * @param totalCreditRequirement   Tổng tín chỉ yêu cầu
     */
    public Program(String majorName,
                   List<Course> requiredCourses,
                   List<Course> electiveCourses,
                   int electiveCreditRequirement,
                   int totalCreditRequirement) {
        this.majorName = majorName;
        this.requiredCourses = requiredCourses;
        this.electiveCourses = electiveCourses;
        this.electiveCreditRequirement = electiveCreditRequirement;
        this.totalCreditRequirement = totalCreditRequirement;
    }

    // ===== Getter / Setter =====
    public String getMajorName() { return majorName; }
    public void setMajorName(String majorName) { this.majorName = majorName; }

    public List<Course> getRequiredCourses() { return requiredCourses; }
    public void setRequiredCourses(List<Course> requiredCourses) { this.requiredCourses = requiredCourses; }

    public List<Course> getElectiveCourses() { return electiveCourses; }
    public void setElectiveCourses(List<Course> electiveCourses) { this.electiveCourses = electiveCourses; }

    public int getElectiveCreditRequirement() { return electiveCreditRequirement; }
    public void setElectiveCreditRequirement(int electiveCreditRequirement) { this.electiveCreditRequirement = electiveCreditRequirement; }

    public int getTotalCreditRequirement() { return totalCreditRequirement; }
    public void setTotalCreditRequirement(int totalCreditRequirement) { this.totalCreditRequirement = totalCreditRequirement; }

    /**
     * Kiểm tra xem sinh viên tín chỉ đã hoàn thành tất cả học phần bắt buộc chưa.
     *
     * @param student Sinh viên hệ tín chỉ
     * @return true nếu hoàn thành; false nếu còn thiếu
     */
    public boolean hasCompletedAllRequired(CreditBasedStudent student) {
        for (Course course : requiredCourses) {
            if (!student.getCompletedCourseIDs().contains(course.getCourseID())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tính tổng tín chỉ đã hoàn thành từ các học phần tự chọn.
     *
     * @param student Sinh viên hệ tín chỉ
     * @return tổng tín chỉ
     */
    public int countCompletedElectiveCredits(CreditBasedStudent student) {
        int sum = 0;
        for (Course course : electiveCourses) {
            if (student.getCompletedCourseIDs().contains(course.getCourseID())) {
                sum += course.getCreditCount();
            }
        }
        return sum;
    }
}