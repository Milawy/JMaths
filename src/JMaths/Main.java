package JMaths;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("App.fxml"));
        primaryStage.setTitle("This window is not allowed in your country");
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("LUL.png")));
        primaryStage.setScene(new Scene(root, 1300, 800));
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}