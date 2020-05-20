package JMaths;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

public class Main extends Application {

    int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();

    Stage stage;
    Scene scene;

    int initialX;
    int initialY;

    @Override
    public void start(Stage primaryStage) throws Exception{

        GraphicsDevice Gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = Gd.getDisplayMode().getWidth();
        int height = Gd.getDisplayMode().getHeight();

        // Here you can uncomment and change the resolution to test
        //width = 1600;
        //height = 800;

        Parent root = FXMLLoader.load(getClass().getResource("App.fxml"));

        primaryStage.setTitle("This window is not allowed in your country");
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("LUL.png")));

        stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        scene = new Scene(root, width, height);
        //String styleSheet = getClass().getResource("Bootstrap3.css").toExternalForm();
        String styleSheet = getClass().getResource("Styles.css").toExternalForm();
        scene.getStylesheets().add(styleSheet);

        // Moving
        scene.setOnMousePressed(m -> {
            if (m.getButton() == MouseButton.PRIMARY) {
                scene.setCursor(Cursor.MOVE);
                initialX = (int) (stage.getX() - m.getScreenX());
                initialY = (int) (stage.getY() - m.getScreenY());
            }
        });

        scene.setOnMouseDragged(m -> {
            if (m.getButton() == MouseButton.PRIMARY) {
                stage.setX(m.getScreenX() + initialX);
                stage.setY(m.getScreenY() + initialY);
            }
        });

        scene.setOnMouseReleased(m -> {
            scene.setCursor(Cursor.DEFAULT);
        });

        primaryStage.setScene(scene); // old : 1300x800
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}