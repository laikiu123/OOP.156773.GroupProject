// File: SVBK/manager/StudentManager.java
package SVBK.manager;

import SVBK.model.Student;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp StudentManager chịu trách nhiệm quản lý danh sách sinh viên:
 * thêm, xóa, tìm kiếm và sửa thông tin sinh viên.
 */
public class StudentManager {
    /**
     * Danh sách sinh viên được quản lý.
     */
    private List<Student> listStudents;

    /**
     * Constructor khởi tạo danh sách sinh viên rỗng.
     */
    public StudentManager() {
        this.listStudents = new ArrayList<>();
    }

    /**
     * Thêm sinh viên mới vào danh sách.
     *
     * @param student Đối tượng Student cần thêm
     * @return true nếu thêm thành công;
     *         false nếu student null hoặc đã tồn tại (trùng studentID).
     */
    public boolean addStudent(Student student) {
        if (student == null || findStudent(student.getStudentID()) != null) {
            return false;
        }
        listStudents.add(student);
        return true;
    }

    /**
     * Xóa sinh viên khỏi danh sách dựa trên mã sinh viên.
     *
     * @param studentID Mã sinh viên cần xóa
     * @return true nếu xóa thành công;
     *         false nếu không tìm thấy studentID.
     */
    public boolean removeStudent(String studentID) {
        Student s = findStudent(studentID);
        if (s == null) {
            return false;
        }
        listStudents.remove(s);
        return true;
    }

    /**
     * Tìm sinh viên theo mã sinh viên.
     *
     * @param studentID Mã sinh viên cần tìm
     * @return đối tượng Student nếu tìm thấy;
     *         null nếu studentID null hoặc không tồn tại.
     */
    public Student findStudent(String studentID) {
        if (studentID == null || studentID.trim().isEmpty()) {
            return null;
        }
        for (Student s : listStudents) {
            if (studentID.equals(s.getStudentID())) {
                return s;
            }
        }
        return null;
    }

    /**
     * Sửa thông tin sinh viên đã tồn tại.
     *
     * @param studentID Mã sinh viên cần sửa
     * @param newData   Đối tượng Student chứa thông tin mới;
     *                  studentID phải khớp với mã cũ.
     * @return true nếu sửa thành công;
     *         false nếu newData null, mã không khớp hoặc không tìm thấy.
     */
    public boolean editStudent(String studentID, Student newData) {
        if (newData == null || studentID == null || !studentID.equals(newData.getStudentID())) {
            return false;
        }
        for (int i = 0; i < listStudents.size(); i++) {
            if (studentID.equals(listStudents.get(i).getStudentID())) {
                listStudents.set(i, newData);
                return true;
            }
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả sinh viên.
     *
     * @return bản sao List<Student> để tránh chỉnh sửa trực tiếp danh sách nội bộ.
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(listStudents);
    }
}