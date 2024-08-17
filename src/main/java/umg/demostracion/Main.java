package umg.demostracion;

import umg.demostracion.botTelegram.BotTarea;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args)
    {
        try{
            TelegramBotsApi botApi=new TelegramBotsApi(DefaultBotSession.class);
            BotTarea bot = new BotTarea();
            botApi.registerBot(bot);
        }
        catch(Exception ex )
        {
            System.out.println("error"+ex.getMessage());

        }
    }
}