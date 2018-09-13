package Recorder;

import java.io.File;

public interface IRecorder {

    void startRecording(); //Begins the recording thread

    File stopRecording(); //finishes the recording

}
