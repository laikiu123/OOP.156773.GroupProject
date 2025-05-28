package Controller;

import SVBK.app.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

	@FXML
	private Button btnStudentMenu;

	@FXML
	private Button btnCurriculumMenu;

	@FXML
	private Button btnCourseMenu;

	@FXML
	private void goToStudentMenu(ActionEvent event) {
		try {
			MainApp.setRoot("/SVBK/fxml/Student.fxml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void goToProgramMenu(ActionEvent event) {
		try {
			MainApp.setRoot("/SVBK/fxml/Program.fxml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void goToCourseMenu(ActionEvent event) {
		try {
			MainApp.setRoot("/SVBK/fxml/Course.fxml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void exitApplication(ActionEvent event) {
		System.exit(0); // Thoát chương trình
	}
}
