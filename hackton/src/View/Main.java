package View;

import Recorder.voice;
import StudentModel.MyStudentModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application{

    public static void main(String[] args){

        launch(args);
    }

    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("lecturerWindow.fxml").openStream());
        primaryStage.setTitle("STAR Learning - Lecturer Control Window");
        Scene scene = new Scene(root, 1000, 530);

        primaryStage.getIcons().add(new Image("file:./src/Resources/Images/star.png"));
        scene.getStylesheets().add(getClass().getResource("lecturerStyle.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        LecturerController LC = fxmlLoader.getController();
        LC.setStage(primaryStage);
        primaryStage.show();
    }
}
