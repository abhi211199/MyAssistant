package com.example.abhishek.myassistant;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        TextToSpeech.OnInitListener {
 ImageButton input;
 public String text;
 public String abc=null;
    public TextToSpeech tts;
 public String inpmsg;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input=(ImageButton)findViewById(R.id.inp);
        tts = new TextToSpeech(this, this);
        input.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                askSpeechInput();
            }
        });
    }

    private void askSpeechInput() {
        inpmsg=null;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hello!\uD83D\uDE00 I am listening...");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }

//        speakOut();

    }

    // Receiving speech input

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    inpmsg.setText(result.get(0));
                    inpmsg=result.get(0);
//                    StringBuffer sb=new StringBuffer(inpmsg);
//                    for(Integer i=0;i<inpmsg.length();i++)
//                    {
//                        abc.charAt(i)=inpmsg.charAt(i);
//                    }
                    String abc= inpmsg.toLowerCase();
                    inpmsg=abc;
                    Toast.makeText(getBaseContext(),inpmsg,Toast.LENGTH_SHORT).show();
                    speakOut();
                }
                break;
            }

        }

    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();

    }

    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    public void speakOut() {
        inpmsg.toLowerCase();
        if(inpmsg.equalsIgnoreCase("hi"))
        {
        tts.speak("hello", TextToSpeech.QUEUE_FLUSH, null);}
        else if (inpmsg.equalsIgnoreCase("hello"))
        { tts.speak("hi", TextToSpeech.QUEUE_FLUSH, null);}
        else if (inpmsg.equalsIgnoreCase("what is your name"))
        { tts.speak("my name is cyril", TextToSpeech.QUEUE_FLUSH, null);}
        else if (inpmsg.equalsIgnoreCase("please introduce yourself"))
        { tts.speak("my name is cyril. i have been made by abhishek garain. i love to eat chocloates", TextToSpeech.QUEUE_FLUSH, null);}
        else if (inpmsg.equalsIgnoreCase("open whatsapp"))
        {   boolean res = openApp(this, "com.facebook.orca");
            if(res==false){
                tts.speak("this app is not present in your phone!", TextToSpeech.QUEUE_FLUSH, null);
                }
        }
        else if(inpmsg.charAt(0)=='p'&&inpmsg.charAt(1)=='l'&&inpmsg.charAt(2)=='a'&&inpmsg.charAt(3)=='y')
        {
            play();
        }
        else
        { tts.speak("sorry! i don't understand what you mean!! my intelligence is in beta version", TextToSpeech.QUEUE_FLUSH, null);
            Toast.makeText(getBaseContext(),"\uD83D\uDE00",Toast.LENGTH_LONG).show();
        }

    }

    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                return false;
                //throw new ActivityNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public void play()
    {
        Integer i=0,k=0;
        String url = inpmsg.substring(5,inpmsg.length());
        String url1="https://www.youtube.com/results?search_query=";
        url1=url1+url;
        tts.speak("Playing  "+inpmsg.substring(5,inpmsg.length()), TextToSpeech.QUEUE_FLUSH, null);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url1));
        intent.setPackage("com.google.android.youtube");
        startActivity(intent);
    }

}
