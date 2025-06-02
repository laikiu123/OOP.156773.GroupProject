package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; // Cần cho việc quay lại MainMenu (nếu muốn load lại scene)
import javafx.scene.Parent;    // Cần cho việc quay lại MainMenu
import javafx.scene.Scene;     // Cần cho việc quay lại MainMenu
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// Import các lớp model, manager, service
import Manager.StudentManager;
import Manager.ProgramManager; // Vẫn giữ nếu GraduationValidator hoặc GradingSystem cần nó gián tiếp
import Manager.CourseManager;  // Vẫn giữ nếu GraduationValidator hoặc GradingSystem cần nó gián tiếp

import java.io.IOException;

import Logic.GradingSystem;
import Logic.GraduationValidator;
import model.Student;
import model.Program;
import model.CreditBasedStudent;
import model.PartTimeStudent;

public class GraduationCheckController {

    @FXML
    private TextField studentIdField;
    @FXML
    private TextArea resultArea;
    @FXML
    private Button backButton; // fx:id="backButton" cần được đặt cho nút "Quay Lại" trong FXML

    // Khai báo các đối tượng quản lý nghiệp vụ
    private StudentManager studentManager;
    // ProgramManager và CourseManager có thể không cần trực tiếp ở đây nếu các logic
    // như lấy program, tính toán tín chỉ... đã nằm trong Student, GraduationValidator, GradingSystem
    // Tuy nhiên, chúng vẫn có thể được truyền vào qua initData nếu các services kia cần.
    // private ProgramManager programManager;
    // private CourseManager courseManager;
    private GradingSystem gradingSystem;
    private GraduationValidator graduationValidator;

    public GraduationCheckController() {
        // Constructor để trống. Các manager sẽ được inject qua initData.
        System.out.println("GraduationCheckController: Constructor called.");
    }

    /**
     * Khởi tạo controller với các manager cần thiết.
     * Phương thức này nên được gọi từ controller đã gọi nó (ví dụ: MainMenuController)
     * sau khi FXMLLoader đã load xong FXML này.
     */
    public void initData(StudentManager studentManager, /*ProgramManager programManager, CourseManager courseManager,*/ GradingSystem gradingSystem, GraduationValidator graduationValidator) {
        this.studentManager = studentManager;
        // this.programManager = programManager; // Bỏ comment nếu cần
        // this.courseManager = courseManager;   // Bỏ comment nếu cần
        this.gradingSystem = gradingSystem;
        this.graduationValidator = graduationValidator;
        System.out.println("GraduationCheckController: initData() called. Managers initialized.");
        if (this.studentManager == null) System.err.println("  WARNING: StudentManager is null!");
        if (this.gradingSystem == null) System.err.println("  WARNING: GradingSystem is null!");
        if (this.graduationValidator == null) System.err.println("  WARNING: GraduationValidator is null!");
    }


    @FXML
    public void initialize() {
        resultArea.setText("Nhập Mã Sinh viên và nhấn \"Kiểm tra\".");
        System.out.println("GraduationCheckController: initialize() called.");
    }

