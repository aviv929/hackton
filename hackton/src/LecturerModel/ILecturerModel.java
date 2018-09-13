package LecturerModel;

import View.LecturerController;

public interface ILecturerModel {

    void startReceivingQuestions(); //starts the process of receiving questions from students

    void setController(LecturerController controller);

    void rejectQuestion(); //reject the current top answer

    void beginAnswering(); // Begin answering a question, recording the answer and transcribing it

    void stopAnswering(); //finishes answering and can move on to next question

    void createQA_FILE(String path); //creates the file that contains the questions and answers.

    String[] getQuestions();

}
