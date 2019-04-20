package chatbotbiblio.modulochatbot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.TransformationMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIService;
import ai.api.AIServiceException;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity implements AIListener {
    private static final String TAG = "ChatActivity";
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private FloatingActionButton sendButton;
    private FloatingActionButton listenButton;
    private AIService aiService;
    private Animation pop_in_anim;
    private Animation pop_out_anim;
    private ArrayList<Acao> acoes = new ArrayList<Acao>();
    private String config;
    private String falaInicial = "lalala";
    private boolean rightSide = true; //true if you want message on right rightSide

    //addition
    private AIDataService aiDataService;
    Result result;
    private TextView rTextView;



    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TTS.init(getApplicationContext());


        sendButton = (FloatingActionButton) findViewById(R.id.btn_send);
        listView = (ListView) findViewById(R.id.msgview);
        listenButton = (FloatingActionButton) findViewById(R.id.btn_mic);
        chatText = (EditText) findViewById(R.id.msg);

        pop_in_anim = AnimationUtils.loadAnimation(this, R.anim.pop_in);
        pop_out_anim = AnimationUtils.loadAnimation(this, R.anim.pop_out);
        sendButton.setAnimation(pop_out_anim);
        sendButton.setAnimation(pop_in_anim);
        listenButton.setAnimation(pop_in_anim);
        listenButton.setAnimation(pop_out_anim);
        sendButton.clearAnimation();
        listenButton.clearAnimation();

        chatArrayAdapter = new ChatArrayAdapter(this, R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        rTextView = (TextView) findViewById(R.id.msgr);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            makeRequest();
        }
        chatText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0 && listenButton.getVisibility() == View.GONE) {
                    sendButton.clearAnimation();
                    sendButton.startAnimation(pop_out_anim);
                    sendButton.setVisibility(View.GONE);
                    sendButton.setEnabled(false);
                    listenButton.clearAnimation();
                    listenButton.setVisibility(View.VISIBLE);
                    listenButton.startAnimation(pop_in_anim);
                    listenButton.setEnabled(true);

                } else if (s.length() > 0 && sendButton.getVisibility() == View.GONE) {
                    listenButton.clearAnimation();
                    listenButton.startAnimation(pop_out_anim);
                    listenButton.setVisibility(View.GONE);
                    listenButton.setEnabled(false);
                    sendButton.clearAnimation();
                    sendButton.setVisibility(View.VISIBLE);
                    sendButton.startAnimation(pop_in_anim);
                    sendButton.setEnabled(true);
                }
            }
          //  MyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String query= "";
                query = chatText.getText().toString();
                //sendChatMessage(query);
                sendRequest(query);

              // sendResponse(result.getFulfillment().getSpeech());
            }
        });
try {
    listenButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            aiService.startListening();
        }
    });
}catch(Exception e){
    System.out.println(e);
}
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });




        final AIConfiguration config = new AIConfiguration(this.getConfig(),
                AIConfiguration.SupportedLanguages.PortugueseBrazil,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        //addition
        aiDataService = new AIDataService(this, config);

        aiService.setListener(this);

        sendResponse(this.getFalaInicial());
        TTS.speak(this.getFalaInicial());
    }
    public void setFalaInicial(String fala){
        this.falaInicial = fala;
    }
    public void BotFala(String fala){
        sendResponse(fala);
        TTS.speak(fala);
    }
    public String getFalaInicial(){
        return this.falaInicial;
    }

    public void setConfig(String config2){
//		"9b40184ce27d4ad5801a48662f609db6"
        this.config = config2;
    }
    public String getConfig(){
        return this.config;
    }
    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {


                } else {

                }
                return;
            }
        }
    }

    private boolean sendResponse(String text) {
        if (text.length() == 0)
            return false;
        chatArrayAdapter.add(new ChatMessage(!rightSide, text));
        return true;
    }

    private boolean sendChatMessage(String text) {
        if (text.length() == 0)
            return false;
        chatArrayAdapter.add(new ChatMessage(rightSide, text));
        chatText.setText("");
        return true;
    }
    public void addAcao(Acao ac){
        this.acoes.add(ac);
    }
    public void onResult(final AIResponse response) {
        result = response.getResult();

        sendChatMessage(response.getResult().getResolvedQuery());


        for(Acao a : this.acoes) {
            if(result.getAction().equals(a.getNome())){
                a.setResult(result);
                a.executaAcao();
            }
        }
         if(result.getAction().equals("alarm")){

            String []time;
            time = result.getStringParameter("time").split(":");
            createAlarm("Marikota Alarme",Integer.parseInt(time[0]),Integer.parseInt(time[1]));
            sendResponse("Alarme programado para às: " +result.getStringParameter("time"));
            TTS.speak("Alarme Ativado");
        }
        else if(result.getAction().equals("timer")){

            String msg;
            msg = result.getStringParameter("any");
            int duration = Integer.parseInt(result.getStringParameter("any1"));
            if(msg == "")
                msg = "Timer";
            startTimer(msg,duration*60);
            sendResponse("Timer set for " + result.getStringParameter("duration") +" minutes");
            TTS.speak("Timer Started.");
        }

        else
        {
            if(result.getFulfillment().getSpeech() == ""){
                sendResponse("Desculpe,não obtive respostas do meu banco de dados para o que você disse."
                        );
                TTS.speak("Desculpe,não obtive respostas do meu banco de dados para o que você disse.");//
            }else {
                sendResponse(result.getFulfillment().getSpeech());//
                TTS.speak(result.getFulfillment().getSpeech());//
            }
            }


    }

    private void sendRequest(String s) {

        final String queryString = s;
        Log.d(TAG, queryString);

        final AsyncTask<String, Void, AIResponse> task = new AsyncTask<String, Void, AIResponse>() {

            private AIError aiError;

            @Override
            protected AIResponse doInBackground(final String... params) {
                final AIRequest request = new AIRequest();
                String query = params[0];


                //String event = params[1];

                if (!TextUtils.isEmpty(query))
                    request.setQuery(query);

                try {
                    return aiDataService.request(request);
                } catch (final AIServiceException e) {
                    aiError = new AIError(e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final AIResponse response) {
                if (response != null) {
                    onResult(response);
                } else {
                    onError(aiError);
                }
            }
        };
        task.execute(queryString);
    }


   public void createAlarm(String message, int hour, int minutes) {

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes)
                .putExtra(AlarmClock.EXTRA_SKIP_UI,TRUE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    //Timer
    public void startTimer(String message, int seconds) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }






    @Override
    public void onError(AIError error) { // here process error
        sendResponse(error.toString());
    }

    @Override
    public void onAudioLevel(float level) { // callback for sound level visualization

    }

    @Override
    public void onListeningStarted() { // indicate start listening here

    }

    @Override
    public void onListeningCanceled() { // indicate stop listening here

    }

    @Override
    public void onListeningFinished() { // indicate stop listening here

    }
}
