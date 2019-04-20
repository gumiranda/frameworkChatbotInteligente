package chatbotbiblio.modulochatbot;

/**
 * Created by kroppt on 10/5/2016.
 */

public class ChatMessage {
    public boolean rightSide;
    public String message;
    public String degree;

    public ChatMessage(boolean rightSide, String message) {
        super();
        this.rightSide = rightSide;
        this.message = message;
    }
    public ChatMessage(String degree, String message) {
        super();
        this.degree = degree;
        this.message = message;

    }
}