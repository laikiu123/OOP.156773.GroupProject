package SVBK.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

	private static Stage primaryStage;

	@Override
	public void start(Stage stage) throws Exception {
		primaryStage = stage;

		// Tải giao diện MainMenu mặc định khi khởi động
		Parent root = FXMLLoader.load(getClass().getResource("/SVBK/fxml/MainMenu.fxml"));
		primaryStage.setTitle("Student Management System by SVBK");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	// Phương thức tiện ích để chuyển đổi scene từ controller
	public static void setRoot(String fxmlPath) throws Exception {
		FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlPath));
		Parent root = loader.load();
		primaryStage.getScene().setRoot(root);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
