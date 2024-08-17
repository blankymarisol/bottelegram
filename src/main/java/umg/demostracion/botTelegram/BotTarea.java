package umg.demostracion.botTelegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class BotTarea extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "@blanksss_bot";
    }

    @Override
    public String getBotToken() {
        return "7326662711:AAFgPZsxfxP2JsZQnqMRyVQweBMZNdUs3Fc";
    }

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

            if (message_text.startsWith("/start")) {
                String welcomeMessage = "Bienvenido, " + nickname + " " + apellido + "!\n";
                welcomeMessage += "Aqui tienes el menu de opciones disponibles;\n" +
                        "/info - Informacion personal\n" +
                        "/progra - Informacion sobre el curso de programacion\n" +
                        "/hola - Saludo\n" +
                        "/cambio [cantidad] - convertir euros a quetzalez\n" +
                        "/grupo - Enviar un mensaje al grupo\n" +
                        "Si tienes acceso a este bot, eres importante y te quiero mucho <3";
                sendText(chat_id, welcomeMessage);
            } else if (message_text.startsWith("/info")) {
                    sendText(chat_id, "Blanky Marisol Lopez Marroquin, 4to Semestre, 0905-23-5227");
                    System.out.println("Blanky Marisol Lopez Marroquin, 4to Semestre, 0905-23-5227");
            }else if (message_text.startsWith("/progra")) {
                    sendText(chat_id, "Es un curso en el cual aprendemos como una base sobre el mundo de la programacion ");
                    System.out.println("Es un curso en el cual aprendemos como una base sobre el mundo de la programacion ");
            }else if (message_text.startsWith("/hola")) {
                    sendText(chat_id, "Hola " + nickname + apellido + " que haces por aqui hoy " + now + " ?");
                    System.out.println("Hola " + nickname + apellido + " que haces por aqui hoy " + now + " ?");
            }else if (message_text.startsWith("/cambio")) {
                    String[] parts = message_text.split(" ");
                    if (parts.length == 2) {
                        try {
                            double euros = Double.parseDouble(parts[1]);
                            double quetzales = euros * 8.53;
                            sendText(chat_id, euros + " euros son " + quetzales + " quetzales.");
                            System.out.println("Euros son " + quetzales + " quetzales.");
                        } catch (NumberFormatException e) {
                            sendText(chat_id, "No ingresaste un numero valido");
                        }
                    } else {
                        sendText(chat_id, "Por favor, usa el formato que se indico: /cambio [cantidad en euros]");
                    }
                }else if (message_text.startsWith("/grupo")) {
                String message = "Bienvenido a mi grupito";
                List<Long> CHAT_IDS = Arrays.asList(6709392176L, 6922425377L, 6710213754L, 1526682027L);
                for (Long chatId : CHAT_IDS) {
                    try {
                        sendText(chat_id, message);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (update.hasMessage() && update.getMessage().hasText()) {
                System.out.println("Hola " + nickname + " tu apellido es: " + apellido);
                String message = update.getMessage().getText();
                long chatid = update.getMessage().getChatId();

                if (message_text.toLowerCase().equals("Hola")) ;
                System.out.println("User id: " + chatid + " Message: " + message);
            }
        }
    }


    public void sendText(Long who, String what) {
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