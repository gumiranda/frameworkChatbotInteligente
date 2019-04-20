package com.example.android.marikota;

import android.os.Bundle;

import chatbotbiblio.modulochatbot.Acao;

public class MainActivity extends chatbotbiblio.modulochatbot.MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
     System.out.println(3);
        super.setConfig("7a97da1eee1d4be1a2a15473d43dd98d");
        super.setFalaInicial("Olá");
        Acao ac = new AcaoTeste("teste");
        Acao ac2 = new AcaoTeste("teste2");
        super.addAcao(ac);
        super.addAcao(ac2);
        super.onCreate(savedInstanceState);

//        BotFala("TESTE");
    }
}
