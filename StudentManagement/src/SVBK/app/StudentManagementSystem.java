// File: SVBK/app/StudentManagementSystem.java
package SVBK.app;

import SVBK.manager.StudentManager;
import SVBK.manager.ProgramManager;
import SVBK.manager.CourseManager;
import SVBK.service.GradingSystem;
import SVBK.service.GraduationValidator;
import SVBK.model.Student;
import SVBK.model.Course;
import SVBK.model.Program;
import SVBK.model.CreditBasedStudent;
import SVBK.model.PartTimeStudent;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Controller chính của hệ thống Student Management.
 * Đảm nhiệm menu console và điều phối các Manager/Service.
 */
public class StudentManagementSystem {
    private StudentManager studentManager;
    private ProgramManager programManager;
    private CourseManager courseManager;
    private GradingSystem gradingSystem;
    private GraduationValidator graduationValidator;
    private Scanner scanner;

    /**
     * Constructor khởi tạo tất cả thành phần cần thiết.
     */
    public StudentManagementSystem() {
        this.programManager = new ProgramManager();
        this.courseManager  = new CourseManager(programManager);
        this.studentManager = new StudentManager();
        this.gradingSystem  = new GradingSystem();
        this.graduationValidator = new GraduationValidator();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Điểm vào chương trình, hiển thị menu chính.
     */
    public static void main(String[] args) {
        StudentManagementSystem sms = new StudentManagementSystem();
        sms.mainMenu();
    }

    /**
     * Menu chính với quyền quản lý sinh viên, chương trình, học phần, nhập điểm, kiểm tra tốt nghiệp.
     */
    public void mainMenu() {
        while (true) {
            System.out.println("=== Student Management System by SVBK ===");
            System.out.println("1. Quản lý sinh viên");
            System.out.println("2. Quản lý chương trình");
            System.out.println("3. Quản lý học phần");
            System.out.println("4. Nhập điểm");
            System.out.println("5. Kiểm tra tốt nghiệp");
            System.out.println("0. Thoát");
            System.out.print("Lựa chọn của bạn: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1: handleStudentManagement(); break;
                case 2: handleProgramManagement(); break;
                case 3: handleCourseManagement(); break;
                case 4: handleGrading(); break;
                case 5: handleGraduationCheck(); break;
                case 0:
                    System.out.println("Kết thúc chương trình. Tạm biệt!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    /**
     * Menu quản lý sinh viên.
     */
    private void handleStudentManagement() {
        while (true) {
            System.out.println("--- Quản lý sinh viên ---");
            System.out.println("1. Thêm sinh viên");
            System.out.println("2. Xóa sinh viên");
            System.out.println("3. Tìm sinh viên");
            System.out.println("4. Sửa sinh viên");
            System.out.println("5. Danh sách sinh viên");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng: ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;
            switch (choice) {
                case 1: {
                    System.out.print("Mã SV: "); String id = scanner.nextLine();
                    System.out.print("Tên SV: "); String name = scanner.nextLine();
                    System.out.print("Chọn hệ (1-Tín chỉ, 2-Niên chế): ");
                    int type = Integer.parseInt(scanner.nextLine());
                    System.out.print("Tên chương trình (ngành): ");
                    String major = scanner.nextLine();
                    Program prog = programManager.findProgram(major);
                    if (prog == null) {
                        System.out.println("Chương trình không tồn tại, vui lòng thêm trước.");
                    } else if (type == 1) {
                        studentManager.addStudent(new CreditBasedStudent(id, name, prog));
                        System.out.println("Đã thêm SV tín chỉ.");
                    } else if (type == 2) {
                        studentManager.addStudent(new PartTimeStudent(id, name, prog));
                        System.out.println("Đã thêm SV niên chế.");
                    } else {
                        System.out.println("Hệ không hợp lệ.");
                    }
                    break;
                }
                case 2: {
                    System.out.print("Mã SV cần xóa: ");
                    String id = scanner.nextLine();
                    if (studentManager.removeStudent(id)) System.out.println("Xóa thành công.");
                    else System.out.println("Không tìm thấy SV.");
                    break;
                }
                case 3: {
                    System.out.print("Mã SV cần tìm: ");
                    String id = scanner.nextLine();
                    Student s = studentManager.findStudent(id);
                    System.out.println(s != null ? s : "Không tìm thấy SV.");
                    break;
                }
                case 4: {
                    System.out.print("Mã SV cần sửa: ");
                    String id = scanner.nextLine();
                    Student old = studentManager.findStudent(id);
                    if (old == null) {
                        System.out.println("Không tìm thấy SV.");
                    } else {
                        System.out.print("Tên mới: ");
                        old.setStudentName(scanner.nextLine());
                        studentManager.editStudent(id, old);
                        System.out.println("Cập nhật thành công.");
                    }
                    break;
                }
                case 5: {
                    List<Student> list = studentManager.getAllStudents();
                    for (Student s : list) System.out.println(s);
                    break;
                }
                default:
                    System.out.println("Chức năng không hợp lệ.");
            }
        }
    }

    /**
     * Menu quản lý chương trình đào tạo.
     */
    private void handleProgramManagement() {
        while (true) {
            System.out.println("--- Quản lý chương trình đào tạo ---");
            System.out.println("1. Thêm chương trình");
            System.out.println("2. Xóa chương trình");
            System.out.println("3. Tìm chương trình");
            System.out.println("4. Đổi tên chương trình");
            System.out.println("5. Thêm học phần vào chương trình");
            System.out.println("6. Xóa học phần khỏi chương trình");
            System.out.println("7. Chỉnh ngưỡng tín chỉ tự chọn");
            System.out.println("8. Chỉnh ngưỡng tín chỉ tổng");
            System.out.println("9. Danh sách chương trình");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng: ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;
            switch (choice) {
                case 1: {
                    System.out.print("Tên chương trình (ngành): ");
                    String name = scanner.nextLine();
                    System.out.print("Số tín chỉ tự chọn yêu cầu: ");
                    int electiveReq = Integer.parseInt(scanner.nextLine());
                    System.out.print("Tổng số tín chỉ yêu cầu: ");
                    int totalReq = Integer.parseInt(scanner.nextLine());
                    List<Course> reqCourses = new ArrayList<>();
                    List<Course> elecCourses = new ArrayList<>();
                    System.out.print("Nhập số lượng học phần bắt buộc: ");
                    int numReq = Integer.parseInt(scanner.nextLine());
                    for (int i = 1; i <= numReq; i++) {
                        System.out.print("Mã HP bắt buộc #" + i + ": ");
                        Course c = courseManager.findCourse(scanner.nextLine());
                        if (c != null) reqCourses.add(c);
                        else System.out.println("HP không tồn tại, bỏ qua.");
                    }
                    System.out.print("Nhập số lượng học phần tự chọn: ");
                    int numElec = Integer.parseInt(scanner.nextLine());
                    for (int i = 1; i <= numElec; i++) {
                        System.out.print("Mã HP tự chọn #" + i + ": ");
                        Course c = courseManager.findCourse(scanner.nextLine());
                        if (c != null) elecCourses.add(c);
                        else System.out.println("HP không tồn tại, bỏ qua.");
                    }
                    Program p = new Program(name, reqCourses, elecCourses, electiveReq, totalReq);
                    if (programManager.addProgram(p)) System.out.println("Thêm chương trình thành công.");
                    else System.out.println("Chương trình đã tồn tại hoặc dữ liệu không hợp lệ.");
                    break;
                }
                case 2: {
                    System.out.print("Tên chương trình cần xóa: ");
                    if (programManager.removeProgram(scanner.nextLine())) System.out.println("Xóa thành công.");
                    else System.out.println("Không tìm thấy chương trình.");
                    break;
                }
                case 3: {
                    System.out.print("Tên chương trình cần tìm: ");
                    Program pr = programManager.findProgram(scanner.nextLine());
                    if (pr != null) {
                        System.out.println("Program: " + pr.getMajorName());
                        System.out.println("Required: " + pr.getRequiredCourses());
                        System.out.println("Elective: " + pr.getElectiveCourses());
                        System.out.println("Elective credit requirement: " + pr.getElectiveCreditRequirement());
                        System.out.println("Total credit requirement: " + pr.getTotalCreditRequirement());
                    } else {
                        System.out.println("Không tìm thấy chương trình.");
                    }
                    break;
                }
                case 4: {
                    System.out.print("Tên chương trình cũ: ");
                    String oldName = scanner.nextLine();
                    Program old = programManager.findProgram(oldName);
                    if (old == null) {
                        System.out.println("Không tìm thấy chương trình.");
                    } else {
                        System.out.print("Tên chương trình mới: ");
                        String newName = scanner.nextLine();
                        Program updated = new Program(newName,
                            old.getRequiredCourses(), old.getElectiveCourses(),
                            old.getElectiveCreditRequirement(), old.getTotalCreditRequirement());
                        if (programManager.editProgram(oldName, updated)) System.out.println("Đổi tên thành công.");
                        else System.out.println("Đổi tên thất bại.");
                    }
                    break;
                }
                case 5: {
                    System.out.print("Tên chương trình: ");
                    Program pr5 = programManager.findProgram(scanner.nextLine());
                    if (pr5 == null) {
                        System.out.println("Không tìm thấy chương trình.");
                    } else {
                        System.out.print("Chọn loại (1-Bắt buộc, 2-Tự chọn): ");
                        int t = Integer.parseInt(scanner.nextLine());
                        System.out.print("Mã học phần cần thêm: ");
                        Course c5 = courseManager.findCourse(scanner.nextLine());
                        if (c5 == null) System.out.println("HP không tồn tại.");
                        else {
                            if (t == 1) pr5.getRequiredCourses().add(c5);
                            else if (t == 2) pr5.getElectiveCourses().add(c5);
                            System.out.println("Đã thêm học phần vào chương trình.");
                        }
                    }
                    break;
                }
                case 6: {
                    System.out.print("Tên chương trình: ");
                    Program pr6 = programManager.findProgram(scanner.nextLine());
                    if (pr6 == null) {
                        System.out.println("Không tìm thấy chương trình.");
                    } else {
                        System.out.print("Chọn loại (1-Bắt buộc, 2-Tự chọn): ");
                        int t = Integer.parseInt(scanner.nextLine());
                        System.out.print("Mã học phần cần xóa: ");
                        String cid = scanner.nextLine();
                        if (t == 1) pr6.getRequiredCourses().removeIf(c -> c.getCourseID().equals(cid));
                        else if (t == 2) pr6.getElectiveCourses().removeIf(c -> c.getCourseID().equals(cid));
                        System.out.println("Đã xóa học phần khỏi chương trình.");
                    }
                    break;
                }
                case 7: {
                    System.out.print("Tên chương trình: ");
                    Program pr7 = programManager.findProgram(scanner.nextLine());
                    if (pr7 == null) System.out.println("Không tìm thấy chương trình.");
                    else {
                        System.out.print("Ngưỡng tín chỉ tự chọn mới: ");
                        pr7.setElectiveCreditRequirement(Integer.parseInt(scanner.nextLine()));
                        System.out.println("Cập nhật thành công.");
                    }
                    break;
                }
                case 8: {
                    System.out.print("Tên chương trình: ");
                    Program pr8 = programManager.findProgram(scanner.nextLine());
                    if (pr8 == null) System.out.println("Không tìm thấy chương trình.");
                    else {
                        System.out.print("Tổng tín chỉ yêu cầu mới: ");
                        pr8.setTotalCreditRequirement(Integer.parseInt(scanner.nextLine()));
                        System.out.println("Cập nhật thành công.");
                    }
                    break;
                }
                case 9: {
                    List<Program> list = programManager.getAllPrograms();
                    for (Program p : list) {
                        System.out.println("Program: " + p.getMajorName());
                        System.out.println(" Required: " + p.getRequiredCourses());
                        System.out.println(" Elective: " + p.getElectiveCourses());
                        System.out.println(" Elective credit requirement: " + p.getElectiveCreditRequirement());
                        System.out.println(" Total credit requirement: " + p.getTotalCreditRequirement());
                        System.out.println();
                    }
                    break;
                }
                default:
                    System.out.println("Chức năng không hợp lệ.");
            }
        }
    }

    /**
     * Menu quản lý học phần.
     */
    private void handleCourseManagement() {
        while (true) {
            System.out.println("--- Quản lý học phần ---");
            System.out.println("1. Thêm học phần");
            System.out.println("2. Xóa học phần");
            System.out.println("3. Tìm học phần");
            System.out.println("4. Sửa học phần");
            System.out.println("5. Danh sách học phần");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng: ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;
            switch (choice) {
                case 1: {
                    System.out.print("Mã HP: "); String id = scanner.nextLine();
                    System.out.print("Tên HP: "); String name = scanner.nextLine();
                    System.out.print("Số tín chỉ: "); int credits = Integer.parseInt(scanner.nextLine());
                    System.out.print("HP tiên quyết (nếu có): "); String pre = scanner.nextLine();
                    Course c = new Course(id, name, credits, pre, 0.5);
                    if (courseManager.addCourse(c)) System.out.println("Thêm thành công.");
                    else System.out.println("Thêm thất bại hoặc đã tồn tại.");
                    break;
                }
                case 2: {
                    System.out.print("Mã HP cần xóa: ");
                    String id = scanner.nextLine();
                    if (courseManager.removeCourse(id)) System.out.println("Xóa thành công.");
                    else System.out.println("Không tìm thấy HP.");
                    break;
                }
                case 3: {
                    System.out.print("Mã HP cần tìm: ");
                    String id = scanner.nextLine();
                    Course c = courseManager.findCourse(id);
                    System.out.println(c != null ? c : "Không tìm thấy HP.");
                    break;
                }
                case 4: {
                    System.out.print("Mã HP cần sửa: ");
                    String id = scanner.nextLine();
                    Course old = courseManager.findCourse(id);
                    if (old == null) {
                        System.out.println("Không tìm thấy HP.");
                    } else {
                        System.out.print("Tên mới: ");
                        old.setCourseName(scanner.nextLine());
                        courseManager.editCourse(id, old);
                        System.out.println("Cập nhật thành công.");
                    }
                    break;
                }
                case 5: {
                    List<Course> list = courseManager.getAllCourses();
                    for (Course c : list) System.out.println(c);
                    break;
                }
                default:
                    System.out.println("Chức năng không hợp lệ.");
            }
        }
    }

    /**
     * Xử lý nhập điểm.
     */
    private void handleGrading() {
        System.out.println("--- Nhập điểm ---");
        System.out.print("Mã SV: "); String sid = scanner.nextLine();
        Student s = studentManager.findStudent(sid);
        if (s == null) { System.out.println("Không tìm thấy SV."); return; }
        System.out.print("Mã HP: "); String cid = scanner.nextLine();
        Course c = courseManager.findCourse(cid);
        if (c == null) { System.out.println("Không tìm thấy HP."); return; }
        System.out.print("Điểm giữa kỳ: "); double mid = Double.parseDouble(scanner.nextLine());
        System.out.print("Điểm cuối kỳ: "); double fin = Double.parseDouble(scanner.nextLine());
        if (gradingSystem.enterGrade(s, c, mid, fin)) {
            System.out.println("Nhập điểm thành công.");
            if (s instanceof CreditBasedStudent) ((CreditBasedStudent) s).registerCourse(c);
        } else {
            System.out.println("Nhập điểm thất bại. Kiểm tra lại.");
        }
    }

    /**
     * Xử lý kiểm tra và xếp loại tốt nghiệp.
     */
    private void handleGraduationCheck() {
        System.out.println("--- Kiểm tra tốt nghiệp ---");
        System.out.print("Mã SV: "); String sid = scanner.nextLine();
        Student s = studentManager.findStudent(sid);
        if (s == null) { System.out.println("Không tìm thấy SV."); return; }
        boolean ok = graduationValidator.checkGraduation(s);
        String type = graduationValidator.typeGraduation(s);
        int credits = graduationValidator.calNumCredits(s);
        System.out.printf("SV %s - %s\n", s.getStudentID(), s.getStudentName());
        System.out.printf("Tổng tín chỉ: %d\n", credits);
        System.out.printf("Điều kiện tốt nghiệp: %s\n", ok ? "Đủ điều kiện" : "Chưa đủ điều kiện");
        System.out.printf("Xếp loại học lực: %s\n", type);
    }
}
