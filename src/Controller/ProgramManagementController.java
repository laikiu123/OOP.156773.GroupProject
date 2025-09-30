package Controller;

import SVBK.manager.CourseManager;
import SVBK.manager.ProgramManager;
import SVBK.model.Course;
import SVBK.model.Program;
import SVBK.model.ProgramType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality; // Thêm cho Dialog
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Thêm cho Dialog

public class ProgramManagementController {

    @FXML
    private TableView<Program> tablePrograms;
    @FXML
    private TableColumn<Program, String> colMajorName;
    @FXML
    private TableColumn<Program, Integer> colTotalCredits;
    @FXML
    private TableColumn<Program, Integer> colElectiveCredits;

    // Thêm nút mới
    @FXML private Button btnAddProgram;
    @FXML private Button btnEditProgram; // Nút để sửa chi tiết chương trình
    @FXML private Button btnDeleteProgram;
    @FXML private Button btnRefreshPrograms;
    @FXML private Button btnBackToMainMenu;


    private ObservableList<Program> observablePrograms;
    private ProgramManager programManager;
    private CourseManager courseManager; // Thêm CourseManager

    @FXML
    public void initialize() {
        System.out.println("ProgramManagementController: initialize() được gọi.");
        colMajorName.setCellValueFactory(new PropertyValueFactory<>("majorName"));
        colTotalCredits.setCellValueFactory(new PropertyValueFactory<>("totalCreditRequirement"));
        colElectiveCredits.setCellValueFactory(new PropertyValueFactory<>("electiveCreditRequirement"));

        // Gán sự kiện cho các nút mới (nếu bạn đã đổi tên fx:id trong FXML)
        // Ví dụ: btnAddProgram.setOnAction(e -> handleAddNewProgramDetailed());
        // btnEditProgram.setOnAction(e -> handleEditProgramDetailed());
    }

    /**
     * Thiết lập các Manager cho controller này.
     * Sẽ được gọi từ MainMenuController.
     */
    public void setManagers(ProgramManager programManager, CourseManager courseManager) {
        this.programManager = programManager;
        this.courseManager = courseManager; // Gán CourseManager
        System.out.println("ProgramManagementController: setManagers() được gọi.");

        if (this.programManager != null && this.courseManager != null) {
            System.out.println("  ProgramManager và CourseManager đã được thiết lập.");
            System.out.println("  Số chương trình: " + this.programManager.getAllPrograms().size());
            System.out.println("  Số học phần (từ courseManager): " + this.courseManager.getAllCourses().size());
            loadProgramData();
        } else {
            System.err.println("  LỖI: Một hoặc cả hai manager (ProgramManager, CourseManager) được truyền vào là null.");
            if (this.programManager == null) showAlert(Alert.AlertType.ERROR, "Lỗi cấu hình", "ProgramManager chưa được thiết lập.");
            if (this.courseManager == null) showAlert(Alert.AlertType.ERROR, "Lỗi cấu hình", "CourseManager chưa được thiết lập.");
            loadProgramData(); // Vẫn gọi để clear table view nếu manager là null
        }
    }

