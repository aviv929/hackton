package Recorder;

import voce.SpeechInterface;
import voce.Utils;

import java.io.File;

public class voice {

    public static Boolean flag=true;
    public static String text="";
    private static boolean singleUse = true;

    public static void Record() throws Exception
    {
        text="";
        File f = new File("");
        String path=f.getAbsolutePath()+"\\src\\voce-0.9.1\\lib\\gram";

        //Utils.setPrintDebug(true);

        if(path == null || path.length()==0){
            throw new Exception("Recorder.voice and grammar paths are required.");
        }

        String configPath = path;


        String voicePath = f.getAbsolutePath()+"\\src\\voce-0.9.1\\lib";//configPath + File.separator + "Recorder.voice";
        String grammarPath = "file:/"+f.getAbsolutePath()+"\\src\\voce-0.9.1\\lib\\gram";//configPath + File.separator + "grammar";
        String grammarName = "digits";//PathHelper.getGrammarFileName(grammarPath);

        System.out.println("voicePath:"+voicePath);
        System.out.println("grammarPath:"+grammarPath);
        System.out.println("grammarName:"+grammarName);

        if(singleUse){

            voce.SpeechInterface.init(voicePath, false, true,grammarPath, grammarName);
            singleUse = false;

            int x=voce.SpeechInterface.getRecognizerQueueSize();
            for (int i = 0; i < x; i++) {
                voce.SpeechInterface.popRecognizedString();
            }
        }

        System.out.println("This is a speech recognition test. "
                + "Speak digits from 0-9 into the microphone. "
                + "Speak 'quit' to quit.");

        String ans="";
        boolean quit = false;
        flag=false;
        while (!flag)
        {
            try
            {
                Thread.sleep(20);
            }
            catch (InterruptedException e)
            {
            }

            while (voce.SpeechInterface.getRecognizerQueueSize() > 0)
            {
                String s = voce.SpeechInterface.popRecognizedString();

                // Check if the string contains 'quit'.
                //if (-1 != s.indexOf("quit"))
                //{
                //    quit = true;
                //}

                ans+=s+" ";
            }
        }

       // voce.SpeechInterface.destroy();
        text=  ans;
    }

    public static void main(String[] args)
    {
        File f = new File("");
        System.out.println(f.getAbsolutePath());

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    voice.Record();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        voice.flag=true;

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(text);
    }
}
