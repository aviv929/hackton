package View;


import LecturerModel.MyLecturerModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.ListView;

import javax.swing.*;
import java.io.File;

public class LecturerController {

    public Button reject_btn;
    public Button begin_btn;
    @FXML
    private ListView answerList;
    @FXML
    private Button answer_btn;

    private Scene mScene;
    private Stage mPrimaryStage;

    public static StringProperty sp = new SimpleStringProperty("");
    private MyLecturerModel lecturer;
    private Thread t;
    private static boolean record = true;
    private boolean broadcasting;

    @FXML
    protected void initialize(){
        lecturer = new MyLecturerModel();
        reject_btn.setDisable(true);
        answer_btn.setDisable(true);
        lecturer.setController(this);
        broadcasting = false;
    }

    public void beginLesson(){
        if(!broadcasting)
        {
            lecturer.startReceivingQuestions(); //begin accepting students
            reject_btn.setDisable(false);
            answer_btn.setDisable(false);
            broadcasting = true;
        }
    }

    public void createQAFile(){
        JFrame parentFrame = new JFrame();
        String path = "./";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File(""));
        fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Select where to save the Questions and answers.");
        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            path = fileToSave.getAbsolutePath();
            lecturer.createQA_FILE(path);
        }
    }

    public void updateQuestions(String question){
        System.out.println("received update");
        Platform.runLater( ()->{
            answerList.getItems().add(question);
        });
    }

    public void removeQuestion(){
        lecturer.rejectQuestion();
        answerList.getItems().remove(0);
    }

    public void setStage(Stage stage)
    {
        mPrimaryStage = stage;
    }

    public void startAnswering(){
        System.out.println("pressed button");
        if(record){
            System.out.println("Beginning recording");
            lecturer.beginAnswering();
        }
        else{
            System.out.println("stopping recording");
            lecturer.rejectQuestion();
            answerList.getItems().remove(0);
            lecturer.stopAnswering();
        }
        record = !record;
    }
}
