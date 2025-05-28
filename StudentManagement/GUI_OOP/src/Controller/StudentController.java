package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import SVBK.model.Course;
import SVBK.model.CreditBasedStudent;
import SVBK.model.PartTimeStudent;
import SVBK.model.Program;
import SVBK.model.ProgramType;
import SVBK.model.Student;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class StudentController {

	@FXML
	private TextField txtStudentID;

	@FXML
	private TextField txtStudentName;

	@FXML
	private ComboBox<String> comboMajorName;

	@FXML
	private ComboBox<String> comboProgramType; // "Credit-based" hoặc "Part-time"

	@FXML
	private Button btnAddStudent;

	@FXML
	private Button btnRemoveStudent;

	@FXML
	private Button btnFindStudent;

	@FXML
	private Button btnEditStudent;

	@FXML
	private Button btnLoadAllStudents;

	@FXML
	private Button btnBackToMenu;

	@FXML
	private ListView<Student> listViewStudents;

	// Danh sách chương trình đào tạo
	private List<Program> programList = new ArrayList<>();

	// Danh sách sinh viên quản lý
	private List<Student> studentList = new ArrayList<>();

	@FXML
	public void initialize() {
		initPrograms();

		comboMajorName.setItems(FXCollections
				.observableArrayList(programList.stream().map(Program::getMajorName).collect(Collectors.toList())));

		comboProgramType.setItems(FXCollections.observableArrayList("Credit-based", "Part-time"));

		if (!comboProgramType.getItems().isEmpty()) {
			comboProgramType.getSelectionModel().selectFirst();
		}
		if (!comboMajorName.getItems().isEmpty()) {
			comboMajorName.getSelectionModel().selectFirst();
		}

		btnAddStudent.setOnAction(e -> addStudent());
		btnLoadAllStudents.setOnAction(e -> loadAllStudents());

		btnBackToMenu.setOnAction(event -> {
			try {
				SVBK.app.MainApp.setRoot("/SVBK/fxml/MainMenu.fxml");
			} catch (Exception e) {
				e.printStackTrace();
				showAlert("Lỗi", "Không thể quay về menu chính.");
			}
		});

		// TODO: Cài đặt các sự kiện cho nút khác (btnRemoveStudent, btnFindStudent,
		// btnEditStudent)
	}

	private void initPrograms() {
		// Chương trình 1: CNTT - tín chỉ
		List<Course> reqCoursesCNTT = List.of(new Course("CS101", "Nhập môn CNTT", 3, "", 0.6),
				new Course("CS102", "Lập trình Java", 4, "CS101", 0.6),
				new Course("CS103", "Cấu trúc dữ liệu", 3, "CS102", 0.6));

		List<Course> elecCoursesCNTT = List.of(new Course("CS201", "Cơ sở dữ liệu", 3, "", 0.6),
				new Course("CS202", "Mạng máy tính", 3, "", 0.6), new Course("CS203", "Hệ điều hành", 3, "", 0.6));

		Program programCNTT = new Program("CNTT", // majorName
				ProgramType.CREDIT_BASED, // type
				reqCoursesCNTT, // requiredCourses
				elecCoursesCNTT, // electiveCourses
				12, // electiveCreditRequirement
				36 // totalCreditRequirement
		);

		// Chương trình 2: Kỹ thuật phần mềm - tín chỉ
		List<Course> reqCoursesSE = List.of(new Course("SE101", "Phân tích thiết kế phần mềm", 3, "", 0.6),
				new Course("SE102", "Lập trình hướng đối tượng", 4, "SE101", 0.6),
				new Course("SE103", "Kiểm thử phần mềm", 3, "SE102", 0.6));

		List<Course> elecCoursesSE = List.of(new Course("SE201", "Phát triển ứng dụng web", 3, "", 0.6),
				new Course("SE202", "Phát triển ứng dụng di động", 3, "", 0.6));

		Program programSE = new Program("Kỹ thuật phần mềm", ProgramType.CREDIT_BASED, reqCoursesSE, elecCoursesSE, 10,
				30);

		// Chương trình 3: Quản trị kinh doanh - niên chế (part-time)
		List<Course> reqCoursesBA = List.of(new Course("BA101", "Nhập môn quản trị", 3, "", 0.5),
				new Course("BA102", "Marketing căn bản", 3, "", 0.5),
				new Course("BA103", "Quản trị nhân sự", 3, "", 0.5));

		List<Course> elecCoursesBA = List.of(); // Niên chế không có học phần tự chọn

		Program programBA = new Program("Quản trị kinh doanh", ProgramType.PART_TIME, reqCoursesBA, elecCoursesBA, 0,
				0);

		// Thêm các chương trình vào danh sách
		programList.clear();
		programList.add(programCNTT);
		programList.add(programSE);
		programList.add(programBA);
	}

	private void addStudent() {
		String id = txtStudentID.getText().trim();
		String name = txtStudentName.getText().trim();
		String major = comboMajorName.getValue();
		String programType = comboProgramType.getValue();

		if (id.isEmpty() || name.isEmpty() || major == null || programType == null) {
			showAlert("Lỗi", "Vui lòng nhập đủ thông tin sinh viên và chọn ngành, loại chương trình.");
			return;
		}

		Program selectedProgram = programList.stream().filter(p -> p.getMajorName().equalsIgnoreCase(major)).findFirst()
				.orElse(null);

		if (selectedProgram == null) {
			showAlert("Lỗi", "Không tìm thấy chương trình đào tạo phù hợp.");
			return;
		}

		Student newStudent;

		if (programType.equalsIgnoreCase("Credit-based")) {
			if (selectedProgram.getType() != ProgramType.CREDIT_BASED) {
				showAlert("Lỗi", "Chương trình ngành không phải tín chỉ.");
				return;
			}
			newStudent = new CreditBasedStudent(id, name, selectedProgram);
		} else if (programType.equalsIgnoreCase("Part-time")) {
			if (selectedProgram.getType() != ProgramType.PART_TIME) {
				showAlert("Lỗi", "Chương trình ngành không phải niên chế.");
				return;
			}
			newStudent = new PartTimeStudent(id, name, selectedProgram);
		} else {
			showAlert("Lỗi", "Loại chương trình không hợp lệ.");
			return;
		}

		studentList.add(newStudent);
		showAlert("Thành công", "Đã thêm sinh viên mới: " + newStudent.getStudentName());
		clearForm();
		loadAllStudents();
	}

	private void loadAllStudents() {
		listViewStudents.setItems(FXCollections.observableArrayList(studentList));
	}

	private void clearForm() {
		txtStudentID.clear();
		txtStudentName.clear();
		comboMajorName.getSelectionModel().selectFirst();
		comboProgramType.getSelectionModel().selectFirst();
	}

	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
