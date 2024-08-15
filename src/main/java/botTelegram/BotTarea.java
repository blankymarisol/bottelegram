package botTelegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class BotTarea extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {return "@blanksss_bot";}

    @Override
    public String getBotToken() {return "7326662711:AAFgPZsxfxP2JsZQnqMRyVQweBMZNdUs3Fc";}

    @Override
    public void onUpdateReceived(Update update) {

        String nombre = update.getMessage().getFrom().getUserName();
        String apellido = update.getMessage().getFrom().getLastName();
        String nickname = update.getMessage().getFrom().getFirstName();
        LocalDateTime now = LocalDateTime.now();

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            if (message_text.startsWith("/info")){
                sendText(chat_id, "Blanky Marisol Lopez Marroquin, 4to Semestre, 0905-23-5227");
                System.out.println("Blanky Marisol Lopez Marroquin, 4to Semestre, 0905-23-5227");
            }
            if (message_text.startsWith("/progra")){
                sendText(chat_id, "Es un curso en el cual aprendemos como una base sobre el mundo de la programacion ");
                System.out.println("Es un curso en el cual aprendemos como una base sobre el mundo de la programacion ");
            }
            if (message_text.startsWith("/hola")){
                sendText(chat_id, "Hola " + nickname + apellido + " que haces por aqui hoy " + now + " ?");
                System.out.println("Hola \" + nickname + apellido + \" que haces por aqui hoy \" + now + \" ?");
            }
        }
    }



    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }
}