    @FXML
    private void handleCheckGraduation() {
        resultArea.clear();

        String sid = studentIdField.getText().trim().toUpperCase();

        if (sid.isEmpty()) {
            resultArea.appendText("Vui lòng nhập mã sinh viên.\n");
            return;
        }

        // Kiểm tra xem các services đã được inject chưa
        if (studentManager == null || gradingSystem == null || graduationValidator == null) {
            resultArea.appendText("Lỗi: Các dịch vụ quản lý chưa được khởi tạo đúng cách.\n" +
                                  "Vui lòng đảm bảo initData() được gọi với các tham số hợp lệ.\n");
            System.err.println("GraduationCheckController: One or more managers are null in handleCheckGraduation.");
            return;
        }

        Student s = studentManager.findStudent(sid);
        if (s == null) {
            resultArea.appendText("Không tìm thấy sinh viên với mã: " + sid + "\n");
            return;
        }

        Program pr = null;
        // Giả định rằng getProgram() trả về kiểu model.Program và đã được load sẵn trong Student
        if (s instanceof CreditBasedStudent) {
            pr = ((CreditBasedStudent) s).getProgram();
        } else if (s instanceof PartTimeStudent) {
            pr = ((PartTimeStudent) s).getProgram();
        } else {
            resultArea.appendText("Loại sinh viên không xác định.\n");
            return;
        }

        if (pr == null) {
            resultArea.appendText("Không tìm thấy thông tin chương trình học cho sinh viên: " + sid + "\n");
            return;
        }

        // Thực hiện tính toán
        double cpa = gradingSystem.calculateCPA(s);
        boolean ok = graduationValidator.checkGraduation(s);
        String rank = graduationValidator.typeGraduation(s);

        // Hiển thị kết quả
        resultArea.appendText("--- Kết quả kiểm tra tốt nghiệp ---\n");
        resultArea.appendText(String.format("Mã SV: %s\n", s.getStudentID()));
        resultArea.appendText(String.format("Tên SV: %s\n", s.getStudentName()));
        resultArea.appendText(String.format("CPA: %.2f\n", cpa));

        if (s instanceof CreditBasedStudent) {
            // graduationValidator.calNumCredits(s) cần Student object đã load Enrollments
            int achieved = graduationValidator.calNumCredits(s);
            // pr.getTotalCreditRequirement() cần Program object đã load thông tin
            int required = pr.getTotalCreditRequirement();
            resultArea.appendText(String.format("Tổng tín chỉ đạt: %d/%d\n", achieved, required));
        } else if (s instanceof PartTimeStudent) {
            // s.getCompletedEnrollments() cần Student object đã load Enrollments và tính toán trạng thái hoàn thành
            int achievedCourses = (s.getCompletedEnrollments() != null) ? s.getCompletedEnrollments().size() : 0;
            // pr.getRequiredCourses() cần Program object đã load danh sách môn bắt buộc
            int totalCourses = (pr.getRequiredCourses() != null) ? pr.getRequiredCourses().size() : 0;
            resultArea.appendText(String.format("Số môn học đã đạt: %d/%d\n", achievedCourses, totalCourses));
        }

        resultArea.appendText(String.format("Điều kiện tốt nghiệp: %s\n", ok ? "Đủ điều kiện" : "Chưa đủ điều kiện"));
        resultArea.appendText(String.format("Xếp loại học lực: %s\n", rank != null ? rank : "Chưa xác định"));
    }

    @FXML
    private void handleBackToMainMenu() {
        System.out.println("Attempting to go back to main menu / close window...");
        Stage stage = null;
        // Ưu tiên lấy stage từ nút được nhấn trực tiếp nếu có fx:id
        if (backButton != null && backButton.getScene() != null) {
            stage = (Stage) backButton.getScene().getWindow();
        }
        // Nếu không, thử lấy từ một control khác trên scene
        else if (studentIdField != null && studentIdField.getScene() != null) {
            stage = (Stage) studentIdField.getScene().getWindow();
        }

        if (stage != null) {
            // Lựa chọn 1: Đóng cửa sổ hiện tại (nếu đây là cửa sổ phụ)
            //stage.close();

            // Lựa chọn 2: Load lại MainMenu.fxml trên cùng Stage (giống GradingController)
            // Nếu bạn muốn hành vi này, hãy bỏ comment phần dưới và comment stage.close()
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/MainMenu.fxml")); // Đường dẫn FXML của Main Menu
                Parent root = loader.load();
                MainMenuController mainMenuController = loader.getController();
                // // Nếu MainMenuController cũng cần initData, bạn có thể gọi ở đây
                // mainMenuController.initData(...); // Truyền các manager cần thiết

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Menu Chính"); // Đặt lại tiêu đề
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                resultArea.appendText("Lỗi khi quay lại Menu Chính: " + e.getMessage() + "\n");
            }
            
        } else {
            if (resultArea != null) {
                resultArea.appendText("Không thể xác định cửa sổ hiện tại để đóng.\n");
            }
            System.err.println("Could not get current stage to close.");
        }
    }
}