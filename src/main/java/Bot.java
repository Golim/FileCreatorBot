import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Bot {

    public static void main(String[] args) {

        ApiContextInitializer.init(); //Inizializza le api per poter sviluppare

        TelegramBotsApi botsApi = new TelegramBotsApi(); //Crea il bot

        try{

            botsApi.registerBot(new MyBot()); //Registra il bot con gli attributi

        }catch (TelegramApiException e) {

            e.printStackTrace();

        }

    }

}