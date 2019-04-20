package chatbotbiblio.modulochatbot;


import ai.api.model.Result;

public class Acao {
    public String nome;
    public Result res;
    public String getNome(){ return this.nome; }
    public Acao(String nome){
        this.nome = nome;
    }

    public Result getResult(){
        return this.res;
    }
    public void setResult(Result res){
        this.res = res;
    }
    public String retornaParametro(String parametro){
        return this.getResult().getStringParameter(parametro);
    }
    public void executaAcao(){

    }

    private boolean enviaResposta(String text) {
        if (text.length() == 0)
            return false;
        MainActivity m = new MainActivity();

        m.chatArrayAdapter.add(new ChatMessage(!m.rightSide, text));
        return true;
    }
    public void BotFala(String fala){
        MainActivity.sendResponse(fala);
        TTS.speak(fala);
    }


    public String RetornaRespostaBot(){
        return this.getResult().getFulfillment().getSpeech();
    }

}
