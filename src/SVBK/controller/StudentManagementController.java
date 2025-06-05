package SVBK.controller;

import java.io.IOException;
import java.util.List;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StudentManagementController {

	@FXML
	private TextField txtStudentID;
	@FXML
	private TextField txtStudentName;
	@FXML
	private ComboBox<String> comboStudentType;
	@FXML
	private ComboBox<String> comboMajorName;
	@FXML
	private ListView<Student> listViewStudents;

	@FXML
	private Button btnAdd;
	@FXML
	private Button btnRemove;
	@FXML
	private Button btnFind;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnLoadAll;
	@FXML
	private Button btnBackToMenu;

	// === CÁC THÀNH PHẦN UI MỚI CHO CHI TIẾT VÀ ĐĂNG KÝ HP ===
	@FXML
	private Label lblStudentProgramName;
	@FXML
	private Label lblStudentCPA;
	@FXML
	private TextField txtCourseToEnroll;
	@FXML
	private Button btnEnrollCourse;
	@FXML
	private ListView<String> listInProgressEnrollments;
	@FXML
	private ListView<String> listCompletedEnrollments;
	// === KẾT THÚC KHAI BÁO UI MỚI ===

	private ObservableList<Student> observableStudents = FXCollections.observableArrayList();
	// Dùng một ObservableList riêng để hiển thị kết quả tìm kiếm/lọc,
	// hoặc để thay thế nội dung của observableStudents chính.
	// Cách tốt nhất là luôn thao tác trên observableStudents chính.

	private List<Program> programList;

	private StudentManager studentManager;
	private ProgramManager programManager;
	private CourseManager courseManager;
	private GradingSystem gradingSystem;

	@FXML
	public void initialize() {
		System.out.println("StudentManagementController: initialize() được gọi.");
		listViewStudents.setItems(observableStudents); // Đảm bảo ListView luôn liên kết với observableStudents
		// Sửa đổi ở đây: Thay đổi "Tín chỉ" và "Niên chế" thành "Credit-based" và
		// "Part-time"
		comboStudentType.setItems(FXCollections.observableArrayList("Credit-based", "Part-time"));

		btnAdd.setOnAction(e -> addStudent());
		btnRemove.setOnAction(e -> removeStudent());
		btnFind.setOnAction(e -> findStudent());
		btnEdit.setOnAction(e -> editStudent());
		btnLoadAll.setOnAction(e -> loadAllStudents());
		btnBackToMenu.setOnAction(e -> backToMainMenu());

		if (btnEnrollCourse != null) {
			btnEnrollCourse.setOnAction(e -> handleEnrollCourse());
		}

		listViewStudents.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				displayStudentDetails(newVal);
				txtStudentID.setEditable(true); // Cho phép sửa ID khi chọn SV
				// Vô hiệu hóa ComboBox khi sinh viên được chọn (chế độ sửa)
				comboStudentType.setDisable(true);
				comboMajorName.setDisable(true);
				btnAdd.setDisable(true); // Vô hiệu hóa nút Add khi đang chọn để sửa
			} else {
				clearStudentDetailsSection();
				txtStudentID.setEditable(true); // Luôn cho phép sửa ID khi không có SV nào được chọn (chế độ thêm/tìm
												// kiếm)
				// Kích hoạt lại ComboBox khi không có sinh viên nào được chọn (chế độ thêm mới)
				comboStudentType.setDisable(false);
				comboMajorName.setDisable(false);
				btnAdd.setDisable(false); // Kích hoạt lại nút Add
			}
		});
	}

	public void initManagersAndData(ProgramManager programManager, StudentManager studentManager,
			CourseManager courseManager, GradingSystem gradingSystem) {
		System.out.println("StudentManagementController: initManagersAndData() được gọi.");
		this.programManager = programManager;
		this.studentManager = studentManager;
		this.courseManager = courseManager;
		this.gradingSystem = gradingSystem;

		if (this.programManager != null) {
			System.out.println(
					"  ProgramManager đã nhận: " + this.programManager.getAllPrograms().size() + " chương trình.");
			loadProgramDataForComboBox();
		} else {
			showErrorAlert("Lỗi cấu hình", "ProgramManager chưa được cung cấp cho StudentManagementController.");
		}

		if (this.studentManager != null) {
			System.out.println(
					"  StudentManager đã nhận: " + this.studentManager.getAllStudents().size() + " sinh viên.");
			loadAllStudents(); // Tải tất cả sinh viên khi khởi tạo
		} else {
			showErrorAlert("Lỗi cấu hình", "StudentManager chưa được cung cấp cho StudentManagementController.");
		}

		if (this.courseManager == null)
			System.err.println("  WARNING: CourseManager là null trong StudentManagementController!");
		if (this.gradingSystem == null)
			System.err.println("  WARNING: GradingSystem là null trong StudentManagementController!");

		clearStudentDetailsSection();
	}

	private void loadProgramDataForComboBox() {
		if (this.programManager == null)
			return;
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
		// Giá trị hiển thị trong ComboBox giờ khớp với giá trị từ model
		comboStudentType.setValue(student.getStudentType());

		Program studProgram = null;
		if (student instanceof CreditBasedStudent) {
			studProgram = ((CreditBasedStudent) student).getProgram();
		} else if (student instanceof PartTimeStudent) {
			studProgram = ((PartTimeStudent) student).getProgram();
		}

		if (studProgram != null) {
			comboMajorName.setValue(studProgram.getMajorName()); // Vẫn set giá trị để hiển thị đúng
			if (lblStudentProgramName != null)
				lblStudentProgramName.setText(studProgram.getMajorName());
		} else {
			comboMajorName.setValue(null);
			if (lblStudentProgramName != null)
				lblStudentProgramName.setText("N/A");
		}

		if (gradingSystem != null && lblStudentCPA != null) {
			double cpa = gradingSystem.calculateCPA(student);
			lblStudentCPA.setText(String.format("%.2f", cpa));
			System.out.println("  CPA của SV " + student.getStudentID() + ": " + cpa);
		} else if (lblStudentCPA != null) {
			lblStudentCPA.setText("N/A");
		}

		if (listInProgressEnrollments != null) {
			List<String> inProgress = student.getInProgressEnrollments().stream().map(Enrollment::toString)
					.collect(Collectors.toList());
			listInProgressEnrollments.setItems(FXCollections.observableArrayList(inProgress));
			System.out.println("  Số môn đang học: " + inProgress.size());
		}
		if (listCompletedEnrollments != null) {
			List<String> completed = student.getCompletedEnrollments().stream().map(Enrollment::toString)
					.collect(Collectors.toList());
			listCompletedEnrollments.setItems(FXCollections.observableArrayList(completed));
			System.out.println("  Số môn đã hoàn thành: " + completed.size());
		}
	}

	private void clearStudentDetailsSection() {
		if (lblStudentProgramName != null)
			lblStudentProgramName.setText("N/A");
		if (lblStudentCPA != null)
			lblStudentCPA.setText("N/A");
		if (txtCourseToEnroll != null)
			txtCourseToEnroll.clear();
		if (listInProgressEnrollments != null)
			listInProgressEnrollments.getItems().clear();
		if (listCompletedEnrollments != null)
			listCompletedEnrollments.getItems().clear();
		System.out.println("StudentManagementController: Đã xóa thông tin chi tiết sinh viên.");
	}

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

		String courseIdToEnroll = txtCourseToEnroll.getText().trim().toUpperCase();
		if (courseIdToEnroll.isEmpty()) {
			showErrorAlert("Thiếu thông tin", "Vui lòng nhập Mã học phần cần đăng ký.");
			return;
		}

		Course course = courseManager.findCourse(courseIdToEnroll);
		if (course == null) {
			showErrorAlert("Không tìm thấy Học phần", "Mã học phần '" + courseIdToEnroll + "' không tồn tại.");
			return;
		}

		System.out.println("StudentManagementController: SV '" + selectedStudent.getStudentID() + "' đăng ký HP '"
				+ course.getCourseID() + "'");
		if (studentManager.enrollCourse(selectedStudent.getStudentID(), course)) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Thông báo");
			alert.setHeaderText(null);
			alert.setContentText("Đăng ký học phần '" + course.getCourseName() + "' cho sinh viên '"
					+ selectedStudent.getStudentName() + "' thành công.");
			alert.showAndWait();

			txtCourseToEnroll.clear();
			displayStudentDetails(selectedStudent);
		} else {
			showErrorAlert("Đăng ký thất bại",
					"Không thể đăng ký học phần này. Lý do có thể là:\n" + "- Học phần đã được đăng ký/hoàn thành.\n"
							+ "- Chưa qua môn tiên quyết (nếu có).\n"
							+ "- Học phần không thuộc chương trình (đối với một số loại SV).");
		}
	}

	private void addStudent() {
		if (programManager == null || studentManager == null || programList == null) {
			showErrorAlert("Lỗi cấu hình", "Managers chưa sẵn sàng để thêm sinh viên.");
			return;
		}
		String id = txtStudentID.getText().trim();
		String name = txtStudentName.getText().trim();
		String typeString = comboStudentType.getValue(); // Lấy giá trị từ ComboBox (Credit-based/Part-time)
		String majorName = comboMajorName.getValue();

		if (id.isEmpty() || name.isEmpty() || typeString == null || majorName == null) {
			showErrorAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ Mã SV, Tên SV, chọn Loại SV và Ngành.");
			return;
		}

		if (studentManager.findStudent(id) != null) {
			showErrorAlert("Trùng Mã SV", "Mã sinh viên '" + id + "' đã tồn tại.");
			return;
		}

		Program selectedProgram = programList.stream().filter(p -> p.getMajorName().equals(majorName)).findFirst()
				.orElse(null);
		if (selectedProgram == null) {
			showErrorAlert("Lỗi Chương trình", "Chương trình đào tạo '" + majorName + "' không hợp lệ.");
			return;
		}

		Student newStudent = null;
		// So sánh với giá trị "Credit-based" hoặc "Part-time"
		if ("Credit-based".equals(typeString)) {
			newStudent = new CreditBasedStudent(id, name, selectedProgram);
		} else if ("Part-time".equals(typeString)) {
			newStudent = new PartTimeStudent(id, name, selectedProgram);
		} else {
			// Trường hợp này khó xảy ra nếu ComboBox được cài đặt đúng
			showErrorAlert("Lỗi Loại SV", "Loại sinh viên không hợp lệ.");
			return;
		}

		if (studentManager.addStudent(newStudent)) {
			loadAllStudents(); // Tải lại danh sách SV trên UI để hiển thị SV mới
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
		comboStudentType.setDisable(false); // Kích hoạt lại ComboBox
		comboMajorName.setDisable(false); // Kích hoạt lại ComboBox
		btnAdd.setDisable(false); // Kích hoạt lại nút Add
	}

	private void removeStudent() {
		Student selected = listViewStudents.getSelectionModel().getSelectedItem();
		String idToRemove;
		if (selected != null) {
			idToRemove = selected.getStudentID();
		} else {
			idToRemove = txtStudentID.getText().trim();
		}

		if (idToRemove.isEmpty()) { // Kiểm tra nếu cả chọn và nhập đều rỗng
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

		// Không cho phép đổi ID nếu ID mới đã tồn tại trong hệ thống
		if (!newID.equals(selectedStudent.getStudentID()) && studentManager.findStudent(newID) != null) {
			showErrorAlert("Trùng Mã SV", "Mã sinh viên '" + newID + "' đã tồn tại.");
			return;
		}

		// Tiến hành sửa đổi
		selectedStudent.setStudentID(newID);
		selectedStudent.setStudentName(newName);

		// Nếu cần đổi loại SV/chương trình, cần tạo lại đối tượng -> tránh làm ở đây
		// Vì đã disable comboStudentType và comboMajorName khi chọn SV

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Thành công");
		alert.setHeaderText(null);
		alert.setContentText("Thông tin sinh viên đã được cập nhật.");
		alert.showAndWait();

		loadAllStudents(); // Cập nhật lại danh sách
		clearFormInputs(); // Reset form
		listViewStudents.getSelectionModel().clearSelection(); // Bỏ chọn SV sau khi sửa
	}

	private void loadAllStudents() {
		if (studentManager == null) {
			System.err.println("StudentManagementController: loadAllStudents() được gọi nhưng studentManager là null.");
			if (observableStudents != null)
				observableStudents.clear();
			return;
		}
		List<Student> studentsFromManager = studentManager.getAllStudents();
		// Luôn sử dụng setAll để cập nhật nội dung của observableStudents
		observableStudents.setAll(studentsFromManager);
		System.out.println(
				"StudentManagementController: Đã tải " + observableStudents.size() + " sinh viên vào ListView.");
		clearStudentDetailsSection();
	}

	@FXML
	private void backToMainMenu() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/MainMenu.fxml"));
			Parent root = loader.load();
			Stage stage = (Stage) btnBackToMenu.getScene().getWindow();
			System.out.println("StudentManagementController: Quay lại MainMenu.");
			stage.setScene(new Scene(root));
			stage.setTitle("Menu Chính");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			showErrorAlert("Lỗi điều hướng", "Không thể quay về menu chính: " + e.getMessage());
		}
	}

	private void showErrorAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}