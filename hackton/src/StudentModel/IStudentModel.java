package StudentModel;

import View.StudentController;

public interface IStudentModel {

    void setController(StudentController controller);

    void connectToLecturer();

    void disconnectFromLecturer();

    void sendQuestion(String Question); // Sends the question to the lecturer

    void writeQuestionAndAnswer(String question, String ans); //Allows a student to input a question and its answer.

    boolean isConnected();
}
