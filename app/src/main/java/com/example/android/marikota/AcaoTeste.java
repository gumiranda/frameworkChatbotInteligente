package com.example.android.marikota;

import chatbotbiblio.modulochatbot.Acao;

/**
 * Created by Samsung on 19/04/2019.
 */

public class AcaoTeste extends Acao {

    public AcaoTeste(String nome) {
        super(nome);
    }

    @Override
    public void executaAcao(){
        if(this.RetornaRespostaBot() == "teste"){
            System.out.println(this.RetornaRespostaBot());
//faz alguma coisa de acordo com a resposta do bot
        }
        if(this.nome == "teste"){
            //faz tal coisa

            String parametroValor = this.retornaParametro("parametro");
            //faz alguma coisa com o parametro
System.out.println(parametroValor);
        }
        else if(this.nome == "teste2"){

            String parametroValor = this.retornaParametro("parametro2");
            //faz alguma coisa com o parametro
            System.out.println(parametroValor);
            //faz outra coisa
        }
    }
}
