package umg.demostracion;

import botTelegram.Bot;
import botTelegram.PokemonBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {


    public static void main(String[] args)
    {
        try{
            TelegramBotsApi botApi=new TelegramBotsApi(DefaultBotSession.class);
             //Bot bot = new Bot();
            PokemonBot poke = new PokemonBot();
            System.out.println("Si jala chido.........");
            botApi.registerBot(poke);

        }

        catch(Exception ex )
        {
            System.out.println("error"+ex.getMessage());


        }




    }

}