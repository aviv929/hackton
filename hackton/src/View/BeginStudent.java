package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class BeginStudent extends Application {


    public static void main(String[] args){

        launch(args);
    }

    public void start(Stage primaryStage) throws Exception{


        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("studentWindow.fxml").openStream());
        primaryStage.setTitle("STAR Learning - Lecturer Control Window");
        Scene scene = new Scene(root, 600, 530);

        primaryStage.getIcons().add(new Image("file:./src/Resources/Images/star.png"));
        scene.getStylesheets().add(getClass().getResource("studentStyle.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        StudentController SC = fxmlLoader.getController();
        SC.setStage(primaryStage);
        primaryStage.show();
    }
}
