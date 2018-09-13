package Recorder;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MyRecorder implements IRecorder{

    private AudioFormat format;
    private DataLine.Info info;
    private TargetDataLine targetLine;
    private Thread recorderThread;
    private File audioFile;
    private String audioFilePath;

    public MyRecorder(String path)
    {
        format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100, 16, 2, 4,44100,false);
        info = new DataLine.Info(TargetDataLine.class, format);
        audioFilePath = path;
        if(!AudioSystem.isLineSupported(info)){
            System.err.print("Unsupported line");
        }
        try{
            targetLine = (TargetDataLine)AudioSystem.getLine(info);
            targetLine.open();
        }
        catch(LineUnavailableException lua)
        {
            lua.printStackTrace();
        }
        recorderThread = new Thread(this::recordStream);
    }

    @Override
    public void startRecording() {
        targetLine.start();
//        recorderThread.start();
    }

    //function run by recording thread
    private void recordStream(){
        AudioInputStream audioStream = new AudioInputStream(targetLine);
        audioFile = new File(audioFilePath); //location of audio file. Maybe receive as input?
        try{
            AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
        }
        catch(IOException ioe)
        {
            System.out.println("IO exception :(");
            ioe.printStackTrace();
        }
    }

    @Override
    public File stopRecording() {
        targetLine.stop();
        targetLine.close();
        return audioFile;
    }
}
