package View;


import StudentModel.MyStudentModel;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;


public class StudentController {

    @FXML
    public TextField question_txt;
    public Label question_lbl;
    public Button send_btn;
    public Label connectionStatus_lbl;

    private Stage stage;
    private MyStudentModel student;

    @FXML
    protected void initialize(){
        System.out.println("initializing");
        student = new MyStudentModel();
        send_btn.setDisable(true);
        student.setController(this);

    }

    public void connectToLecturer(){
        connectionStatus_lbl.setText("Attempting to connect..");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        student.connectToLecturer();

        send_btn.setDisable(false);
        connectionStatus_lbl.setText("Connected!");
    }

    public void send(ActionEvent eve)
    {
        String text = question_txt.getText();
        student.sendQuestion(text);
        question_txt.setText("");
    }

    public void connected(){
        send_btn.setDisable(false);
        connectionStatus_lbl.setText("Connected!");
    }

    public void setStage(Stage primaryStage) {
        stage = primaryStage;
    }
}
