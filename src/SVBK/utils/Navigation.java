package SVBK.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Navigation {

    // fxmlPath ví dụ: "/JavaFX/StudentManagement.fxml"
    public static void gotoView(Node nodeOnCurrentScene, String fxmlPath) {
        try {
            Parent newRoot = FXMLLoader.load(Navigation.class.getResource(fxmlPath));
            Stage stage = (Stage) nodeOnCurrentScene.getScene().getWindow();

            // đổi root của Scene hiện tại => giữ nguyên trạng thái full/max
            stage.getScene().setRoot(newRoot);

            // đảm bảo vẫn full/max (tuỳ bạn dùng chế độ nào ở MainApp)
            if (stage.isFullScreen()) {
                stage.setFullScreen(true);
            } else {
                stage.setMaximized(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
