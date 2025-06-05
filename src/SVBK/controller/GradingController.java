package SVBK.controller;

import SVBK.app.MainApp;
import SVBK.manager.CourseManager;
import SVBK.manager.ProgramManager;
import SVBK.manager.StudentManager;
import SVBK.model.Course;
import SVBK.model.CreditBasedStudent;
import SVBK.model.Enrollment;
import SVBK.model.PartTimeStudent;
import SVBK.model.Program;
import SVBK.model.Student;
import SVBK.service.GradingSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class GradingController {

    @FXML private TextField txtStudentID;
    @FXML private TextField txtCourseID;
    @FXML private TextField txtMidterm;
    @FXML private TextField txtFinal;
    @FXML private Label lblStatus;
    @FXML private Button btnBackToMainMenu;

    @FXML private Label lblFoundStudentName;
    @FXML private Label lblFoundCourseName;
    @FXML private Label lblFoundProgramName;

    private StudentManager studentManager;
    private CourseManager courseManager;
    private ProgramManager programManager;   // Sẽ lấy từ MainApp
    private GradingSystem gradingSystem;     // Sẽ lấy từ MainApp

    public GradingController() {
        // Constructor
    }

    /**
     * Được gọi từ MainMenuController.
     * Nhận các manager cơ bản, các manager khác sẽ được lấy từ MainApp.
     */
    public void initData(StudentManager studentManager, CourseManager courseManager) {
        this.studentManager = studentManager;
        this.courseManager = courseManager;
        System.out.println("GradingController: initData(SM, CM) được gọi.");

        // Lấy các manager/logic khác từ MainApp thông qua static getters
        this.programManager = MainApp.getStaticProgramManager();
        this.gradingSystem = MainApp.getStaticGradingSystem();

        if (this.studentManager == null) System.err.println("  WARNING: StudentManager là null trong GradingController!");
        if (this.courseManager == null) System.err.println("  WARNING: CourseManager là null trong GradingController!");
        if (this.programManager == null) System.err.println("  WARNING: ProgramManager lấy từ MainApp là null!");
        if (this.gradingSystem == null) System.err.println("  WARNING: GradingSystem lấy từ MainApp là null!");
    }

    @FXML
    public void initialize() {
        System.out.println("GradingController: initialize() được gọi.");
        if (lblStatus != null) lblStatus.setText("Sẵn sàng nhập điểm.");
        clearInfoLabels();

        txtStudentID.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !txtStudentID.getText().trim().isEmpty()) {
                findAndDisplayStudentInfo();
            }
        });
         txtCourseID.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !txtCourseID.getText().trim().isEmpty()) {
                findAndDisplayCourseInfo();
            }
        });
    }
    
    private void clearInfoLabels() {
        if (lblFoundStudentName != null) lblFoundStudentName.setText("");
        if (lblFoundCourseName != null) lblFoundCourseName.setText("");
        if (lblFoundProgramName != null) lblFoundProgramName.setText("");
    }

    private Student findAndDisplayStudentInfo() {
        clearInfoLabels();
        String studentId = txtStudentID.getText().trim().toUpperCase();
        if (studentId.isEmpty() || studentManager == null) return null;
        
        Student student = studentManager.findStudent(studentId);
        if (student != null) {
            if(lblFoundStudentName != null) lblFoundStudentName.setText("SV: " + student.getStudentName() + " (" + student.getStudentType() + ")");
            Program p = student.getProgram(); 
            if (p != null && lblFoundProgramName != null) lblFoundProgramName.setText("CT: " + p.getMajorName());
        } else {
            if(lblFoundStudentName != null) lblFoundStudentName.setText("Không tìm thấy SV: " + studentId);
        }
        return student;
    }

    private Course findAndDisplayCourseInfo() {
        if(lblFoundCourseName != null) lblFoundCourseName.setText("");
        String courseId = txtCourseID.getText().trim().toUpperCase();
         if (courseId.isEmpty() || courseManager == null) return null;

        Course course = courseManager.findCourse(courseId);
        if (course != null) {
             if(lblFoundCourseName != null) lblFoundCourseName.setText("HP: " + course.getCourseName());
        } else {
            if(lblFoundCourseName != null) lblFoundCourseName.setText("Không tìm thấy HP: " + courseId);
        }
        return course;
    }

    @FXML
    public void handleSaveGrade() {
        lblStatus.setText(""); 
        // Kiểm tra các manager đã được khởi tạo (bao gồm cả những cái lấy từ MainApp)
        if (studentManager == null || courseManager == null || programManager == null || gradingSystem == null) {
            showError("Lỗi hệ thống: Một hoặc nhiều thành phần quản lý (Manager/Logic) chưa được khởi tạo đúng cách.");
            System.err.println("GradingController.handleSaveGrade: Một hoặc nhiều manager là null.");
            System.err.println("  studentManager: " + (studentManager == null));
            System.err.println("  courseManager: " + (courseManager == null));
            System.err.println("  programManager: " + (programManager == null));
            System.err.println("  gradingSystem: " + (gradingSystem == null));
            return;
        }

        String studentId = txtStudentID.getText().trim().toUpperCase();
        String courseId = txtCourseID.getText().trim().toUpperCase();
        String midtermStr = txtMidterm.getText().trim();
        String finalStr = txtFinal.getText().trim();

        if (studentId.isEmpty() || courseId.isEmpty() || midtermStr.isEmpty() || finalStr.isEmpty()) {
            showError("Vui lòng nhập đầy đủ Mã SV, Mã HP và các cột điểm.");
            return;
        }

        Student student = studentManager.findStudent(studentId); // Không cần gọi findAndDisplayStudentInfo() lại ở đây nếu đã có listener
        if (student == null) {
            showError("Không tìm thấy sinh viên với mã: " + studentId);
            if(lblFoundStudentName!=null) lblFoundStudentName.setText("Không tìm thấy SV: " + studentId); // Cập nhật label
            return;
        }
        if(lblFoundStudentName != null) lblFoundStudentName.setText("SV: " + student.getStudentName() + " (" + student.getStudentType() + ")");


        Course course = courseManager.findCourse(courseId); // Không cần gọi findAndDisplayCourseInfo() lại
        if (course == null) {
            showError("Không tìm thấy học phần với mã: " + courseId);
             if(lblFoundCourseName!=null) lblFoundCourseName.setText("Không tìm thấy HP: " + courseId); // Cập nhật label
            return;
        }
        if(lblFoundCourseName != null) lblFoundCourseName.setText("HP: " + course.getCourseName());


        Program studentProgram = student.getProgram(); // Lỗi này cần được sửa bằng cách thêm abstract getProgram() vào Student.java
        if (studentProgram == null) {
            showError("Sinh viên '" + studentId + "' chưa được gán vào chương trình đào tạo nào.");
             if(lblFoundProgramName!=null) lblFoundProgramName.setText("CT: Chưa gán");
            return;
        }
        if(lblFoundProgramName != null) lblFoundProgramName.setText("CT: " + studentProgram.getMajorName());


        // 4) Kiểm tra khóa học có trong chương trình SV không
        boolean courseIsValidForStudentProgram;
        if (student instanceof CreditBasedStudent) {
            courseIsValidForStudentProgram = studentProgram.containsCourse(courseId);
            if (!courseIsValidForStudentProgram) {
                showError("Học phần '" + courseId + "' không thuộc chương trình tín chỉ của sinh viên.");
                return;
            }
        } else if (student instanceof PartTimeStudent) {
            courseIsValidForStudentProgram = studentProgram.isRequiredCourse(courseId);
            if (!courseIsValidForStudentProgram) {
                showError("Học phần '" + courseId + "' không phải là học phần bắt buộc của sinh viên tại chức/niên chế.");
                return;
            }
        } else {
            showError("Không xác định được loại sinh viên để kiểm tra chương trình.");
            return;
        }

        // 4a) Kiểm tra xem SV đã đăng ký học phần này chưa
        boolean isEnrolled = student.getEnrollments().stream()
            .anyMatch(e -> e.getCourse() != null && e.getCourse().getCourseID().equalsIgnoreCase(courseId));
        if (!isEnrolled) {
            showError("Sinh viên '" + studentId + "' chưa đăng ký học phần '" + courseId + "'. Vui lòng đăng ký trước.");
            return;
        }

        try {
            double midterm = Double.parseDouble(midtermStr);
            double finalExam = Double.parseDouble(finalStr);

            if (midterm < 0 || midterm > 10 || finalExam < 0 || finalExam > 10) {
                showError("Điểm giữa kỳ và cuối kỳ phải nằm trong khoảng từ 0.0 đến 10.0.");
                return;
            }

            System.out.println("GradingController: Gọi gradingSystem.enterGrade() cho SV: " + studentId + ", HP: " + courseId);
            boolean gradeUpdated = gradingSystem.enterGrade(student, course, midterm, finalExam);

            if (gradeUpdated) {
                Enrollment enrollment = student.getEnrollments().stream()
                                           .filter(e -> e.getCourse() != null && e.getCourse().getCourseID().equalsIgnoreCase(courseId))
                                           .findFirst().orElse(null);
                
                String successMsg = "Đã lưu điểm cho SV " + studentId + " - HP " + courseId + ".";
                if (enrollment != null) {
                    double finalCourseGrade = enrollment.calculateFinalGrade();
                    successMsg += String.format(" Điểm HP: %.2f.", finalCourseGrade);
                    if (enrollment.isPassed()) {
                        successMsg += " Sinh viên ĐẠT.";
                    } else {
                        successMsg += " Sinh viên KHÔNG ĐẠT.";
                    }
                }
                lblStatus.setText(successMsg);
                showInfo("Thành công", successMsg);
                System.out.println("  " + successMsg);
                
                txtMidterm.clear();
                txtFinal.clear();
                txtCourseID.requestFocus(); 
            } else {
                showError("Lưu điểm thất bại. Vui lòng kiểm tra lại (có thể lỗi logic trong GradingSystem.enterGrade).");
                System.err.println("  gradingSystem.enterGrade() trả về false cho SV: " + studentId + ", HP: " + courseId);
            }

        } catch (NumberFormatException e) {
            showError("Điểm giữa kỳ và cuối kỳ phải là số (ví dụ: 7.5).");
        } catch (Exception e) {
            showError("Đã xảy ra lỗi không mong muốn khi lưu điểm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBackToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/MainMenu.fxml")); 
            Parent root = loader.load();
            Stage stage = (Stage) btnBackToMainMenu.getScene().getWindow(); 
            
            System.out.println("GradingController: Quay lại MainMenu.");
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Chính"); 
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi khi quay lại Menu Chính: " + e.getMessage());
        }
    }

    private void showError(String message) {
        if (lblStatus != null) {
            lblStatus.setText("Lỗi: " + message);
            lblStatus.setStyle("-fx-text-fill: red;");
        }
        showAlert(Alert.AlertType.ERROR, "Lỗi Nhập Điểm", message);
    }

    private void showInfo(String title, String message) {
        if (lblStatus != null) {
            lblStatus.setText(message);
            lblStatus.setStyle("-fx-text-fill: green;");
        }
        showAlert(Alert.AlertType.INFORMATION, title, message);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}