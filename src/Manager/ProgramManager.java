
// File: SVBK/manager/ProgramManager.java
package Manager;

import model.Program;
import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý danh sách chương trình đào tạo (Program).
 * Cho phép thêm, xóa, tìm kiếm và chỉnh sửa chương trình.
 */
public class ProgramManager {
    /** Danh sách các Program đang được quản lý. */
    private List<Program> listPrograms;

    /**
     * Khởi tạo ProgramManager với danh sách trống.
     */
    public ProgramManager() {
        this.listPrograms = new ArrayList<>();
    }

    /**
     * Thêm một Program mới.
     *
     * @param program Đối tượng Program cần thêm
     * @return true nếu thêm thành công; false nếu program null hoặc đã tồn tại (trùng majorName)
     */
    public boolean addProgram(Program program) {
        if (program == null || findProgram(program.getMajorName()) != null) {
            return false;
        }
        listPrograms.add(program);
        return true;
    }

    /**
     * Xóa Program theo tên ngành.
     *
     * @param majorName Tên ngành của Program cần xóa
     * @return true nếu xóa thành công; false nếu không tìm thấy
     */
    public boolean removeProgram(String majorName) {
        Program p = findProgram(majorName);
        if (p == null) {
            return false;
        }
        listPrograms.remove(p);
        return true;
    }

    /**
     * Tìm kiếm Program theo tên ngành.
     *
     * @param majorName Tên ngành cần tìm
     * @return Program nếu tồn tại; null nếu không tìm thấy hoặc input không hợp lệ
     */
    public Program findProgram(String majorName) {
        if (majorName == null || majorName.trim().isEmpty()) {
            return null;
        }
        for (Program p : listPrograms) {
            if (majorName.equals(p.getMajorName())) {
                return p;
            }
        }
        return null;
    }

    /**
     * Cập nhật một Program đã tồn tại.
     *
     * @param majorName  Tên ngành của Program cần sửa
     * @param newProgram Đối tượng Program mới (majorName phải khớp)
     * @return true nếu cập nhật thành công; false nếu không tìm thấy hoặc input không hợp lệ
     */
    public boolean editProgram(String majorName, Program newProgram) {
        if (newProgram == null || majorName == null || !majorName.equals(newProgram.getMajorName())) {
            return false;
        }
        for (int i = 0; i < listPrograms.size(); i++) {
            if (majorName.equals(listPrograms.get(i).getMajorName())) {
                listPrograms.set(i, newProgram);
                return true;
            }
        }
        return false;
    }

    /**
     * Lấy bản sao danh sách tất cả Program.
     *
     * @return List<Program> sao chép từ listPrograms
     */
    public List<Program> getAllPrograms() {
        return new ArrayList<>(listPrograms);
    }
}