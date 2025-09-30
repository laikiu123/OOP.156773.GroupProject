package Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import SVBK.manager.CourseManager;
import SVBK.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality; // Thêm cho Dialog của CourseList
import javafx.stage.Stage;

public class CourseManagementController {

    @FXML private TextField txtCourseID;
    @FXML private TextField txtCourseName;
    @FXML private TextField txtCredits;
    @FXML private TextField txtPrerequisite;
    @FXML private TextField txtFinalWeight;

    // Nút mới để làm trống form
    @FXML private Button btnClearForm; 

    @FXML private Button btnAdd;
    @FXML private Button btnDelete;
    @FXML private Button btnFind;
    @FXML private Button btnEdit;
    @FXML private Button btnList;
    @FXML private Button btnBack;

    @FXML private ListView<Course> listCourses;
    @FXML private Label lblMessage;

    private CourseManager courseManager;

    public CourseManagementController() {
        // Constructor
    }

    @FXML
    private void initialize() {
        System.out.println("CourseManagementController: initialize() được gọi.");
        // Gán sự kiện cho các nút
        if (btnClearForm != null) {
            btnClearForm.setOnAction(e -> {
                clearForm();
                lblMessage.setText("Form đã được làm trống, sẵn sàng nhập mới.");
            });
        } else {
            System.err.println("CourseManagementController: initialize() - btnClearForm là null. Kiểm tra fx:id trong FXML.");
        }
        
        // Các nút khác
        if (btnAdd != null) btnAdd.setOnAction(e -> handleAddCourse());
        if (btnDelete != null) btnDelete.setOnAction(e -> handleDeleteCourse());
        if (btnFind != null) btnFind.setOnAction(e -> handleFindCourse());
        if (btnEdit != null) btnEdit.setOnAction(e -> handleEditCourse());
        if (btnList != null) btnList.setOnAction(e -> handleOpenCourseListWindow());
        if (btnBack != null) btnBack.setOnAction(e -> handleBackToMainMenu(e));


        if (courseManager != null) {
            System.out.println("CourseManagementController: CourseManager đã tồn tại trong initialize(), đang hiển thị danh sách.");
            showCourseList();
        } else {
            System.out.println("CourseManagementController: CourseManager là null trong initialize(). Đang chờ setCourseManager.");
        }
    }

    private void handleAddCourse() {
        if (courseManager == null) {
             lblMessage.setText("Lỗi: CourseManager chưa được khởi tạo.");
             System.err.println("Lỗi thêm course: CourseManager là null.");
             return;
        }
        // Đảm bảo ID có thể nhập được khi thêm
        if (!txtCourseID.isEditable()) {
            lblMessage.setText("Vui lòng 'Nhập mới' trước khi thêm học phần.");
            return;
        }

        String id = txtCourseID.getText().trim().toUpperCase();
        String name = txtCourseName.getText().trim();
        String pre = txtPrerequisite.getText().trim();
        if (pre.isEmpty()) pre = null;
        String finalWeightStr = txtFinalWeight.getText().trim();

        int credits;
        double finalWeightValue;

        if (id.isEmpty() || name.isEmpty()) {
            lblMessage.setText("Mã học phần và tên học phần không được để trống.");
            return;
        }
        try {
            credits = Integer.parseInt(txtCredits.getText().trim());
            if (credits <= 0) {
                lblMessage.setText("Số tín chỉ phải là số dương.");
                return;
            }
        } catch (NumberFormatException ex) {
            lblMessage.setText("Số tín chỉ phải là một số nguyên.");
            return;
        }

        try {
            finalWeightValue = Double.parseDouble(finalWeightStr);
            if (finalWeightValue < 0.0 || finalWeightValue > 1.0) {
                lblMessage.setText("Trọng số điểm cuối kỳ phải từ 0.0 đến 1.0.");
                return;
            }
        } catch (NumberFormatException ex) {
            lblMessage.setText("Trọng số điểm cuối kỳ phải là một số (ví dụ: 0.7).");
            return;
        }
        
        Course c = new Course(id, name, credits, pre, finalWeightValue);

        if (courseManager.addCourse(c)) {
            lblMessage.setText("Thêm học phần thành công: " + c.getCourseName());
            clearForm(); // Xóa form sau khi thêm thành công
            showCourseList();
        } else {
            lblMessage.setText("Mã học phần '" + id + "' đã tồn tại hoặc có lỗi khi thêm.");
        }
    }

