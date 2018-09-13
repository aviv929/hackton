package LecturerModel;

import Recorder.MyRecorder;
import Recorder.voice;
import View.LecturerController;
import javafx.util.Pair;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyLecturerModel implements ILecturerModel {

    private ConcurrentLinkedQueue<String> questionsQueue;
   // private MyRecorder recorder;
    private LinkedList<EchoThread> clientList;
    private ArrayList<Pair<String, String>> questionsAndAnswers;
    private String currentQuestion;
    private Thread studentListener;
    private LecturerController controller;


    private static int index = 1;
    private final int port = 6789;

    public MyLecturerModel(){
        questionsQueue = new ConcurrentLinkedQueue<>();
      //  recorder = new MyRecorder("./recording.wav");
        clientList = new LinkedList<>();
        questionsAndAnswers = new ArrayList<>();
        studentListener = new Thread(this::listenToClients);

    }

    @Override
    public void startReceivingQuestions(){
        studentListener.start();
        System.out.println("Began listening to students");
    }

    @Override
    public void setController(LecturerController controller) {
        this.controller = controller;
    }

    private void listenToClients(){
        try{
            ServerSocket serverSocket =  new ServerSocket(port);
            Socket socket = null;

            while(true){
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }
                // new thread for a client
                clientList.add(new EchoThread(socket));
                clientList.getLast().start();
            }
        } catch(BindException bex){ System.out.println("Address already in use"); }
        catch(IOException ioe)
        {
            System.out.println("IO exception with server");
            ioe.printStackTrace();
        }
    }

    @Override
    public void rejectQuestion() {
        questionsQueue.poll();
    }

    @Override
    public void beginAnswering() {
        currentQuestion = questionsQueue.poll();
        //recorder.startRecording();
        //begin transcribing

        Thread transcribe = new Thread(()->{
            try {
                voice.flag = false;
                voice.Record();
            } catch (Exception e) { System.out.println("voice record exception"); }

        });
        transcribe.start();
    }

    @Override
    public void stopAnswering(){
       // File audioAnswer = recorder.stopRecording();
        voice.flag = true;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String answer = voice.text;
        System.out.println("Recorded answer: " + answer);
        //transcribe or w/e
        questionsAndAnswers.add(new Pair<>(currentQuestion, answer));
    }

    @Override
    public void createQA_FILE(String path) {
        int questionNum = 1;
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
     //   path += "\\output\\";
        PrintWriter out;
        System.out.println("Number of files: " + questionsAndAnswers.size());
        for(Pair<String, String> entry : questionsAndAnswers){
            try {
                String filePath = path + "\\" + "Question " + questionNum + ".txt";
                System.out.println(filePath);
                out = new PrintWriter( filePath );
                out.write(entry.getKey()+"\n"+entry.getValue());
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public String[] getQuestions() {
        String[] questions = new String[questionsQueue.size()];
        this.questionsQueue.toArray(questions);
        return questions;
    }


    private class EchoThread extends Thread {
        Socket socket;

        EchoThread(Socket clientSocket) {
            this.socket = clientSocket;
        }

        public void run(){
            InputStream inp;
            BufferedReader brinp;
            DataOutputStream out;
            try {
                inp = socket.getInputStream();
                brinp = new BufferedReader(new InputStreamReader(inp));
                out = new DataOutputStream(socket.getOutputStream());
            }catch(IOException e){
                return;
            }
            String question;
            boolean connectToStudent = true;
            while (connectToStudent) {
                try {
                    question = brinp.readLine();
                    if ((question == null) || question.equalsIgnoreCase("QUIT")) {
                        System.out.println("Terminating connection");
                        socket.close();
                        return;
                    } else {
                        System.out.println("Message received from student number " + index++ +":" + question);
                        questionsQueue.add(question);
                        controller.updateQuestions(question);
                        //System.out.println("Writing message to student..");
                        out.writeBytes("Message from the motherfuckin' lecturer");
                        System.out.println("Replied to student");
                        out.flush();
                    }
                } catch(SocketException sockex){
                    System.out.println("Student disconnected..");
                    connectToStudent = false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}


