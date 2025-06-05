package SVBK.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import SVBK.controller.MainMenuController;
import SVBK.manager.*;
import SVBK.model.*;
import SVBK.service.*;
import SVBK.utils.SampleDataGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainApp extends Application {

    private ProgramManager programManager;
    private CourseManager courseManager;
    private StudentManager studentManager;
    private GradingSystem gradingSystem;
    private GraduationValidator graduationValidator;

    private static ProgramManager staticProgramManager;
    private static CourseManager staticCourseManager;
    private static StudentManager staticStudentManager;
    private static GradingSystem staticGradingSystem;
    private static GraduationValidator staticGraduationValidator;

    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Khởi tạo Managers
            this.programManager = new ProgramManager();
            this.courseManager = new CourseManager(this.programManager);
            this.studentManager = new StudentManager();
            this.gradingSystem = new GradingSystem();
            this.graduationValidator = new GraduationValidator();

            staticProgramManager = this.programManager;
            staticCourseManager = this.courseManager;
            staticStudentManager = this.studentManager;
            staticGradingSystem = this.gradingSystem;
            staticGraduationValidator = this.graduationValidator;

            // 2. Tạo và thêm tất cả Courses từ SampleDataGenerator
            // This list is still needed for student enrollment examples later
            List<Course> allCourses = SampleDataGenerator.createCoursesOnly();
            for (Course course : allCourses) {
                if (course != null) { // Đảm bảo course không null trước khi thêm
                    courseManager.addCourse(course);
                }
            }
            System.out.println("MainApp: Đã thêm " + courseManager.getAllCourses().size() + " học phần vào CourseManager.");

            // 3. Tạo và thêm Programs từ SampleDataGenerator
            // Calls to generator methods no longer pass allCourses
            Program ITE6 = SampleDataGenerator.generateVietNhatITProgram();
            if (ITE6 != null) programManager.addProgram(ITE6);

            Program ITE6NC = SampleDataGenerator.generateVietNhatITparttimeProgram();
            if (ITE6NC != null) programManager.addProgram(ITE6NC);

            Program CH1 = SampleDataGenerator.generateChemicalEngineeringProgram();
            if (CH1 != null) programManager.addProgram(CH1);
            
            // Assuming generateChemicalEngineeringPartTimeProgram exists and is parameterless
            Program CH1NC = SampleDataGenerator.generateChemicalEngineeringPartTimeProgram();
            if (CH1NC != null) programManager.addProgram(CH1NC);


            Program PH1 = SampleDataGenerator.generateEngineeringPhysicsProgram();
            if (PH1 != null) programManager.addProgram(PH1);

            // Bạn có thể gọi thêm các hàm generate...Program khác ở đây và add vào programManager

            System.out.println("MainApp: Đã thêm " + programManager.getAllPrograms().size() + " chương trình vào ProgramManager.");
            if (!programManager.getAllPrograms().isEmpty()){
                System.out.println("  Chương trình đầu tiên trong ProgramManager: " + programManager.getAllPrograms().get(0).getMajorName());
            }

            /*
            // 4. Tạo dữ liệu mẫu cho Student (và Enrollments) - ví dụ đơn giản
            // The 'allCourses' list is used here by SampleDataGenerator.findCourseById
            if (itE6Program != null) {
                CreditBasedStudent sv001 = new CreditBasedStudent("SV001", "Nguyễn Văn An", itE6Program);
                // SampleDataGenerator.findCourseById still needs the allCourses list
                Course cs101Ref = SampleDataGenerator.findCourseById(allCourses, "IT3210"); // Ví dụ C Programming Language
                if (cs101Ref != null) {
                    Enrollment enrollAnCS101 = new Enrollment(cs101Ref);
                    enrollAnCS101.setMidtermScore(8.0); enrollAnCS101.setFinalScore(7.5);
                    sv001.getEnrollments().add(enrollAnCS101);
                }
                studentManager.addStudent(sv001);
            }

            if (ch1Program != null) {
                 CreditBasedStudent sv002 = new CreditBasedStudent("SV002", "Trần Thị Bình", ch1Program);
                 studentManager.addStudent(sv002);
            }

            if (itE6NCPProgram != null) {
                PartTimeStudent sv003 = new PartTimeStudent("SV003", "Lê Văn Cường", itE6NCPProgram);
                studentManager.addStudent(sv003);
            }*/
            
            CreditBasedStudent student1 = new CreditBasedStudent("20235223", "Nguyễn Văn Công", ITE6);
            CreditBasedStudent student2 = new CreditBasedStudent("20238329", "Nguyễn Thị Mai", CH1);
            CreditBasedStudent student3 = new CreditBasedStudent("20237546", "Nguyễn Văn Chí ", PH1);
            PartTimeStudent student4 = new PartTimeStudent("20226868", "Nguyễn Văn Hoa", ITE6NC);
            PartTimeStudent student5 = new PartTimeStudent("20237328", "Nguyễn Thị Linh", CH1NC);
            
            studentManager.addStudent(student1);
            studentManager.addStudent(student2);
            studentManager.addStudent(student3);
            studentManager.addStudent(student4);
            studentManager.addStudent(student5);


            System.out.println("✅ MainApp: Đã khởi tạo dữ liệu mẫu mới.");
            System.out.println("MainApp: Số chương trình trong staticProgramManager: " + (staticProgramManager != null ? staticProgramManager.getAllPrograms().size() : "null"));


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/MainMenu.fxml"));
            Scene scene = new Scene(loader.load());

            MainMenuController controller = loader.getController();
            System.out.println("MainApp: Gọi initManagers của MainMenuController LẦN ĐẦU.");
            controller.initManagers(this.programManager, this.courseManager, this.studentManager,
                                    this.gradingSystem, this.graduationValidator);

            primaryStage.setTitle("Student Management System (Dữ liệu nội bộ)");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
             System.err.println("❌ Lỗi nghiêm trọng khi khởi tạo ứng dụng: " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi ứng dụng");
            alert.setHeaderText("Không thể khởi chạy ứng dụng.");
            alert.setContentText("Đã có lỗi xảy ra trong quá trình khởi tạo: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public static ProgramManager getStaticProgramManager() { return staticProgramManager; }
    public static CourseManager getStaticCourseManager() { return staticCourseManager; }
    public static StudentManager getStaticStudentManager() { return staticStudentManager; }
    public static GradingSystem getStaticGradingSystem() { return staticGradingSystem; }
    public static GraduationValidator getStaticGraduationValidator() { return staticGraduationValidator; }

    public static void main(String[] args) {
        launch(args);
    }
}