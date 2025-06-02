package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Manager.CourseManager;
import Manager.ProgramManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox; // Đảm bảo import VBox
import javafx.stage.Stage;
import model.Course;
import model.Program;
import model.ProgramType;

public class AddEditProgramDialogController {

	@FXML
	private Label lblDialogTitle;
	@FXML
	private TextField txtMajorName;
	@FXML
	private ComboBox<ProgramType> comboProgramType;
	@FXML
	private Label lblElectiveCreditReq;
	@FXML
	private TextField txtElectiveCreditReq;
	@FXML
	private Label lblTotalCreditReq;
	@FXML
	private TextField txtTotalCreditReq;

	@FXML
	private ListView<String> listRequiredCourses;
	@FXML
	private TextField txtRequiredCourseIdInput;
	// Các nút đã có onAction trong FXML, không cần @FXML ở đây trừ khi bạn muốn
	// truy cập trực tiếp
	// @FXML private Button btnAddRequiredCourse;
	// @FXML private Button btnRemoveRequiredCourse;

	@FXML
	private VBox electiveCoursesSection; // fx:id cho VBox chứa phần tự chọn
	@FXML
	private ListView<String> listElectiveCourses;
	@FXML
	private TextField txtElectiveCourseIdInput;
	// @FXML private Button btnAddElectiveCourse;
	// @FXML private Button btnRemoveElectiveCourse;

	@FXML
	private Label lblValidationMessage;

	// @FXML private Button btnSave;
	// @FXML private Button btnCancel;

	private Stage dialogStage;
	private ProgramManager programManager;
	private CourseManager courseManager;
	private Program editingProgram;
	private boolean isEditMode = false;
	private String originalMajorNameForEditMode = null;

	private ObservableList<String> observableRequiredCourses = FXCollections.observableArrayList();
	private ObservableList<String> observableElectiveCourses = FXCollections.observableArrayList();

	private List<Course> actualRequiredCourses = new ArrayList<>();
	private List<Course> actualElectiveCourses = new ArrayList<>();

