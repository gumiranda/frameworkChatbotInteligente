package com.example.android.marikota;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;

import chatbotbiblio.modulochatbot.*;

public class SplashScreen extends AppCompatActivity {//extends chatbotbiblio.modulochatbot.SplashScreen{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(1);
        setContentView(chatbotbiblio.modulochatbot.R.layout.activity_splash_screen);

        Thread myThread = new Thread(){
            @Override
            public void run(){
                try{
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
      //  chatbotbiblio.modulochatbot.MainActivity m =  new  chatbotbiblio.modulochatbot.MainActivity();
       // m.setConfig("9b40184ce27d4ad5801a48662f609db6");
        //m.setFalaInicial("Olá");
        super.onCreate(savedInstanceState);

    }
}
