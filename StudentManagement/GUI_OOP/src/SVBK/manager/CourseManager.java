// File: SVBK/manager/CourseManager.java
package SVBK.manager;

import java.util.ArrayList;
import java.util.List;

import SVBK.model.Course;
import SVBK.model.Program;

/**
 * Quản lý danh sách học phần (Course). Tự động đồng bộ xóa và cập nhật trong
 * các Program liên quan khi một Course bị xóa hoặc sửa.
 */
public class CourseManager {
	/** Danh sách các Course hiện có trong hệ thống. */
	private List<Course> listCourses;

	/** Tham chiếu đến ProgramManager để thực hiện cascade xóa và cập nhật. */
	private ProgramManager programManager;

	/**
	 * Constructor khởi tạo CourseManager với ProgramManager để quản lý cascade.
	 *
	 * @param programManager đối tượng quản lý Program
	 */
	public CourseManager(ProgramManager programManager) {
		this.listCourses = new ArrayList<>();
		this.programManager = programManager;
	}

	/**
	 * Thêm một Course mới vào danh sách.
	 *
	 * @param course Đối tượng Course cần thêm
	 * @return true nếu thêm thành công; false nếu course null hoặc đã tồn tại
	 *         (trùng courseID)
	 */
	public boolean addCourse(Course course) {
		if (course == null || findCourse(course.getCourseID()) != null) {
			return false;
		}
		listCourses.add(course);
		return true;
	}

	/**
	 * Xóa Course khỏi danh sách theo courseID và cascade xóa khỏi tất cả Program.
	 *
	 * @param courseID Mã học phần cần xóa
	 * @return true nếu xóa thành công; false nếu không tìm thấy courseID
	 */
	public boolean removeCourse(String courseID) {
		Course toRemove = findCourse(courseID);
		if (toRemove == null) {
			return false;
		}
		// Xóa khỏi danh sách trung tâm
		listCourses.remove(toRemove);
		// Cascade xóa khỏi yêu cầu và tự chọn của mỗi Program
		for (Program p : programManager.getAllPrograms()) {
			p.getRequiredCourses().removeIf(c -> courseID.equals(c.getCourseID()));
			p.getElectiveCourses().removeIf(c -> courseID.equals(c.getCourseID()));
		}
		return true;
	}

	/**
	 * Tìm kiếm Course theo courseID.
	 *
	 * @param courseID mã học phần cần tìm
	 * @return Course nếu tồn tại; null nếu không tìm thấy hoặc input không hợp lệ
	 */
	public Course findCourse(String courseID) {
		if (courseID == null || courseID.trim().isEmpty()) {
			return null;
		}
		for (Course c : listCourses) {
			if (courseID.equals(c.getCourseID())) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Sửa thông tin Course đã tồn tại. Cascade: cập nhật thay thế instance cũ bằng
	 * newCourse trong tất cả Program.
	 *
	 * @param courseID  Mã học phần cần sửa
	 * @param newCourse Đối tượng Course chứa thông tin mới; courseID phải khớp
	 * @return true nếu sửa thành công; false nếu không tìm thấy hoặc input không
	 *         hợp lệ
	 */
	public boolean editCourse(String courseID, Course newCourse) {
		if (newCourse == null || courseID == null || !courseID.equals(newCourse.getCourseID())) {
			return false;
		}
		for (int i = 0; i < listCourses.size(); i++) {
			if (courseID.equals(listCourses.get(i).getCourseID())) {
				// Lấy instance cũ để so sánh
				Course oldCourse = listCourses.get(i);
				// Thay thế trong danh sách trung tâm
				listCourses.set(i, newCourse);
				// Cascade cập nhật trong mỗi Program
				for (Program p : programManager.getAllPrograms()) {
					// requiredCourses
					List<Course> req = p.getRequiredCourses();
					for (int j = 0; j < req.size(); j++) {
						if (oldCourse.getCourseID().equals(req.get(j).getCourseID())) {
							req.set(j, newCourse);
						}
					}
					// electiveCourses
					List<Course> elec = p.getElectiveCourses();
					for (int j = 0; j < elec.size(); j++) {
						if (oldCourse.getCourseID().equals(elec.get(j).getCourseID())) {
							elec.set(j, newCourse);
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Lấy danh sách tất cả Course dưới dạng bản sao.
	 *
	 * @return List<Course> sao chép từ listCourses
	 */
	public List<Course> getAllCourses() {
		return new ArrayList<>(listCourses);
	}
}