    private void handleDeleteCourse() {
        if (courseManager == null) {
             lblMessage.setText("Lỗi: CourseManager chưa được khởi tạo.");
             System.err.println("CourseManagementController.handleDeleteCourse: Lỗi - CourseManager là null.");
             return;
        }
        
        String idToDelete;
        Course selectedCourse = listCourses.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            idToDelete = selectedCourse.getCourseID();
            System.out.println("CourseManagementController.handleDeleteCourse: Sẽ xóa course được chọn từ ListView, ID: " + idToDelete);
        } else {
            idToDelete = txtCourseID.getText().trim().toUpperCase();
            if (idToDelete.isEmpty()) {
                 lblMessage.setText("Vui lòng nhập mã học phần hoặc chọn một học phần từ danh sách để xóa.");
                 System.out.println("CourseManagementController.handleDeleteCourse: Không có ID nhập hoặc course được chọn.");
                return;
            }
            System.out.println("CourseManagementController.handleDeleteCourse: Sẽ xóa course với ID từ TextField: " + idToDelete);
        }
        
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Xác nhận xóa");
        confirmDialog.setHeaderText("Bạn có chắc chắn muốn xóa học phần có mã: " + idToDelete + "?");
        confirmDialog.setContentText("Hành động này sẽ xóa học phần khỏi hệ thống và có thể ảnh hưởng đến các chương trình đào tạo liên quan.");
        
