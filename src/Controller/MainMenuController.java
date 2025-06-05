package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import SVBK.app.MainApp;
import SVBK.manager.CourseManager;
import SVBK.manager.ProgramManager;
import SVBK.manager.StudentManager;
import SVBK.service.GradingSystem;
import SVBK.service.GraduationValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML private Button btnStudentManagement, btnProgramManagement, btnCourseManagement, btnGrading, btnGraduationCheck, btnExit;

    private ProgramManager programManager;
    private CourseManager courseManager;
    private StudentManager studentManager;
    private GradingSystem gradingSystem;
    private GraduationValidator graduationValidator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("MainMenuController: initialize() được gọi.");
        // Cố gắng khởi tạo managers nếu chúng chưa được thiết lập (ví dụ, khi MainMenu là scene đầu tiên)
        // hoặc nếu chúng được truyền từ một controller trước đó (ví dụ, LoginController)
        if (this.programManager == null && MainApp.getStaticProgramManager() != null) { // Kiểm tra MainApp có instance không
            System.out.println("MainMenuController: Managers đang null, thử tải lại từ MainApp static instances.");
            this.initManagers(
                MainApp.getStaticProgramManager(),
                MainApp.getStaticCourseManager(),
                MainApp.getStaticStudentManager(),
                MainApp.getStaticGradingSystem(),
                MainApp.getStaticGraduationValidator()
            );
        } else if (this.programManager != null) {
            System.out.println("MainMenuController: initialize() - Managers đã được thiết lập trước đó.");
        } else {
            // Trường hợp này xảy ra nếu MainMenuController được load mà không có cách nào để lấy managers
            // và MainApp cũng không cung cấp chúng. Đây là một vấn đề cần được giải quyết ở tầng kiến trúc.
            System.err.println("MainMenuController: initialize() - QUAN TRỌNG: Managers là null VÀ MainApp static instances cũng null hoặc không truy cập được!");
            System.err.println("  Ứng dụng có thể không hoạt động đúng. Hãy đảm bảo managers được khởi tạo và truyền vào MainMenuController.");
            // Có thể hiển thị lỗi cho người dùng ở đây nếu đây là tình huống không mong muốn
            // showErrorAlert("Lỗi Khởi Tạo Hệ Thống", "Không thể khởi tạo các thành phần quản lý cốt lõi của ứng dụng.");
        }
    }

    /**
     * Phương thức này được dùng để MainApp (hoặc một controller khác)
     * truyền các instance manager vào MainMenuController.
     */
    public void initManagers(ProgramManager pm, CourseManager cm, StudentManager sm,
                             GradingSystem gs, GraduationValidator gv) {
        this.programManager = pm;
        this.courseManager = cm;
        this.studentManager = sm;
        this.gradingSystem = gs;
        this.graduationValidator = gv;
        System.out.println("MainMenuController: Phương thức initManagers ĐÃ ĐƯỢC GỌI. Các manager đã được thiết lập.");

        // Log chi tiết trạng thái các manager nhận được để dễ debug
        String pmStatus = (pm == null) ? "NULL" : "OK" + (pm.getAllPrograms() != null ? ", " + pm.getAllPrograms().size() + " programs" : ", programs list is null");
        String cmStatus = (cm == null) ? "NULL" : "OK" + (cm.getAllCourses() != null ? ", " + cm.getAllCourses().size() + " courses" : ", courses list is null");
        String smStatus = (sm == null) ? "NULL" : "OK" + (sm.getAllStudents() != null ? ", " + sm.getAllStudents().size() + " students" : ", students list is null");

        System.out.println("  programManager is " + pmStatus);
        System.out.println("  courseManager is " + cmStatus);
        System.out.println("  studentManager is " + smStatus);
        System.out.println("  gradingSystem is " + (gs == null ? "NULL" : "OK"));
        System.out.println("  graduationValidator is " + (gv == null ? "NULL" : "OK"));
    }

    @FXML
    void handleStudentManagement(ActionEvent event) {
        if (this.programManager == null || this.studentManager == null || this.courseManager == null || this.gradingSystem == null) {
            showErrorAlert("Lỗi Manager", "Một số Manager cần thiết cho Quản lý Sinh viên chưa được khởi tạo.");
            System.err.println("MainMenuController.handleStudentManagement: Một hoặc nhiều manager là null.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/StudentManagement.fxml"));
            Parent root = loader.load();
            StudentManagementController controller = loader.getController();
            System.out.println("MainMenuController: Đang truyền managers cho StudentManagementController.");
            controller.initManagersAndData(this.programManager, this.studentManager, this.courseManager, this.gradingSystem);
            showScene(event, root, "Quản lý Sinh viên");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Lỗi tải giao diện", "Không thể tải giao diện Quản lý Sinh viên: " + e.getMessage());
        }
    }

    @FXML
    void handleProgramManagement(ActionEvent event) {
        if (this.programManager == null || this.courseManager == null) {
            showErrorAlert("Lỗi Manager", "ProgramManager hoặc CourseManager chưa được khởi tạo.");
            System.err.println("MainMenuController.handleProgramManagement: ProgramManager hoặc CourseManager là null.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/ProgramManagement.fxml"));
            Parent root = loader.load();
            ProgramManagementController controller = loader.getController();
            System.out.println("MainMenuController: Đang truyền ProgramManager và CourseManager cho ProgramManagementController.");
            controller.setManagers(this.programManager, this.courseManager);
            showScene(event, root, "Quản lý Chương trình Đào tạo");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Lỗi tải giao diện", "Không thể tải giao diện Quản lý Chương trình Đào tạo: " + e.getMessage());
        }
    }

    @FXML
    void handleCourseManagement(ActionEvent event) {
         if (this.courseManager == null) {
            showErrorAlert("Lỗi Manager", "CourseManager chưa được khởi tạo.");
            System.err.println("MainMenuController.handleCourseManagement: courseManager là null.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/CourseManagement.fxml"));
            Parent root = loader.load();
            CourseManagementController controller = loader.getController();
            System.out.println("MainMenuController: Đang truyền CourseManager cho CourseManagementController.");
            controller.setCourseManager(this.courseManager);
            showScene(event, root, "Quản lý Học phần");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Lỗi tải giao diện", "Không thể tải giao diện Quản lý Học phần: " + e.getMessage());
        }
    }

    @FXML
    void handleGrading(ActionEvent event) {
        // GradingController cần StudentManager và CourseManager
        if (this.studentManager == null || this.courseManager == null) {
             showErrorAlert("Lỗi Manager", "StudentManager hoặc CourseManager cần cho Nhập điểm chưa được khởi tạo.");
             System.err.println("MainMenuController.handleGrading: studentManager hoặc courseManager là null.");
             return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/Grading.fxml"));
            Parent root = loader.load();
            GradingController controller = loader.getController(); // Lấy controller đúng kiểu
            
            System.out.println("MainMenuController: Đang truyền StudentManager và CourseManager cho GradingController.");
            // Truyền các manager cần thiết cho GradingController
            controller.initData(this.studentManager, this.courseManager);
            
            showScene(event, root, "Nhập điểm");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Lỗi tải giao diện", "Không thể tải giao diện Nhập điểm: " + e.getMessage());
        } catch (Exception e) { // Bắt các lỗi khác có thể xảy ra khi lấy controller hoặc initData
            e.printStackTrace();
            showErrorAlert("Lỗi Khởi Tạo", "Lỗi khi khởi tạo giao diện Nhập điểm: " + e.getMessage());
        }
    }

    @FXML
    void handleGraduationCheck(ActionEvent event) {
        // GraduationCheckController cần StudentManager, GradingSystem, và GraduationValidator
         if (this.studentManager == null || this.gradingSystem == null || this.graduationValidator == null) {
             showErrorAlert("Lỗi Manager", "Một số Manager/Logic cần cho Kiểm tra Tốt nghiệp chưa được khởi tạo.");
             System.err.println("MainMenuController.handleGraduationCheck: studentManager, gradingSystem, hoặc graduationValidator là null.");
             return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/GraduationCheck.fxml"));
            Parent root = loader.load();
            GraduationCheckController controller = loader.getController(); // Lấy controller đúng kiểu

            System.out.println("MainMenuController: Đang truyền managers cho GraduationCheckController.");
            // Truyền các manager/logic cần thiết cho GraduationCheckController
            controller.initData(this.studentManager, this.gradingSystem, this.graduationValidator);
            
            showScene(event, root, "Kiểm tra Tốt nghiệp");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Lỗi tải giao diện", "Không thể tải giao diện Kiểm tra Tốt nghiệp: " + e.getMessage());
        } catch (Exception e) { // Bắt các lỗi khác
            e.printStackTrace();
            showErrorAlert("Lỗi Khởi Tạo", "Lỗi khi khởi tạo giao diện Kiểm tra Tốt nghiệp: " + e.getMessage());
        }
    }

    @FXML
    void handleExit(ActionEvent event) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    private void showScene(ActionEvent event, Parent root, String title) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        // Cân nhắc việc setResizable(true) hoặc false tùy theo thiết kế của bạn cho từng màn hình
        // stage.setResizable(false); 
        stage.show();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}