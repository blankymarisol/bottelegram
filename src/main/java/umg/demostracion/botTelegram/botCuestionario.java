package umg.demostracion.botTelegram;

import umg.demostracion.model.Cuestionario;
import umg.demostracion.model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import umg.demostracion.service.CuestionarioService;
import umg.demostracion.service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class botCuestionario extends TelegramLongPollingBot {
    private Map<Long, String> estadoConversacion = new HashMap<>();
    User usuarioConectado = null;
    UserService userService = new UserService();

    private final Map<Long, Integer> indicePregunta = new HashMap<>();
    private final Map<Long, String> seccionActiva = new HashMap<>();
    private final Map<String, String[]> preguntas = new HashMap<>();

    public botCuestionario() {
        // Inicializa los cuestionarios con las preguntas.
        preguntas.put("SECTION_1", new String[]{"🤦‍♂️1.1- Estas aburrido?", "😂😂 1.2- Te bañaste hoy?", "🤡🤡 Pregunta 1.3"});
        preguntas.put("SECTION_2", new String[]{"Te gusta Taylor Swift?🤔 2.1", "Por que tu cancion favorita es All Too Well? 😪 2.2", "El karma es un gato? 😼 2.3"});
        preguntas.put("SECTION_3", new String[]{"Te gusta Getaway Car? 🚗 3.1"});
        preguntas.put("SECTION_4", new String[]{"4.1- Como estas?","4.2- Cual es tu edad?","4.3- Estas en la computadora?","4.4- PERO SI TE GUSTA TAYLOR SWIFT??????????"});
    }

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
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();
                String userFirstName = update.getMessage().getFrom().getFirstName();
                String userLastName = update.getMessage().getFrom().getLastName();
                String nickName = update.getMessage().getFrom().getUserName();

                // Verificar si el usuario está registrado
                String state = estadoConversacion.getOrDefault(chatId, "");
                usuarioConectado = userService.getUserByTelegramId(chatId);

                if (usuarioConectado == null) {
                    // Proceso de registro
                    if (state.isEmpty()) {
                        sendText(chatId, "Hola " + formatUserInfo(userFirstName, userLastName, nickName) + ", no tienes un usuario registrado en el sistema. Por favor ingresa tu correo electrónico:");
                        estadoConversacion.put(chatId, "ESPERANDO_CORREO");
                        return;
                    } else if (state.equals("ESPERANDO_CORREO")) {
                        processEmailInput(chatId, messageText);
                        return;
                    }
                } else {
                    // Usuario ya registrado, manejo de cuestionario
                    if (messageText.equals("/menu")) {
                        sendMenu(chatId);
                        return;
                    } else if (seccionActiva.containsKey(chatId)) {
                        enviarRespuesta(seccionActiva.get(chatId), indicePregunta.get(chatId), messageText, chatId);
                        manejaCuestionario(chatId, messageText);
                        return;
                    } else {
                        sendText(chatId, "Hola " + formatUserInfo(userFirstName, userLastName, nickName) + ", envía /menu para iniciar el cuestionario.");
                    }
                }
            } else if (update.hasCallbackQuery()) {
                // Manejo de respuestas de botones
                String callbackData = update.getCallbackQuery().getData();
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                inicioCuestionario(chatId, callbackData);
            }
        } catch (Exception e) {
            // Manejo de errores
            long chatId = update.getMessage().getChatId();
            sendText(chatId, "Ocurrió un error al procesar tu mensaje. Por favor intenta de nuevo.");
            e.printStackTrace();  // Loguear la excepción
        }
    }


    private void sendMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Selecciona una sección:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Crea los botones del menú
        rows.add(crearFilaBoton("Sección 1", "SECTION_1"));
        rows.add(crearFilaBoton("Sección 2", "SECTION_2"));
        rows.add(crearFilaBoton("Sección 3", "SECTION_3"));
        rows.add(crearFilaBoton("Sección 4", "SECTION_4"));

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private List<InlineKeyboardButton> crearFilaBoton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);
        return row;
    }

    private void inicioCuestionario(long chatId, String section) {
        seccionActiva.put(chatId, section);
        indicePregunta.put(chatId, 0);
        enviarPregunta(chatId);
    }

    private void enviarPregunta(long chatId) {
        String seccion = seccionActiva.get(chatId);
        int index = indicePregunta.get(chatId);
        String[] questions = preguntas.get(seccion);

        if (index < questions.length) {
            sendText(chatId, questions[index]);
        } else {
            sendText(chatId, "¡Has completado el cuestionario!");
            seccionActiva.remove(chatId);
            indicePregunta.remove(chatId);
        }
    }

    private void manejaCuestionario(long chatId, String response) {
        String section = seccionActiva.get(chatId);
        int index = indicePregunta.get(chatId);
        if (indicePregunta.get(chatId) == 1 ) {
            int intresponse = Integer.parseInt(response);
            if (intresponse<5){
                sendText(chatId, "Tu respuesta fue: " + response);
                sendText(chatId, "No jodas, estas muy chiquito\nPor favor tienes que ser mayor de edad!!!!!");
                enviarPregunta(chatId);
            } else if (intresponse>95) {
                sendText(chatId, "Tu respuesta fue: " + response);
                sendText(chatId,"aqui no es Jurassic Park, baboso!🙄\nPorfavor colocar otra edad");
                enviarPregunta(chatId);
            }else {
                siguientepregunta(chatId,response,index);
            }
        } else{
            siguientepregunta(chatId,response,index);
        }
    }
    private void enviarRespuesta(String seccion,Integer preguntaid, String response,Long telegramid) {
        CuestionarioService cuestionarioService =new CuestionarioService();
        Cuestionario cuestionario = new Cuestionario();

        // Crear un nuevo usuarioUseruser=newUser();
        cuestionario.setSeccion(seccion);
        cuestionario.setPreguntaid(preguntaid);
        cuestionario.setResponse(response);
        cuestionario.setTelegramid(telegramid);

        try {
            cuestionarioService.crearUsuario(cuestionario);
            System.out.println("User created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void siguientepregunta(long chatId,String response,int index) {
        sendText(chatId, "Tu respuesta fue: " + response);
        indicePregunta.put(chatId, index + 1);
        enviarPregunta(chatId);
    }

    private String formatUserInfo(String firstName, String lastName, String userName) {
        return firstName + " " + lastName + "";
    }

    private void processEmailInput(long chat_id, String email) {
        sendText(chat_id, "Recibo su Correo: " + email);
        estadoConversacion.remove(chat_id);
        try{
            usuarioConectado = userService.getUserByEmail(email);
        } catch (Exception e) {
            System.err.println("Error al obtener el usuario por correo: " + e.getMessage());
            e.printStackTrace();
        }
        if (usuarioConectado == null) {
            sendText(chat_id, "El correo no se encuentra registrado en el sistema, por favor contacte al administrador.");
        } else {
            usuarioConectado.setTelegramid(chat_id);
            try {
                userService.updateUser(usuarioConectado);
            } catch (Exception e) {
                System.err.println("Error al actualizar el usuario: " + e.getMessage());
                e.printStackTrace();
            }
            sendText(chat_id, "Usuario actualizado con éxito!");
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
