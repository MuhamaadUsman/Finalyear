package com.example.fyp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextClassifierEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.jar.Attributes;
import java.util.logging.Level;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 10000;
    TextView SpeechText;
    Button ArrangeMeting;
    ImageButton Speakbutton;
    TextToSpeech TTS;
    SpeechRecognizer mySpeechRecognizer;
    String Sign ;
    String Name;
    String Time;
    String Date;
    String Duration;
    String Reason;
    String Level ;
    String OtherChoice;
    String OtherTime;
    String OtherDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpeechText = findViewById(R.id.Speech);
        Speakbutton = findViewById(R.id.Speak);

        ArrangeMeting = (Button) findViewById(R.id.Arrange);
        ArrangeMeting.setVisibility(View.GONE);
        ArrangeMeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MeetingActivity.class));
            }
        });

       Sign = "";

        Python.start(new AndroidPlatform(this));
        Speakbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                speak();
            }
        });

        initializeTextToSpeech();
        initializeSpeechRecognizer();

    }




    public void speak() {

        Intent intent = new Intent((RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);


        try {
            mySpeechRecognizer.startListening(intent);



            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);

        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle bundle) {

                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    SpeechText.setText(result.get(0));
                    if (Sign.equals("Name")) {
                        try {
                            GetName(result.get(0));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (Sign.equals("Time")){

                        try {
                            GetTime(result.get(0));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    else if (Sign.equals("Date")){

                        try {
                            GetDate(result.get(0));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    else if (Sign.equals("Duration")){

                        try {
                            GetDuration(result.get(0));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    else if (Sign.equals("Reason")){

                        try {
                            GetReason(result.get(0));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }else if (Sign.equals("Level")){

                        try {
                            GetLevel(result.get(0));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    else if (Sign.equals("OtherChoice")){

                        try {
                            GetOtherChoice(result.get(0));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    else if (Sign.equals("OtherTime")){

                        try {
                            GetOtherTime(result.get(0));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    else if (Sign.equals("OtherDate")){

                        try {
                            GetOtherDate(result.get(0));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    else
                        {
                        processResult(result.get(0));
                    }
                }
                break;
            }
        }
    }

    private void GetOtherDate(String s) throws InterruptedException {
        s= s.toLowerCase();
        OtherDate = s;

        if (OtherDate.equals("")){

            speak("sir kindly inform me about other time choice");
            synchronized(this) {
                this.wait(2000);
            }
            speak();

        }
        else {
            PutValue();
            Sign = "";

    }

    }
 public void PutValue(){
     Intent intent = new Intent(this, MeetingActivity.class);
    // speak("Okay sir have a look at the meeting details, is that what you want ");

     intent.putExtra("Name", Name);
     intent.putExtra("Date", Date);
     intent.putExtra("Time", Time);
     intent.putExtra("Duration", Duration);
     intent.putExtra("Reason", Reason);
     intent.putExtra("Level", Level);
     intent.putExtra("OtherTime", OtherTime);
     intent.putExtra("OtherDate", OtherDate);
     startActivity(intent);
     //ArrangeMeting.performClick();


 }
    private void GetOtherTime(String s) throws InterruptedException {
        s= s.toLowerCase();
        OtherTime = s;

        if (OtherTime.equals("")){

            speak("sir kindly inform me about other time choice");
            synchronized(this) {
                this.wait(2000);
            }
            speak();

        }
        else {

            Sign = "OtherDate";
            speak("what would be the preferrd  date after this ");
            synchronized(this) {
                this.wait(2000);
            }
            speak();


        }
    }

    private void GetOtherChoice(String s) throws InterruptedException {
        s= s.toLowerCase();
        OtherChoice = s;

        if (OtherChoice.equals("")){

            speak("sir kindly inform me about other choice");
            synchronized(this) {
                this.wait(2000);
            }
            speak();

        }
        else if(OtherChoice.indexOf("yes")!=-1 || OtherChoice.indexOf("yeah")!=-1 || OtherChoice.indexOf("why not")!=-1)

        {

            Sign = "OtherTime";
            speak("what would be the preferrd  time after this ");
            synchronized(this) {
                this.wait(3000);
            }
            speak();


        }
        else if (OtherChoice.equals("no"))
        {

            Sign = "";
            PutValue();



            speak("new page ");

            // startActivity(intent);

        }
    }

    private void GetLevel(String s) throws InterruptedException {
        s= s.toLowerCase();
        Level = s;

        if (Level.equals("")){

            speak("sir i asked about the priority of meeting weather casual or urgent");
            synchronized(this) {
                this.wait(3000);
            }
            speak();

        }
        else if ((Level.indexOf("urgent")!=-1 || Level.indexOf("casual")!=-1)){

            Sign = "OtherChoice";
            speak("If this schdule is busy do you want to meet some other time");
            synchronized(this) {
                this.wait(3000);
            }
            speak();


        }
        else
            {
           speak("kindly tell the priority again i am unable to understand");
                synchronized(this) {
                    this.wait(3000);
                }
            speak();
        }


    }

    private void GetReason(String s) throws InterruptedException {
        s= s.toLowerCase();
        Reason = s;


        if (Reason.equals("")){

            speak("sir kindly give reason of meeting");
            synchronized(this) {
                this.wait(2000);
            }
            speak();

        }
        else{
            Sign = "Level";
            speak("Is the meeting argent or casual");
            synchronized(this) {
                this.wait(2000);
            }
            speak();

        }
    }

    private void GetDuration(String s) throws InterruptedException {
        s= s.toLowerCase();
        Duration = s;

        if (Duration.equals("")){

            speak("sir kindly give the meeting duration");
            synchronized(this) {
                this.wait(2000);
            }
            speak();

        }
        else{
            speak("what is the reason of the meeting sir");
            synchronized(this) {
                this.wait(2000);
            }
            Sign = "Reason";
            speak();





        }
    }

    private void GetDate(String s) throws InterruptedException {
        try {
            s= s.toLowerCase();

            Python py = Python.getInstance();
            PyObject pyf  = py.getModule("CommandCheck");
        PyObject obj = pyf.callAttr("Date", s);
        String Reply = obj.toString();
        Date = Reply;

        if (Date.equals("")){

            speak("sir kindly the date for meeting");
            synchronized(this) {
                this.wait(2000);
            }
            speak();

        }
        else{
            speak("what would be the estimated duration of meeting");
            Sign = "Duration";
            synchronized(this) {
                this.wait(3000);
            }
            speak();





        }} catch (Exception e) {
        Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
    }}

    private void GetName(String s) throws InterruptedException {
            s= s.toLowerCase();
        try {

            Python py = Python.getInstance();
            PyObject pyf  = py.getModule("CommandCheck");

        PyObject obj = pyf.callAttr("Name", s);
         Name = obj.toString();


        if (Name.equals(""))
        {

            speak("this person dont have same give the name again");
            synchronized(this) {
                this.wait(3000);
            }

            speak();


        }

        else{

            speak("At what time do you want to meet");
            Sign = "Time";
            synchronized(this) {
                this.wait(2000);
            }
            speak();




        }


    } catch (Exception e) {
        Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
    }}

    private void GetTime(String s) throws InterruptedException {
        s= s.toLowerCase();
        try {
            Python py = Python.getInstance();
            PyObject pyf = py.getModule("CommandCheck");

            PyObject obj = pyf.callAttr("Time", s);
            String Reply = obj.toString();
            Time = Reply;

            if (Time.equals("")) {

                speak("sir kindly give the time for meeting or wrong time input");
                synchronized (this) {
                    this.wait(3000);
                }
                speak();

            } else {
                speak("On which date sir");
                Sign = "Date";
                synchronized (this) {
                    this.wait(1500);
                }
                speak();


            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void speak(String message)  {
        boolean speakingEnd = TTS.isSpeaking();
        if (Build.VERSION.SDK_INT >= 21) {
                 TTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
         } else {

                TTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);

        }

    }

    private void processResult(String command) {
        command = command.toLowerCase();
        try {

            Python py = Python.getInstance();
            PyObject pyf  = py.getModule("CommandCheck");
            PyObject obj = pyf.callAttr("Check", command);
            String CheckReply = obj.toString();


            if (CheckReply.equals("greeting")) {
                speak("Hi how can i help you");
            } else if (CheckReply.equals("AskingAboutYou")) {
                speak("I m Good ");
            } else if (CheckReply.equals("name")) {
                speak("I m Agent Sam ");
            } else if (CheckReply.equals("time")) {
                Date now = new Date();
                String time = DateUtils.formatDateTime(this, now.getTime(), DateUtils.FORMAT_SHOW_TIME);
                speak("The time now is" + time);
            } else if (CheckReply.equals("browser")) {

                try {
                    speak("i am opning browser");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
            else if (CheckReply.equals("meeting")){

                Meeting();

            }
            else if (CheckReply.equals("openschedule")){




            }

        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        /*

        {
            // Intent intent = new Intent(this,meting.class);
            // startActivity(intent);

            speak("what is the title of the event?");
            Speakbutton.performClick();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms

                    String title = (String) SpeechText.getText(); //command.toLowerCase().toString();
                    // speak("where is the Event");
                    String loc = ""; //command.toLowerCase().toString();

                    String time_now = DateUtils.formatDateTime(getApplicationContext(), new Date().getTime(), DateUtils.FORMAT_SHOW_TIME);


                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.Events.TITLE, title)
                            .putExtra(CalendarContract.Events.EVENT_LOCATION, loc)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time_now)
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, time_now);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            }, 5000);


        } else {
            speak("Unable to understand");
        } */
    }

    private void Meeting() throws InterruptedException {
        speak("Sir Kindly give me meeting details, Who do you want to meet sir");


        Sign = "Name";


        synchronized(this) {
            this.wait(4000);
        }

       speak();





    }

    private void initializeTextToSpeech() {
        TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (TTS.getEngines().size()==0){
                    Toast.makeText(MainActivity.this,"No TTS engine installed",Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    TTS.setLanguage(Locale.US);
                    // speak("I am Agent Sir how can i Help You");
                }

            }
        });
    }
}