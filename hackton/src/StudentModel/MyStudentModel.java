package StudentModel;

import View.StudentController;
import javafx.util.Pair;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class MyStudentModel implements IStudentModel{

    private boolean connectedToLecturer;
    private ArrayList<Pair<String,String>> questionsAndAnswers;
    private Socket clientSocket;
    private StudentController controller;

    public MyStudentModel() {
        connectedToLecturer = false;
        questionsAndAnswers = new ArrayList<>();
    }

    @Override
    public void setController(StudentController controller) {
        this.controller = controller;
    }

    public void connectToLecturer(){
        Thread connect = new Thread( ()->{
            while (!connectedToLecturer) {
                try {
                    clientSocket = new Socket("localhost", 6789);
                    connectedToLecturer = true;
                    System.out.println("managed to connect");
//                    controller.connected();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (ConnectException cex) {
                    System.out.println("No server found..");
                    try {
                        connectedToLecturer = false;
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {ie.printStackTrace(); }
                } catch (IOException e) { e.printStackTrace(); }
            }
        });

        connect.start();
    }

    @Override
    public void disconnectFromLecturer() {
        try {
            clientSocket.close();
            connectedToLecturer = false;
        } catch (IOException e) { System.out.println("IO exception when closing lecturer socket from student"); }
    }


    @Override
    public void sendQuestion(String question){
        Thread sender = new Thread( () ->{
            while(true){
                try {
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    connectedToLecturer = true;
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //Why doesnt this work :(
                    outToServer.writeBytes(question + '\n');
                    break;
                }
                catch(ConnectException cex)
                {
                    System.out.println("No server found..");
                    try{
                        connectedToLecturer = false;
                        Thread.sleep(2000);
                    }
                    catch(InterruptedException ie){ ie.printStackTrace(); }
                }
                catch(IOException IOex)
                { IOex.printStackTrace(); }
            }
        });

        sender.start();
    }

    @Override
    public void writeQuestionAndAnswer(String question, String ans) {
        questionsAndAnswers.add(new Pair<>(question, ans));
    }

    @Override
    public boolean isConnected() {
        return connectedToLecturer;
    }
}
