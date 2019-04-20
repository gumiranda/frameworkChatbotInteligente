# DOCUMENTAÇÃO Framework Android Chatbot Inteligente usando a API DialogFlow 

O framework chatbot inteligente facilita a integração com a API de processamento de linguagem natural DialogFlow em dispositivos Android. Com ela podemos criar diferentes cenários de diálogos que interagem com o usuário diretamente através de intents e possíveis ações definidas pelo programador.
Através do framework é possível definir os comportamentos resultantes de ações programadas e utilizar seus parâmetros.

Para utilizar o framework inclua em seu projeto a pasta 'modulochatbot' presente no código fonte.
Dentro do arquivo **build.gradle** presente na pasta **app** do projeto, inclua a referência da pasta do framework nas suas dependências.

```
    compile project(path: ':modulochatbot')
```

Para integrar o aplicativo com o seu projeto do DialogFlow é preciso configurar o app passando seu client access token que é gerado pela plataforma e está presente na tela de configuração na parte inferior(Como ilustrado abaixo).
Selecione na tela de configurações a versão 1 da api(V1 API).
![Client Access Token](clientkey.png)


* [Executando o código de exemplo](#codigo_exemplo)
* [Começando com o seu próprio app](#seu_proprio_app)
    * [Primeira classe](#primeira_classe)
    * [Segunda classe](#segunda_classe)
    * [Classe AcaoTeste](#classe_acao_teste)
* [Executando o App](#executando_app)

# <a name="codigo_exemplo" />Executando o código de exemplo do framework chatbot inteligente

O framework chatbot inteligente traz para o usuário um exemplo de código que exemplifica o uso de ações e parâmetros para que o usuário possa manipular e personalizar o comportamento do bot criado na plataforma DialogFlow.
Use os seguintes passos para executar o código de exemplo:
1. Crie um agente inteligente na plataforma DialogFlow .Para mais detalhes consulte a [Documentação oficial](http://dialogflow.com/docs).
2. Abra o [Android Studio](https://developer.android.com/sdk/installing/studio.html).
Preferencialmente na versão 2.3.3.
3. Vá até **Ferramentas>Android>SDK Manager** e certifique-se de que a build tools 25.0.0 está instalada. Caso não esteja, instale e a referencie no arquivo build.gradle presente na pasta 'app' do projeto
4. Dentro do arquivo MainActivity presente na pasta **app/src/main/java/com/example/marikota/** acrescente no método **setConfig** seu client access token obtido anteriormente .
5. Vincule um dispositivo Android ativando seu modo de debug. Caso não tenha dispositivo crie um emulador de máquina virtual Android no Android Studio para executar o app.
6. Clique no menu **Run** **>** **Debug** e escolha o seu dispositivo.
7. Escreva textos na parte inferior da tela no app e clique no botão inferior direito para enviar as mensagens ao bot.


# <a name="seu_proprio_app" />Começando com o seu próprio app

Essa seção descreve o que é preciso fazer para integrar seu app com o framework chatbot inteligente.
Primeiramente é necessária a criação de duas classes Activity no projeto. Uma irá chamar o splash screen presente no framework. A outra é invocada por essa classe após o splash screen do framework ser executado.

## <a name="primeira_classe" />Primeira classe
Em primeiro lugar é preciso importar o módulo presente na pasta 'modulochatbot' que você incluiu anteriormente em seu projeto.
```
import chatbotbiblio.modulochatbot.*;
```
Após isso é preciso definir como layout da classe o layout definido no framework para o splash screen.
```
        setContentView(chatbotbiblio.modulochatbot.R.layout.activity_splash_screen);
```
Logo depois criamos uma thread que irá invocar a segunda classe a ser implementada a seguir.
```
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
```

## <a name="segunda_classe" />Segunda classe

A segunda classe necessariamente deve extender a classe principal do framework, como mostrado abaixo:

```
public class MainActivity extends chatbotbiblio.modulochatbot.MainActivity {
```
Dentro da sobrescrita do método principal onCreate recomenda-se o uso do seguinte modelo de uso:
```
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setConfig("SEU ACCESS TOKEN AQUI");
        super.setFalaInicial("Olá");
        Acao ac = new AcaoTeste("teste");
        super.addAcao(ac);
        super.onCreate(savedInstanceState);
    }
```

Com o método **setConfig()** devemos passar como parâmetro nosso client access token gerado pela API DialogFlow. Já no método **setFalaInicial** configuramos a fala inicial do bot criado.
Nas linhas abaixo, com a criação de uma classe de exemplo AcaoTeste que extende a classe Acao do framework, devemos instanciar essas classes de acordo com as ações criadas dentro das intents da plataforma.
Caso não haja nenhuma , a criação da classe não é necessária.
Através da criação de instâncias da classe AcaoTeste é possível trabalhar com ações definidas dentro das intents da plataforma DialogFlow.
Para isso é preciso passar como construtor da classe AcaoTeste o mesmo nome das ações criadas na plataforma DialogFlow.

## <a name="classe_acao_teste" />Classe AcaoTeste

Para definir o comportamento das ações entre na classe AcaoTeste modifique o método sobrescrito **executaAcao()** de acordo com os nomes das ações criadas na classe principal da aplicação.
Para trabalhar com parâmetros use o método **retornaParametro** como ilustrado no exemplo a seguir.

```
import chatbotbiblio.modulochatbot.Acao;

public class AcaoTeste extends Acao {

    public AcaoTeste(String nome) {
        super(nome);
    }

    @Override
    public void executaAcao(){
        if(this.RetornaRespostaBot() == "resposta-retornada-pelo-bot"){
        }
        if(this.nome == "nome-da-acao"){
            //define o comportamento da ação
            String parametroValor = this.retornaParametro("nome-do-parametro");
        }
        else if(this.nome == "nome-da-acao-2"){
            //define o comportamento da ação
		String parametroValor = this.retornaParametro("nome-do-parametro2");
            //faz alguma coisa com o parametro
        }
    }
}
```
Como podemos observar, seguindo essa estrutura na sobrescrita do método **executaAcao()** podemos definir os comportamentos das ações criadas na classe principal da aplicação.
Através da estrutura de ifs como exemplo, definimos um código personalizado para tratar diversas situações que o bot pode encontrar.
A partir do método **RetornaRespostaBot** nós recuperamos as falas geradas pelo bot para responder a interação com o usuário. Com isso ,dentro do if ilustrado no código acima pode-se definir o tratamento de diversas situações a partir de uma resposta fixada na API DialogFlow.


## <a name="executando_app" />Executando o app

1. Vincule um dispositivo Android no seu computador, ou crie uma máquina virtual Android pelo AndroidStudio.7
2. Certifique-se de que seu dispositivo Android está com o modo debug ativado e ligado ao computador.Ou verifique se sua máquina virtual está pronta.
3. Selecione sua aplicação para executar no Android Studio e clique no botão Debug
4. O app deve executar em instantes. 
Escreva textos na parte inferior da tela no app e clique no botão inferior direito para enviar as mensagens ao bot.