    private void loadProgramData() {
        System.out.println("ProgramManagementController: loadProgramData() được gọi.");
        if (this.programManager == null) {
            // showAlert(Alert.AlertType.ERROR, "Lỗi cấu hình: ProgramManager chưa được thiết lập."); // Đã có ở setManagers
            System.err.println("  ProgramManager là null, không thể tải dữ liệu chương trình.");
            if (observablePrograms != null) {
                observablePrograms.clear();
            } else {
                observablePrograms = FXCollections.observableArrayList();
            }
            tablePrograms.setItems(observablePrograms);
            return;
        }
        try {
            List<Program> programsFromManager = this.programManager.getAllPrograms();
            System.out.println("  Số chương trình lấy từ manager: " + programsFromManager.size());
            observablePrograms = FXCollections.observableArrayList(programsFromManager);
            tablePrograms.setItems(observablePrograms);
            System.out.println("  Đã đặt " + tablePrograms.getItems().size() + " chương trình vào TableView.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi tải dữ liệu", "Lỗi khi tải dữ liệu chương trình từ ProgramManager: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddNewProgramDetailed() {
        if (this.programManager == null || this.courseManager == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi cấu hình", "Chưa thiết lập đủ Manager để thêm chương trình mới.");
            return;
        }
        try {
            // Load FXML cho dialog thêm/sửa chương trình
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/AddEditProgramDialog.fxml")); // Tên file FXML dialog mới
            Parent page = loader.load();

            // Tạo stage cho dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm chương trình đào tạo mới");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            // dialogStage.initOwner(((Node)event.getSource()).getScene().getWindow()); // Nếu muốn dialog phụ thuộc cửa sổ hiện tại
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Lấy controller của dialog và truyền manager vào
            AddEditProgramDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setManagers(programManager, courseManager);
            controller.setProgram(null); // null vì đây là thêm mới

            // Hiển thị dialog và đợi người dùng đóng lại
            dialogStage.showAndWait();

            // Sau khi dialog đóng, làm mới danh sách chương trình
            loadProgramData();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi giao diện", "Không thể mở cửa sổ thêm chương trình: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleEditProgramDetailed() {
        Program selectedProgram = tablePrograms.getSelectionModel().getSelectedItem();
        if (selectedProgram == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn chương trình", "Vui lòng chọn một chương trình để sửa.");
            return;
        }
        if (this.programManager == null || this.courseManager == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi cấu hình", "Chưa thiết lập đủ Manager để sửa chương trình.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/AddEditProgramDialog.fxml"));
            Parent page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sửa thông tin chương trình đào tạo");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AddEditProgramDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setManagers(programManager, courseManager);
            controller.setProgram(selectedProgram); // Truyền chương trình đang chọn để sửa

            dialogStage.showAndWait();
            loadProgramData();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi giao diện", "Không thể mở cửa sổ sửa chương trình: " + e.getMessage());
        }
    }


    @FXML
    private void handleDeleteProgram() { // Đổi tên từ handleDelete để tránh trùng
        if (this.programManager == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi cấu hình", "ProgramManager chưa được thiết lập để xóa.");
            return;
        }
        Program selected = tablePrograms.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Xác nhận xóa");
            confirmDialog.setHeaderText("Bạn có chắc chắn muốn xóa chương trình: " + selected.getMajorName() + "?");
            confirmDialog.setContentText("Hành động này không thể hoàn tác.");

            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (programManager.removeProgram(selected.getMajorName())) {
                    loadProgramData();
                    showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Đã xóa chương trình đào tạo: " + selected.getMajorName());
                } else {
                    showAlert(Alert.AlertType.WARNING, "Lỗi xóa", "Không thể xóa chương trình '" + selected.getMajorName() + "' từ Manager.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn một chương trình đào tạo để xóa.");
        }
    }

    @FXML
    private void handleRefreshPrograms() { // Đổi tên từ handleRefresh
        System.out.println("ProgramManagementController: handleRefreshPrograms() được gọi.");
        loadProgramData();
        showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Dữ liệu chương trình đào tạo đã được làm mới.");
    }

    @FXML
    private void handleBackToMainMenuProgram() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/MainMenu.fxml"));
            Parent root = loader.load();

            // Truyền lại managers của phiên hiện tại
            MainMenuController main = loader.getController();
            if (main != null) {
                main.initManagers(programManager, courseManager, null, null, null);
            }

            Stage stage = (Stage) tablePrograms.getScene().getWindow();

            if (stage.getScene() == null) {
                stage.setScene(new Scene(root));
            } else {
                stage.getScene().setRoot(root);  // giữ kích thước & trạng thái
            }

            if (stage.isFullScreen()) stage.setFullScreen(true);
            else stage.setMaximized(true);

            stage.setTitle("SSMS - Main Menu");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi điều hướng",
                      "Lỗi khi quay lại Menu Chính: " + e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}