	@FXML
	public void initialize() {
		comboProgramType.setItems(FXCollections.observableArrayList(ProgramType.values()));
		comboProgramType.valueProperty().addListener((obs, oldType, newType) -> {
			if (newType != null) {
				updateUIBasedOnProgramType(newType);
			}
		});

		listRequiredCourses.setItems(observableRequiredCourses);
		listElectiveCourses.setItems(observableElectiveCourses);

		lblValidationMessage.setText("");
		// Mặc định ban đầu khi chưa setProgram
		updateUIBasedOnProgramType(null);
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setManagers(ProgramManager programManager, CourseManager courseManager) {
		this.programManager = programManager;
		this.courseManager = courseManager;
	}

	public void setProgram(Program program) {
		this.editingProgram = program;
		if (program != null) {
			isEditMode = true;
			originalMajorNameForEditMode = program.getMajorName();
			lblDialogTitle.setText("Sửa thông tin Chương trình Đào tạo");
			txtMajorName.setText(program.getMajorName());

			comboProgramType.setValue(program.getType());
			comboProgramType.setDisable(true);

			actualRequiredCourses.clear();
			if (program.getRequiredCourses() != null) {
				actualRequiredCourses.addAll(program.getRequiredCourses());
			}
			actualElectiveCourses.clear();
			if (program.getElectiveCourses() != null) {
				actualElectiveCourses.addAll(program.getElectiveCourses());
			}
			updateCourseListViews();
			updateUIBasedOnProgramType(program.getType());

			if (program.getType() == ProgramType.CREDIT_BASED) {
				txtElectiveCreditReq.setText(String.valueOf(program.getElectiveCreditRequirement()));
				txtTotalCreditReq.setText(String.valueOf(program.getTotalCreditRequirement()));
			}
		} else {
			isEditMode = false;
			originalMajorNameForEditMode = null;
			lblDialogTitle.setText("Thêm Chương trình Đào tạo Mới");
			txtMajorName.clear();
			txtElectiveCreditReq.clear();
			txtTotalCreditReq.clear();
			comboProgramType.setDisable(false);
			if (!comboProgramType.getItems().isEmpty()) {
				comboProgramType.getSelectionModel().selectFirst();
			} else {
				updateUIBasedOnProgramType(null);
			}
			actualRequiredCourses.clear();
			actualElectiveCourses.clear();
			updateCourseListViews();
		}
	}

	private void updateCourseListViews() {
		observableRequiredCourses.setAll(actualRequiredCourses.stream()
				.map(c -> c.getCourseID() + " - " + c.getCourseName() + " (" + c.getCreditCount() + "TC)")
				.collect(Collectors.toList()));
		observableElectiveCourses.setAll(actualElectiveCourses.stream()
				.map(c -> c.getCourseID() + " - " + c.getCourseName() + " (" + c.getCreditCount() + "TC)")
				.collect(Collectors.toList()));
	}

	private void updateUIBasedOnProgramType(ProgramType selectedType) {
		boolean isCreditBased = (selectedType == ProgramType.CREDIT_BASED);

		lblElectiveCreditReq.setVisible(isCreditBased);
		txtElectiveCreditReq.setVisible(isCreditBased);
		lblTotalCreditReq.setVisible(isCreditBased);
		txtTotalCreditReq.setVisible(isCreditBased);

		if (electiveCoursesSection != null) { // Đảm bảo FXML đã inject
			electiveCoursesSection.setVisible(isCreditBased);
			electiveCoursesSection.setManaged(isCreditBased);
		}

		if (!isCreditBased) {
			txtElectiveCreditReq.clear();
			txtTotalCreditReq.clear();
			actualElectiveCourses.clear();
			updateCourseListViews(); // Cập nhật ListView tự chọn (sẽ rỗng)
		}
	}

	@FXML
	private void handleAddRequiredCourse() {
		addCourseToList(txtRequiredCourseIdInput.getText(), actualRequiredCourses, "Bắt buộc");
		txtRequiredCourseIdInput.clear();
	}

	@FXML
	private void handleRemoveRequiredCourse() {
		removeCourseFromList(listRequiredCourses.getSelectionModel().getSelectedItem(), actualRequiredCourses);
	}

	@FXML
	private void handleAddElectiveCourse() {
		if (comboProgramType.getValue() == ProgramType.CREDIT_BASED && electiveCoursesSection.isVisible()) {
			addCourseToList(txtElectiveCourseIdInput.getText(), actualElectiveCourses, "Tự chọn");
			txtElectiveCourseIdInput.clear();
		} else {
			lblValidationMessage.setText("Chỉ chương trình Tín chỉ mới có học phần tự chọn.");
		}
	}

	@FXML
	private void handleRemoveElectiveCourse() {
		if (comboProgramType.getValue() == ProgramType.CREDIT_BASED && electiveCoursesSection.isVisible()) {
			removeCourseFromList(listElectiveCourses.getSelectionModel().getSelectedItem(), actualElectiveCourses);
		}
	}

	private void addCourseToList(String courseIdText, List<Course> actualList, String listTypeForMessage) {
		lblValidationMessage.setText("");
		String courseId = courseIdText.trim().toUpperCase();
		if (courseId.isEmpty()) {
			lblValidationMessage.setText("Vui lòng nhập Mã học phần.");
			return;
		}
		if (courseManager == null) {
			lblValidationMessage.setText("Lỗi: CourseManager chưa được khởi tạo.");
			return;
		}
		Course course = courseManager.findCourse(courseId);
		if (course == null) {
			lblValidationMessage.setText("Mã học phần '" + courseId + "' không tồn tại.");
			return;
		}

		if (actualRequiredCourses.stream().anyMatch(c -> c.getCourseID().equalsIgnoreCase(courseId))
				|| actualElectiveCourses.stream().anyMatch(c -> c.getCourseID().equalsIgnoreCase(courseId))) {
			lblValidationMessage.setText("Học phần '" + courseId + "' đã có trong một trong các danh sách.");
			return;
		}

		actualList.add(course);
		updateCourseListViews();
		lblValidationMessage.setText("Đã thêm HP '" + courseId + "' vào ds " + listTypeForMessage + ".");
	}

	private void removeCourseFromList(String selectedCourseString, List<Course> actualList) {
		lblValidationMessage.setText("");
		if (selectedCourseString == null || selectedCourseString.isEmpty()) {
			lblValidationMessage.setText("Vui lòng chọn một học phần để xóa từ danh sách.");
			return;
		}
		String courseIdToRemove = selectedCourseString.split(" - ")[0];
		boolean removed = actualList.removeIf(course -> course.getCourseID().equalsIgnoreCase(courseIdToRemove));
		if (removed) {
			updateCourseListViews();
			lblValidationMessage.setText("Đã xóa HP '" + courseIdToRemove + "'.");
		}
	}

	@FXML
	private void handleSave() {
		lblValidationMessage.setText("");
		String majorName = txtMajorName.getText().trim();
		ProgramType type = comboProgramType.getValue();

		if (majorName.isEmpty() || type == null) {
			lblValidationMessage.setText("Tên ngành và Loại chương trình không được để trống.");
			return;
		}

		if ((!isEditMode && programManager.findProgram(majorName) != null)
				|| (isEditMode && !originalMajorNameForEditMode.equalsIgnoreCase(majorName)
						&& programManager.findProgram(majorName) != null)) {
			lblValidationMessage.setText("Tên ngành '" + majorName + "' đã tồn tại hoặc bị trùng khi đổi tên.");
			return;
		}

		int electiveCreditReq = 0;
		int totalCreditReq = 0;

		if (type == ProgramType.CREDIT_BASED) {
			if (txtElectiveCreditReq.getText().trim().isEmpty() || txtTotalCreditReq.getText().trim().isEmpty()) {
				lblValidationMessage
						.setText("Với hệ Tín chỉ, vui lòng nhập đủ Tín Chỉ Tự Chọn Yêu Cầu và Tổng Tín Chỉ Yêu Cầu.");
				return;
			}
			try {
				electiveCreditReq = Integer.parseInt(txtElectiveCreditReq.getText().trim());
				totalCreditReq = Integer.parseInt(txtTotalCreditReq.getText().trim());
				if (electiveCreditReq < 0 || totalCreditReq < 0) {
					lblValidationMessage.setText("Số Tín Chỉ Tự Chọn Yêu Cầu hoặc Tổng Tín Chỉ Yêu Cầu không được âm.");
					return;
				}
				if (electiveCreditReq > totalCreditReq) {
					lblValidationMessage.setText("Tín Chỉ Tự Chọn Yêu Cầu không thể lớn hơn Tổng Tín Chỉ Yêu Cầu.");
					return;
				}
				if (actualRequiredCourses.isEmpty() && actualElectiveCourses.isEmpty() && totalCreditReq > 0) {
					lblValidationMessage.setText("Chương trình tín chỉ cần có học phần nếu Tổng Tín Chỉ Yêu Cầu > 0.");
					return;
				}
				if (actualRequiredCourses.isEmpty() && totalCreditReq > 0 && electiveCreditReq < totalCreditReq) {
					lblValidationMessage.setText(
							"Chương trình tín chỉ cần có học phần bắt buộc nếu Tổng Tín Chỉ Yêu Cầu > Tín Chỉ Tự Chọn Yêu Cầu.");
					return;
				}
			} catch (NumberFormatException e) {
				lblValidationMessage.setText("Số tín chỉ yêu cầu phải là số nguyên.");
				return;
			}
		}

		List<Course> finalRequiredCourses = new ArrayList<>(actualRequiredCourses);
		List<Course> finalElectiveCourses = (type == ProgramType.CREDIT_BASED) ? new ArrayList<>(actualElectiveCourses)
				: new ArrayList<>();

		if (type == ProgramType.PART_TIME) {
			int sumReqCredits = finalRequiredCourses.stream().mapToInt(Course::getCreditCount).sum();
			totalCreditReq = sumReqCredits;
			electiveCreditReq = 0;
			if (finalRequiredCourses.isEmpty()) {
				lblValidationMessage.setText("Chương trình Tại chức/Niên chế phải có ít nhất 1 học phần bắt buộc.");
				return;
			}
		} else { // CREDIT_BASED
			int sumReqCreditsFromList = finalRequiredCourses.stream().mapToInt(Course::getCreditCount).sum();
			int sumElecCreditsFromList = finalElectiveCourses.stream().mapToInt(Course::getCreditCount).sum();

			int expectedReqFromInput = totalCreditReq - electiveCreditReq;
			if (sumReqCreditsFromList != expectedReqFromInput) {
				lblValidationMessage.setText("Tổng Tín Chỉ Bắt Buộc (" + sumReqCreditsFromList
						+ ") không khớp (Tổng Tín Chỉ Yêu Cầu - Tín Chỉ Tự Chọn Yêu Cầu = " + expectedReqFromInput
						+ ").");
				return;
			}
			if (sumElecCreditsFromList < electiveCreditReq) {
				lblValidationMessage.setText("Tổng Tín Chỉ Tự Chọn trong danh sách (" + sumElecCreditsFromList
						+ ") nhỏ hơn Tín Chỉ Tự Chọn Yêu Cầu (" + electiveCreditReq + ").");
				return;
			}
		}

		boolean successOperation;
		if (isEditMode) {
			editingProgram.setMajorName(majorName);
			// editingProgram.setType(type); // Loại chương trình không đổi khi sửa
			editingProgram.setRequiredCourses(finalRequiredCourses);
			editingProgram.setElectiveCourses(finalElectiveCourses);
			editingProgram.setElectiveCreditRequirement(electiveCreditReq);
			editingProgram.setTotalCreditRequirement(totalCreditReq);

			successOperation = programManager.editProgram(originalMajorNameForEditMode, editingProgram);
			if (successOperation) {
				showAlert(Alert.AlertType.INFORMATION, "Thành công",
						"Cập nhật chương trình '" + majorName + "' thành công.");
			} else {
				showAlert(Alert.AlertType.ERROR, "Lỗi",
						"Cập nhật chương trình thất bại. Kiểm tra Console để biết thêm chi tiết.");
				return;
			}
		} else {
			Program newProgram = new Program(majorName, type, finalRequiredCourses, finalElectiveCourses,
					electiveCreditReq, totalCreditReq);
			successOperation = programManager.addProgram(newProgram);
			if (successOperation) {
				showAlert(Alert.AlertType.INFORMATION, "Thành công",
						"Thêm chương trình mới '" + majorName + "' thành công.");
			} else {
				showAlert(Alert.AlertType.ERROR, "Lỗi", "Thêm chương trình mới thất bại (có thể do trùng tên).");
				return;
			}
		}
		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private void showAlert(Alert.AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}