package Controller;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.*;
import java.util.Optional;
import java.util.stream.Collectors;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class StudentManagementController {

    @FXML private TextField txtStudentID;
    @FXML private TextField txtStudentName;
    @FXML private ComboBox<String> comboStudentType;
    @FXML private ComboBox<String> comboMajorName;
    @FXML private ListView<Student> listViewStudents;

    @FXML private Button btnAdd;
    @FXML private Button btnRemove;
    @FXML private Button btnFind;
    @FXML private Button btnEdit;
    @FXML private Button btnLoadAll;
    @FXML private Button btnBackToMenu;

    // === UI chi tiết & đăng ký học phần ===
    @FXML private Label lblStudentProgramName;
    @FXML private Label lblStudentCPA;
    @FXML private TextField txtCourseToEnroll;     // Ô cần autocomplete
    @FXML private Button btnEnrollCourse;
    @FXML private ListView<String> listInProgressEnrollments;
    @FXML private ListView<String> listCompletedEnrollments;
    
    @FXML private Button btnBack;

    private final ObservableList<Student> observableStudents = FXCollections.observableArrayList();

    private List<Program> programList;

    private StudentManager studentManager;
    private ProgramManager programManager;
    private CourseManager courseManager;
    private GradingSystem gradingSystem;

    // ==== Autocomplete: popup & nguồn gợi ý theo SV đang chọn ====
    private final ContextMenu suggestionsMenu = new ContextMenu();
    private final ObservableList<String> suggestableCourseIds = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("StudentManagementController: initialize() được gọi.");
        listViewStudents.setItems(observableStudents);
        comboStudentType.setItems(FXCollections.observableArrayList("Credit-based", "Part-time"));

        btnAdd.setOnAction(e -> addStudent());
        btnRemove.setOnAction(e -> removeStudent());
        btnFind.setOnAction(e -> findStudent());
        btnEdit.setOnAction(e -> editStudent());
        btnLoadAll.setOnAction(e -> clearFormInputs());
        btnBackToMenu.setOnAction(e -> backToMain(e));

        if (btnEnrollCourse != null) {
            btnEnrollCourse.setOnAction(e -> handleEnrollCourse());
        }

        // Lựa chọn SV -> hiển thị chi tiết + rebuild gợi ý cho ô Mã HP
        listViewStudents.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                displayStudentDetails(newVal);
                rebuildSuggestionsForSelectedStudent(newVal);   // <<< xây lại danh sách gợi ý
                if (txtCourseToEnroll != null) txtCourseToEnroll.clear();
                suggestionsMenu.hide();

                txtStudentID.setEditable(true);
                comboStudentType.setDisable(true);
                comboMajorName.setDisable(true);
                btnAdd.setDisable(true);
            } else {
                clearStudentDetailsSection();
                suggestableCourseIds.clear();
                if (txtCourseToEnroll != null) txtCourseToEnroll.clear();
                suggestionsMenu.hide();

                txtStudentID.setEditable(true);
                comboStudentType.setDisable(false);
                comboMajorName.setDisable(false);
                btnAdd.setDisable(false);
            }
        });

        // Cài autocomplete cho ô txtCourseToEnroll
        installAutoCompleteForCourseId();
    }

    public void initManagersAndData(ProgramManager programManager, StudentManager studentManager,
                                    CourseManager courseManager, GradingSystem gradingSystem) {
        System.out.println("StudentManagementController: initManagersAndData() được gọi.");
        this.programManager = programManager;
        this.studentManager = studentManager;
        this.courseManager = courseManager;
        this.gradingSystem = gradingSystem;

        if (this.programManager != null) {
            System.out.println("  ProgramManager đã nhận: " + this.programManager.getAllPrograms().size() + " chương trình.");
            loadProgramDataForComboBox();
        } else {
            showErrorAlert("Lỗi cấu hình", "ProgramManager chưa được cung cấp cho StudentManagementController.");
        }

        if (this.studentManager != null) {
            System.out.println("  StudentManager đã nhận: " + this.studentManager.getAllStudents().size() + " sinh viên.");
            loadAllStudents();
        } else {
            showErrorAlert("Lỗi cấu hình", "StudentManager chưa được cung cấp cho StudentManagementController.");
        }

        if (this.courseManager == null) System.err.println("  WARNING: CourseManager là null trong StudentManagementController!");
        if (this.gradingSystem == null) System.err.println("  WARNING: GradingSystem là null trong StudentManagementController!");

        clearStudentDetailsSection();
        suggestableCourseIds.clear();
        suggestionsMenu.hide();
    }

    private void loadProgramDataForComboBox() {
        if (this.programManager == null) return;
        try {
            this.programList = programManager.getAllPrograms();
            ObservableList<String> majorNames = FXCollections.observableArrayList();
            if (this.programList != null) {
                for (Program p : this.programList) {
                    majorNames.add(p.getMajorName());
                }
            }
            comboMajorName.setItems(majorNames);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Lỗi", "Không thể tải danh sách chương trình đào tạo.");
        }
    }

    private void displayStudentDetails(Student student) {
        if (student == null) {
            clearStudentDetailsSection();
            return;
        }
        System.out.println("StudentManagementController: Hiển thị chi tiết cho SV: " + student.getStudentID());

        txtStudentID.setText(student.getStudentID());
        txtStudentName.setText(student.getStudentName());
        comboStudentType.setValue(student.getStudentType());

        Program studProgram = getProgramOf(student);

        if (studProgram != null) {
            comboMajorName.setValue(studProgram.getMajorName());
            if (lblStudentProgramName != null) lblStudentProgramName.setText(studProgram.getMajorName());
        } else {
            comboMajorName.setValue(null);
            if (lblStudentProgramName != null) lblStudentProgramName.setText("N/A");
        }

        if (gradingSystem != null && lblStudentCPA != null) {
            double cpa = gradingSystem.calculateCPA(student);
            lblStudentCPA.setText(String.format(Locale.US, "%.2f", cpa));
            System.out.println("  CPA của SV " + student.getStudentID() + ": " + cpa);
        } else if (lblStudentCPA != null) {
            lblStudentCPA.setText("N/A");
        }

        if (listInProgressEnrollments != null) {
            List<String> inProgress = student.getInProgressEnrollments().stream()
                    .map(Enrollment::toString)
                    .collect(Collectors.toList());
            listInProgressEnrollments.setItems(FXCollections.observableArrayList(inProgress));
            System.out.println("  Số môn đang học: " + inProgress.size());
        }
        if (listCompletedEnrollments != null) {
            List<String> completed = student.getCompletedEnrollments().stream()
                    .map(Enrollment::toString)
                    .collect(Collectors.toList());
            listCompletedEnrollments.setItems(FXCollections.observableArrayList(completed));
            System.out.println("  Số môn đã hoàn thành: " + completed.size());
        }
    }

    private void clearStudentDetailsSection() {
        if (lblStudentProgramName != null) lblStudentProgramName.setText("N/A");
        if (lblStudentCPA != null) lblStudentCPA.setText("N/A");
        if (txtCourseToEnroll != null) txtCourseToEnroll.clear();
        if (listInProgressEnrollments != null) listInProgressEnrollments.getItems().clear();
        if (listCompletedEnrollments != null) listCompletedEnrollments.getItems().clear();
        System.out.println("StudentManagementController: Đã xóa thông tin chi tiết sinh viên.");
    }

    // ====== AUTOCOMPLETE FOR txtCourseToEnroll ======

    /** Cài đặt lắng nghe gõ phím và hiển thị menu gợi ý */
    private void installAutoCompleteForCourseId() {
        if (txtCourseToEnroll == null) return;

        suggestionsMenu.setAutoHide(true);

        // Khi text thay đổi -> lọc lại gợi ý
        txtCourseToEnroll.textProperty().addListener((obs, oldText, newText) -> {
            showSuggestions(newText);
        });

        // Mất focus -> ẩn menu
        txtCourseToEnroll.focusedProperty().addListener((obs, was, is) -> {
            if (!is) suggestionsMenu.hide();
        });

        // Phím tắt: DOWN để bung menu, ESC để ẩn
        txtCourseToEnroll.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.DOWN) {
                if (!suggestionsMenu.isShowing()) showSuggestions(txtCourseToEnroll.getText());
            } else if (e.getCode() == KeyCode.ESCAPE) {
                suggestionsMenu.hide();
            }
        });
    }

    /** Xây lại danh sách courseID có thể gợi ý cho sinh viên đang chọn */
    private void rebuildSuggestionsForSelectedStudent(Student s) {
        suggestableCourseIds.clear();
        if (s == null) return;

        Program p = getProgramOf(s);
        if (p == null) return;

        // Tập course hợp lệ theo chương trình: Part-time -> chỉ required; Credit-based -> required + elective
        List<Course> baseCourses = new ArrayList<>(p.getRequiredCourses());
        if (s instanceof CreditBasedStudent) {
            baseCourses.addAll(p.getElectiveCourses());
        }

        // Loại bỏ các môn đã đăng ký (đang học/đã hoàn thành)
        Set<String> already = s.getEnrollments().stream()
                .map(e -> e.getCourse().getCourseID().toUpperCase(Locale.ROOT))
                .collect(Collectors.toSet());

        baseCourses.stream()
                .filter(Objects::nonNull)
                .map(c -> c.getCourseID().toUpperCase(Locale.ROOT))
                .filter(id -> !already.contains(id))
                .distinct()
                .sorted()
                .forEach(suggestableCourseIds::add);
    }

    /** Lọc và hiển thị popup gợi ý ngay dưới ô txtCourseToEnroll */
    private void showSuggestions(String queryRaw) {
        if (txtCourseToEnroll == null) return;
        String query = (queryRaw == null) ? "" : queryRaw.trim().toUpperCase(Locale.ROOT);

        if (query.isEmpty() || suggestableCourseIds.isEmpty()) {
            suggestionsMenu.hide();
            return;
        }

        List<String> matches = suggestableCourseIds.stream()
                .filter(id -> id.startsWith(query))
                .limit(10)
                .collect(Collectors.toList());

        if (matches.isEmpty()) {
            suggestionsMenu.hide();
            return;
        }

        List<CustomMenuItem> items = new ArrayList<>();
        for (String id : matches) {
            Label lbl = new Label(id);
            lbl.setMinWidth(240);
            lbl.setMinHeight(26);
            CustomMenuItem item = new CustomMenuItem(lbl, true);
            item.setOnAction(ev -> {
                txtCourseToEnroll.setText(id);
                txtCourseToEnroll.positionCaret(id.length());
                suggestionsMenu.hide();
            });
            items.add(item);
        }

        suggestionsMenu.getItems().setAll(items);

        if (!suggestionsMenu.isShowing()) {
            suggestionsMenu.show(txtCourseToEnroll, Side.BOTTOM, 0, 0);
        }
    }

    // ====== Đăng ký học phần ======

    @FXML
    private void handleEnrollCourse() {
        Student selectedStudent = listViewStudents.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showErrorAlert("Chưa chọn Sinh viên", "Vui lòng chọn một sinh viên từ danh sách để đăng ký học phần.");
            return;
        }
        if (txtCourseToEnroll == null || courseManager == null || studentManager == null) {
            showErrorAlert("Lỗi cấu hình",
                    "Một số thành phần cần thiết (CourseManager, StudentManager, UI) chưa sẵn sàng.");
            System.err.println("StudentManagementController.handleEnrollCourse: Manager hoặc UI component là null.");
            return;
        }

        String courseIdToEnroll = txtCourseToEnroll.getText().trim().toUpperCase(Locale.ROOT);
        if (courseIdToEnroll.isEmpty()) {
            showErrorAlert("Thiếu thông tin", "Vui lòng nhập Mã học phần cần đăng ký.");
            return;
        }

        Course course = courseManager.findCourse(courseIdToEnroll);
        if (course == null) {
            showErrorAlert("Không tìm thấy Học phần", "Mã học phần '" + courseIdToEnroll + "' không tồn tại.");
            return;
        }

        System.out.println("StudentManagementController: SV '" + selectedStudent.getStudentID()
                + "' đăng ký HP '" + course.getCourseID() + "'");
        if (studentManager.enrollCourse(selectedStudent.getStudentID(), course)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Đăng ký học phần '" + course.getCourseName() + "' cho sinh viên '"
                    + selectedStudent.getStudentName() + "' thành công.");
            alert.showAndWait();

            txtCourseToEnroll.clear();
            displayStudentDetails(selectedStudent);

            // Cập nhật lại danh sách gợi ý để loại môn vừa đăng ký
            rebuildSuggestionsForSelectedStudent(selectedStudent);
            suggestionsMenu.hide();

        } else {
            showErrorAlert("Đăng ký thất bại",
                    "Không thể đăng ký học phần này. Lý do có thể là:\n"
                            + "- Học phần đã được đăng ký/hoàn thành.\n"
                            + "- Chưa qua môn tiên quyết (nếu có).\n"
                            + "- Học phần không thuộc chương trình (đối với một số loại SV).");
        }
    }

    // ====== CRUD SV ======

    private void addStudent() {
        if (programManager == null || studentManager == null || programList == null) {
            showErrorAlert("Lỗi cấu hình", "Managers chưa sẵn sàng để thêm sinh viên.");
            return;
        }
        String id = txtStudentID.getText().trim();
        String name = txtStudentName.getText().trim();
        String typeString = comboStudentType.getValue();
        String majorName = comboMajorName.getValue();

        if (id.isEmpty() || name.isEmpty() || typeString == null || majorName == null) {
            showErrorAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ Mã SV, Tên SV, chọn Loại SV và Ngành.");
            return;
        }

        if (studentManager.findStudent(id) != null) {
            showErrorAlert("Trùng Mã SV", "Mã sinh viên '" + id + "' đã tồn tại.");
            return;
        }

        Program selectedProgram = programList.stream()
                .filter(p -> p.getMajorName().equals(majorName))
                .findFirst().orElse(null);
        if (selectedProgram == null) {
            showErrorAlert("Lỗi Chương trình", "Chương trình đào tạo '" + majorName + "' không hợp lệ.");
            return;
        }

        Student newStudent;
        if ("Credit-based".equals(typeString)) {
            newStudent = new CreditBasedStudent(id, name, selectedProgram);
        } else if ("Part-time".equals(typeString)) {
            newStudent = new PartTimeStudent(id, name, selectedProgram);
        } else {
            showErrorAlert("Lỗi Loại SV", "Loại sinh viên không hợp lệ.");
            return;
        }

        if (studentManager.addStudent(newStudent)) {
            loadAllStudents();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công");
            alert.setHeaderText(null);
            alert.setContentText("Thêm sinh viên '" + name + "' thành công.");
            alert.showAndWait();
            clearFormInputs();
        } else {
            showErrorAlert("Lỗi thêm SV", "Không thể thêm sinh viên vào hệ thống.");
        }
    }

    private void clearFormInputs() {
        txtStudentID.clear();
        txtStudentName.clear();
        comboStudentType.getSelectionModel().clearSelection();
        comboMajorName.getSelectionModel().clearSelection();

        txtStudentID.setEditable(true);
        comboStudentType.setDisable(false);
        comboMajorName.setDisable(false);
        btnAdd.setDisable(false);
    }


    private void removeStudent() {
        Student selected = listViewStudents.getSelectionModel().getSelectedItem();
        String idToRemove = (selected != null) ? selected.getStudentID() : txtStudentID.getText().trim();

        if (idToRemove.isEmpty()) {
            showErrorAlert("Chưa chọn/nhập SV", "Vui lòng chọn một sinh viên từ danh sách hoặc nhập Mã SV để xóa.");
            return;
        }

        if (studentManager == null) {
            showErrorAlert("Lỗi cấu hình", "StudentManager chưa được khởi tạo.");
            return;
        }

        Student studentToRemove = studentManager.findStudent(idToRemove);
        if (studentToRemove == null) {
            showErrorAlert("Không tìm thấy", "Không tìm thấy sinh viên với mã: " + idToRemove);
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Xác nhận xóa");
        confirmDialog.setHeaderText(
                "Bạn có chắc muốn xóa sinh viên: " + studentToRemove.getStudentName() + " (ID: " + idToRemove + ")?");
        confirmDialog.setContentText("Hành động này không thể hoàn tác.");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (studentManager.removeStudent(idToRemove)) {
                loadAllStudents();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công");
                alert.setHeaderText(null);
                alert.setContentText("Xóa sinh viên thành công.");
                alert.showAndWait();
                clearFormInputs();
                clearStudentDetailsSection();
                suggestableCourseIds.clear();
                suggestionsMenu.hide();
            } else {
                showErrorAlert("Lỗi xóa", "Không thể xóa sinh viên (có thể sinh viên không tồn tại theo ID vừa nhập).");
            }
        }
    }

    @FXML
    private void findStudent() {
        if (studentManager == null) {
            showErrorAlert("Lỗi cấu hình", "StudentManager chưa được khởi tạo.");
            return;
        }
        String id = txtStudentID.getText().trim();
        if (id.isEmpty()) {
            showErrorAlert("Thiếu thông tin", "Vui lòng nhập Mã sinh viên để tìm.");
            return;
        }
        Student s = studentManager.findStudent(id);
        if (s != null) {
            observableStudents.setAll(s);
            listViewStudents.getSelectionModel().selectFirst();
        } else {
            showErrorAlert("Thông báo", "Không tìm thấy sinh viên với mã: " + id);
            observableStudents.clear();
            clearStudentDetailsSection();
            suggestableCourseIds.clear();
            suggestionsMenu.hide();
        }
    }

    private void editStudent() {
        Student selectedStudent = listViewStudents.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showErrorAlert("Chưa chọn sinh viên", "Vui lòng chọn một sinh viên để sửa.");
            return;
        }

        String newID = txtStudentID.getText().trim();
        String newName = txtStudentName.getText().trim();

        if (newID.isEmpty() || newName.isEmpty()) {
            showErrorAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ Mã SV và Tên SV.");
            return;
        }

        if (!newID.equals(selectedStudent.getStudentID()) && studentManager.findStudent(newID) != null) {
            showErrorAlert("Trùng Mã SV", "Mã sinh viên '" + newID + "' đã tồn tại.");
            return;
        }

        selectedStudent.setStudentID(newID);
        selectedStudent.setStudentName(newName);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText("Thông tin sinh viên đã được cập nhật.");
        alert.showAndWait();

        loadAllStudents();
        clearFormInputs();
        listViewStudents.getSelectionModel().clearSelection();
        suggestableCourseIds.clear();
        suggestionsMenu.hide();
    }

    private void loadAllStudents() {
        if (studentManager == null) {
            System.err.println("StudentManagementController: loadAllStudents() được gọi nhưng studentManager là null.");
            if (observableStudents != null) observableStudents.clear();
            return;
        }
        List<Student> studentsFromManager = studentManager.getAllStudents();
        observableStudents.setAll(studentsFromManager);
        System.out.println("StudentManagementController: Đã tải " + observableStudents.size() + " sinh viên vào ListView.");
        clearStudentDetailsSection();
        suggestableCourseIds.clear();
        suggestionsMenu.hide();
    }
    @FXML
    private void handleClearFormOnly() {
        // 1) Xóa toàn bộ ô nhập & trạng thái nút như bạn đã viết
        clearFormInputs();

        // 2) (Khuyến nghị) Hủy chọn dòng đang chọn trong ListView/TableView nếu có
        if (listViewStudents != null) {
            listViewStudents.getSelectionModel().clearSelection();
        }
        // hoặc: tableView.getSelectionModel().clearSelection();

        // 3) (Tùy chọn) Xóa vùng chi tiết, gợi ý… nếu bạn đang dùng
        clearStudentDetailsSection();   // nếu có
        suggestableCourseIds.clear();   // nếu có
        if (suggestionsMenu != null) suggestionsMenu.hide();

        // 4) Đưa focus về ô Mã SV để sẵn sàng nhập mới
        txtStudentID.requestFocus();
    }


    @FXML
    private void backToMain(javafx.event.ActionEvent e) {
        try {
            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(getClass().getResource("/JavaFX/MainMenu.fxml"));
            javafx.scene.Parent root = loader.load();

           
            MainMenuController main = loader.getController();
            if (main != null) {
               
                main.initManagers(programManager, courseManager, studentManager, gradingSystem, null);
            }

            javafx.stage.Stage stage =
                    (javafx.stage.Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();

          
            if (stage.getScene() == null) stage.setScene(new javafx.scene.Scene(root));
            else stage.getScene().setRoot(root);

           
            if (stage.isFullScreen()) stage.setFullScreen(true);
            else stage.setMaximized(true);

            stage.setTitle("SSMS - Main Menu");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    // ===== Helpers =====

    private Program getProgramOf(Student student) {
        if (student instanceof CreditBasedStudent) return ((CreditBasedStudent) student).getProgram();
        if (student instanceof PartTimeStudent)   return ((PartTimeStudent) student).getProgram();
        return null;
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