        final String finalIdToDelete = idToDelete; 
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("CourseManagementController.handleDeleteCourse: Người dùng nhấn OK để xóa ID: " + finalIdToDelete);
                if (courseManager == null) { // Kiểm tra lại courseManager một lần nữa trong lambda
                    System.err.println("CourseManagementController.handleDeleteCourse: LỖI BÊN TRONG LAMBDA - courseManager là NULL!");
                    lblMessage.setText("Lỗi nghiêm trọng: CourseManager đã bị null. Không thể xóa.");
                    return;
                }
                System.out.println("  (Bên trong lambda) courseManager KHÔNG NULL. Chuẩn bị gọi courseManager.removeCourse('" + finalIdToDelete + "')");
                try { 
                    if (courseManager.removeCourse(finalIdToDelete)) {
                        lblMessage.setText("Xóa học phần '" + finalIdToDelete + "' thành công.");
                        System.out.println("    courseManager.removeCourse('" + finalIdToDelete + "') trả về true.");
                        clearForm();
                        showCourseList();
                    } else {
                        lblMessage.setText("Không tìm thấy học phần '" + finalIdToDelete + "' để xóa hoặc lỗi khi xóa (removeCourse trả về false).");
                        System.out.println("    courseManager.removeCourse('" + finalIdToDelete + "') trả về false.");
                    }
                } catch (Exception e) {
                    lblMessage.setText("Lỗi nghiêm trọng khi đang xóa học phần: " + e.getMessage());
                    System.err.println("CourseManagementController.handleDeleteCourse: NGOẠI LỆ NÉM RA TỪ courseManager.removeCourse('" + finalIdToDelete + "'):");
                    e.printStackTrace();
                }
            } else {
                System.out.println("CourseManagementController.handleDeleteCourse: Người dùng nhấn Cancel.");
            }
        });
    }

    private void handleFindCourse() {
         if (courseManager == null) {
             lblMessage.setText("Lỗi: CourseManager chưa được khởi tạo.");
             System.err.println("Lỗi tìm course: CourseManager là null.");
             return;
        }
        // Đảm bảo ID có thể nhập được khi tìm
        txtCourseID.setEditable(true); 
        txtCourseID.requestFocus(); // Focus để người dùng nhập

        String id = txtCourseID.getText().trim().toUpperCase();
        if (id.isEmpty()) {
            lblMessage.setText("Vui lòng nhập mã học phần để tìm.");
            return;
        }
        System.out.println("CourseManagementController.handleFindCourse: Tìm kiếm ID: " + id);
        Course c = courseManager.findCourse(id);
        if (c != null) {
            populateFormWithCourse(c);
            lblMessage.setText("Tìm thấy: " + c.getCourseName());
            listCourses.getSelectionModel().select(c); 
            listCourses.scrollTo(c); 
        } else {
            lblMessage.setText("Không tìm thấy học phần '" + id + "'.");
            // Không clear form khi không tìm thấy để người dùng có thể sửa lại ID tìm kiếm
        }
    }
    
    private void populateFormWithCourse(Course course) {
        if (course == null) {
            // clearForm(); // Không nên gọi clearForm() ở đây vì nó sẽ clear selection và gây vòng lặp với listener
            txtCourseID.clear();
            txtCourseName.clear();
            txtCredits.clear();
            txtPrerequisite.clear();
            txtFinalWeight.clear();
            txtCourseID.setEditable(true); // Cho phép nhập nếu không có course
            return;
        }
        txtCourseID.setText(course.getCourseID());
        txtCourseName.setText(course.getCourseName());
        txtCredits.setText(String.valueOf(course.getCreditCount()));
        txtPrerequisite.setText(course.getPreCourseID() == null ? "" : course.getPreCourseID());
        txtFinalWeight.setText(String.valueOf(course.getFinalWeight()));
        txtCourseID.setEditable(false); // Không cho sửa ID khi course đã được tải lên form
    }

    private void handleEditCourse() {
        if (courseManager == null) {
             lblMessage.setText("Lỗi: CourseManager chưa được khởi tạo.");
             System.err.println("Lỗi sửa course: CourseManager là null.");
             return;
        }
        
        String id = txtCourseID.getText().trim().toUpperCase(); 
        if (id.isEmpty() || txtCourseID.isEditable()) { 
            lblMessage.setText("Vui lòng tìm hoặc chọn một học phần từ danh sách để sửa.");
            return;
        }
        
        Course oldCourse = courseManager.findCourse(id); 
        if (oldCourse == null) {
             lblMessage.setText("Không tìm thấy học phần có mã '" + id + "' trong hệ thống để sửa. Vui lòng làm mới danh sách.");
             return;
        }

        String newName = txtCourseName.getText().trim();
        String newPre = txtPrerequisite.getText().trim();
        if (newPre.isEmpty()) newPre = null;
        String newFinalWeightStr = txtFinalWeight.getText().trim();
        int newCredits;
        double newFinalWeightValue;

        try {
            newCredits = Integer.parseInt(txtCredits.getText().trim());
             if (newCredits <= 0) {
                lblMessage.setText("Số tín chỉ cập nhật phải là số dương.");
                return;
            }
        } catch (NumberFormatException ex) {
            lblMessage.setText("Số tín chỉ cập nhật phải là một số nguyên.");
            return;
        }
        try {
            newFinalWeightValue = Double.parseDouble(newFinalWeightStr);
            if (newFinalWeightValue < 0.0 || newFinalWeightValue > 1.0) {
                lblMessage.setText("Trọng số điểm cuối kỳ cập nhật phải từ 0.0 đến 1.0.");
                return;
            }
        } catch (NumberFormatException ex) {
            lblMessage.setText("Trọng số điểm cuối kỳ cập nhật phải là một số.");
            return;
        }
        
        if (newName.isEmpty()) {
            lblMessage.setText("Tên học phần cập nhật không được để trống.");
            return;
        }

        Course newCourse = new Course(id, newName, newCredits, newPre, newFinalWeightValue);
        System.out.println("CourseManagementController.handleEditCourse: Chuẩn bị sửa ID '" + id + "' với dữ liệu mới.");

        if (courseManager.editCourse(id, newCourse)) { 
            lblMessage.setText("Cập nhật học phần '" + id + "' thành công.");
            int selectedIndex = listCourses.getSelectionModel().getSelectedIndex(); 
            showCourseList(); 
            // Cố gắng chọn lại item sau khi danh sách được làm mới
            if (selectedIndex != -1 && selectedIndex < listCourses.getItems().size()) {
                 // Cần tìm lại đối tượng newCourse trong danh sách mới vì listCourses được tạo lại
                 Course reSelectedCourse = listCourses.getItems().stream()
                                            .filter(c -> c.getCourseID().equals(id))
                                            .findFirst().orElse(null);
                 if (reSelectedCourse != null) {
                    listCourses.getSelectionModel().select(reSelectedCourse);
                    listCourses.scrollTo(reSelectedCourse);
                 }
            } else if (!listCourses.getItems().isEmpty()) {
                 listCourses.getSelectionModel().selectFirst();
            }
        } else {
            lblMessage.setText("Cập nhật học phần '" + id + "' thất bại (kiểm tra Console từ CourseManager).");
        }
    }

    private void handleOpenCourseListWindow() {
        if (courseManager == null) {
            lblMessage.setText("Lỗi: CourseManager chưa được khởi tạo để mở danh sách.");
            System.err.println("Lỗi mở CourseList window: CourseManager là null.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/CourseList.fxml"));
            Parent root = loader.load();
            CourseListController controller = loader.getController();
            controller.setCourseManager(courseManager);

            Stage dialogStage = new Stage(); // Đổi tên biến để tránh nhầm lẫn với stage của cửa sổ chính
            dialogStage.setTitle("Danh sách tất cả Học phần");
            dialogStage.setScene(new Scene(root));
            dialogStage.initOwner(btnList.getScene().getWindow()); 
            dialogStage.initModality(Modality.WINDOW_MODAL); 
            dialogStage.showAndWait(); 
        } catch (IOException ex) {
            ex.printStackTrace();
            lblMessage.setText("Lỗi: Không thể mở cửa sổ danh sách học phần.");
        }
    }

    @FXML
    private void handleBackToMainMenu(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/MainMenu.fxml"));
            Parent root = loader.load();

            // Truyền lại manager về menu (không ghi đè null nhờ initManagers kiểu merge)
            MainMenuController main = loader.getController();
            if (main != null) {
                main.initManagers(null, courseManager, null, null, null);
            }

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            // GIỮ kích thước/trạng thái: ưu tiên thay root nếu Scene đã tồn tại
            if (stage.getScene() == null) stage.setScene(new Scene(root));
            else stage.getScene().setRoot(root);

            // đảm bảo vẫn full screen / maximize
            if (stage.isFullScreen()) stage.setFullScreen(true);
            else stage.setMaximized(true);

            stage.setTitle("SSMS - Main Menu");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            // TODO: show alert nếu muốn
        }
    }

    private void showCourseList() {
        System.out.println("CourseManagementController: showCourseList() được gọi.");
        if (courseManager != null) {
            List<Course> coursesFromManager = courseManager.getAllCourses();
            System.out.println("  Số học phần lấy từ manager: " + (coursesFromManager != null ? coursesFromManager.size() : "null list"));
            
            if (coursesFromManager != null && !coursesFromManager.isEmpty()) {
                 System.out.println("  Học phần đầu tiên: " + coursesFromManager.get(0).getCourseName());
            } else if (coursesFromManager == null) {
                 System.err.println("  LỖI: courseManager.getAllCourses() trả về null!");
            }
            
            ObservableList<Course> coursesObservable;
            if (coursesFromManager != null) {
                coursesObservable = FXCollections.observableArrayList(coursesFromManager);
            } else {
                coursesObservable = FXCollections.observableArrayList(); 
            }
            listCourses.setItems(coursesObservable);
            System.out.println("  Đã đặt " + listCourses.getItems().size() + " học phần vào ListView.");
            
            listCourses.getSelectionModel().clearSelection();
            lblMessage.setText("Sẵn sàng ("+ listCourses.getItems().size() +" học phần)."); 
            // txtCourseID.setEditable(true); // Không nên set ở đây vì có thể ghi đè logic của listener
        } else {
            System.out.println("  CourseManager là null, không thể hiển thị danh sách.");
            listCourses.setItems(FXCollections.observableArrayList());
            lblMessage.setText("Chưa có dữ liệu học phần (CourseManager chưa được thiết lập).");
        }
    }

    private void clearForm() {
        txtCourseID.clear();
        txtCourseName.clear();
        txtCredits.clear();
        txtPrerequisite.clear();
        txtFinalWeight.clear();
        lblMessage.setText("Form đã được làm trống. Sẵn sàng nhập mới hoặc tìm kiếm.");
        txtCourseID.setEditable(true); 
        listCourses.getSelectionModel().clearSelection(); 
        txtCourseID.requestFocus(); 
    }

    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
        System.out.println("CourseManagementController: setCourseManager() được gọi.");
        if (this.courseManager != null) {
            System.out.println("  CourseManager đã được thiết lập. Số học phần: " + (this.courseManager.getAllCourses() != null ? this.courseManager.getAllCourses().size() : "null list"));
            showCourseList();
        } else {
            System.err.println("  LỖI: CourseManager được truyền vào là null trong setCourseManager().");
            showCourseList(); 
        }
        
        listCourses.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFormWithCourse(newSelection);
                lblMessage.setText("Đã chọn: " + newSelection.getCourseName());
                // txtCourseID.setEditable(false); // Đã được xử lý trong populateFormWithCourse
            } else {
                // Khi không có gì được chọn, chỉ cho phép sửa ID, không xóa form hoàn toàn
                // trừ khi người dùng nhấn nút "Làm trống Form"
                txtCourseID.setEditable(true); 
                // Không gọi clearForm() ở đây để tránh xóa trắng khi người dùng chỉ bỏ chọn
            }
        });
    }
}