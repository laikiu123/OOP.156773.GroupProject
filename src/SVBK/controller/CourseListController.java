package SVBK.controller;

import SVBK.manager.CourseManager;
import SVBK.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List; // Thêm import cho List

public class CourseListController {

    @FXML
    private ListView<String> listCourses; // ListView này sẽ hiển thị String

    @FXML
    private Button btnClose; // Nút để đóng cửa sổ danh sách

    private CourseManager courseManager;

    /**
     * Phương thức này được gọi bởi FXML loader sau khi các trường @FXML đã được inject.
     * Tuy nhiên, việc tải dữ liệu sẽ chờ đến khi courseManager được thiết lập.
     */
    @FXML
    public void initialize() {
        System.out.println("CourseListController: initialize() được gọi.");
        // Không tải dữ liệu ở đây, chờ setCourseManager()
        // Gán sự kiện cho nút đóng
        if (btnClose != null) {
            btnClose.setOnAction(e -> handleClose());
        }
    }

    /**
     * Thiết lập CourseManager cho controller này và tải danh sách học phần.
     * Phương thức này phải được gọi từ controller đã mở cửa sổ này.
     * @param courseManager Đối tượng CourseManager đã được khởi tạo.
     */
    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
        System.out.println("CourseListController: setCourseManager() được gọi.");
        if (this.courseManager != null) {
            System.out.println("  CourseManager đã được thiết lập. Chuẩn bị tải danh sách học phần.");
            loadCourses();
        } else {
            System.err.println("  LỖI: CourseManager được truyền vào CourseListController là null.");
            // Có thể hiển thị thông báo lỗi trên ListView nếu cần
            listCourses.setItems(FXCollections.observableArrayList("Lỗi: Không có CourseManager để tải dữ liệu."));
        }
    }

    /**
     * Tải và hiển thị danh sách các học phần lên ListView.
     * Mỗi học phần được hiển thị dưới dạng "Mã HP - Tên HP".
     */
    private void loadCourses() {
        System.out.println("CourseListController: loadCourses() được gọi.");
        if (courseManager != null) {
            List<Course> coursesFromManager = courseManager.getAllCourses();
            ObservableList<String> courseDisplayStrings = FXCollections.observableArrayList();

            if (coursesFromManager != null && !coursesFromManager.isEmpty()) {
                System.out.println("  Tìm thấy " + coursesFromManager.size() + " học phần từ CourseManager.");
                for (Course course : coursesFromManager) {
                    if (course != null) { // Kiểm tra course không null
                        courseDisplayStrings.add(course.getCourseID() + " - " + course.getCourseName());
                    }
                }
            } else if (coursesFromManager == null) {
                System.err.println("  LỖI: courseManager.getAllCourses() trả về null trong loadCourses().");
                courseDisplayStrings.add("Lỗi tải danh sách học phần.");
            } else {
                System.out.println("  Không có học phần nào trong CourseManager.");
                courseDisplayStrings.add("Không có học phần nào để hiển thị.");
            }
            listCourses.setItems(courseDisplayStrings);
        } else {
            System.err.println("  CourseManager là null trong loadCourses(), không thể tải danh sách.");
            listCourses.setItems(FXCollections.observableArrayList("CourseManager chưa được thiết lập."));
        }
    }

    /**
     * Xử lý sự kiện đóng cửa sổ danh sách học phần.
     */
    @FXML
    private void handleClose() {
        if (btnClose != null && btnClose.getScene() != null && btnClose.getScene().getWindow() != null) {
            Stage stage = (Stage) btnClose.getScene().getWindow();
            System.out.println("CourseListController: Đóng cửa sổ danh sách học phần.");
            stage.close();
        } else {
            System.err.println("CourseListController.handleClose: Không thể lấy stage từ nút đóng.");
        }
    }
}