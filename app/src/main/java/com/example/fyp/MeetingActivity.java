package com.example.fyp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.jar.Attributes;

public class MeetingActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 10000;
    TextView NameText;
    TextView TimeText;
    TextView DateText;
    TextView DurationText;
    TextView ReasonText;
    TextView LevelText;
    TextView OtherDateText;
    TextView OtherTimeText;

    ImageButton Speakbutton;
    TextToSpeech TTS;
    SpeechRecognizer mySpeechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        NameText = findViewById(R.id.Name);
        DateText = findViewById(R.id.Date);
        TimeText = findViewById(R.id.Time);
        DurationText = findViewById(R.id.Duration);
        LevelText = findViewById(R.id.Level);
        OtherDateText = findViewById(R.id.OtherDate);
        OtherTimeText = findViewById(R.id.OtherTime);
        Speakbutton = findViewById(R.id.Speak);
        ReasonText = findViewById(R.id.reason);
        Intent intent = getIntent();

        NameText.setText(getIntent().getStringExtra("Name"));
        DateText.setText(getIntent().getStringExtra("Date"));
        TimeText.setText(getIntent().getStringExtra("Time"));
        DurationText.setText(getIntent().getStringExtra("Duration"));
        ReasonText.setText(getIntent().getStringExtra("Reason"));
        LevelText.setText(getIntent().getStringExtra("Level"));
        OtherTimeText.setText(getIntent().getStringExtra("OtherTime"));
        OtherDateText.setText(getIntent().getStringExtra("OtherDate"));

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(getIntent().getStringExtra("Name"));
        arrayList.add(getIntent().getStringExtra("Date"));
        arrayList.add(getIntent().getStringExtra("Time"));
        arrayList.add(getIntent().getStringExtra("Duration"));
        arrayList.add(getIntent().getStringExtra("Reason"));
        arrayList.add(getIntent().getStringExtra("Level"));
        arrayList.add(getIntent().getStringExtra("OtherTime"));
        arrayList.add(getIntent().getStringExtra("OtherDate"));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = FirebaseDatabase.getInstance().getReference().child("a").push().getKey();
        DatabaseReference myRef = database.getReference(key);


        myRef.setValue(arrayList).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MeetingActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
            }
        });
        //   speak("If the data is correct press ok");


        Speakbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  speak();
            }
        });

      //  initializeTextToSpeech();
       // initializeSpeechRecognizer();
    }

    public void SetValue() {
        Intent intent = getIntent();

        NameText.setText(getIntent().getStringExtra("Name"));
        DateText.setText(getIntent().getStringExtra("Date"));
        TimeText.setText(getIntent().getStringExtra("Time"));
        DurationText.setText(getIntent().getStringExtra("Duration"));
        ReasonText.setText(getIntent().getStringExtra("Reason"));
        LevelText.setText(getIntent().getStringExtra("Level"));
        OtherTimeText.setText(getIntent().getStringExtra("OtherTime"));
        OtherDateText.setText(getIntent().getStringExtra("OtherDate"));

        speak("If the data is correct press ok");

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


                    processResult(result.get(0));
                }
            }
            break;
        }

    }


    void speak(String message) {
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
            PyObject pyf = py.getModule("CommandCheck");
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

            } else if (CheckReply.equals("meeting")) {


            } else if (CheckReply.equals("openschedule")) {


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


    private void initializeTextToSpeech() {
        TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (TTS.getEngines().size() == 0) {
                    Toast.makeText(MeetingActivity.this, "No TTS engine installed", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    TTS.setLanguage(Locale.US);
                    // speak("I am Agent Sir how can i Help You");
                }

            }
        });
    }
}
