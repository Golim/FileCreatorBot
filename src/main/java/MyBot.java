import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.*;

public class MyBot extends TelegramLongPollingBot {
    boolean isFirstTime = true;
    int state = 0;
    /*
    0: in ascolto
    1: inserire nome
    2: inserire testo
     */

    Long sender_id;
    String received_text;
    File file;
    SendDocument document;
    String file_name, file_text;
    String caption;
    FileWriter writer;

    File dir = new File(MyBot.class.getProtectionDomain().getCodeSource().getLocation().getPath());

    public void onUpdateReceived(Update update) {
        sender_id = update.getMessage().getChatId();

        if (isFirstTime && false) {
            sender_id = update.getMessage().getChatId();
            mySendMessage("Ciao, sono " + getBotUsername() + ", posso creare dei file di testo per te!\n\nScrivi \"New\" per crearne uno!", sender_id);
            isFirstTime = false;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().toLowerCase().contains("annulla")) {
                mySendMessage("Ok, ho annullato", sender_id);
                reset();
            } else if (update.getMessage().getText().toLowerCase().contains("help")) {
                mySendMessage("Ciao, sono " + getBotUsername()
                                + ", posso creare dei file di testo per te!\n\nScrivi \"New\" per crearne uno!"
                                + "\nScrivi \"annulla\" per annullare l'operazione",
                        sender_id);

                reset();
            } else if (state == 0) {
                received_text = update.getMessage().getText();
                if ((received_text).toLowerCase().contains("new")) {
                    state = 1;
                    mySendMessage("Inserisci il nome del file con l'estensione\n(Es: file.txt)", sender_id);
                } else
                    mySendMessage("Prova a scrivere \"new\"", sender_id);
            } else if (state == 1) {
                file_name = update.getMessage().getText();
                state = 2;
                mySendMessage("Inserisci il testo del file", sender_id);
            } else if (state == 2) {
                sender_id = update.getMessage().getChatId();
                file_text = update.getMessage().getText();
                caption = "File " + file_name + " created by FileCreator_bot";
                document = new SendDocument();
                document.setChatId(sender_id);
                document.setCaption(caption);
                try {
                    document.setNewDocument(file_name, new ByteArrayInputStream(file_text.getBytes("UTF-8")));
                    sendDocument(document);

                    System.out.println("File created: " + file_name + "\nText: \n" + file_text);
                    System.out.println("\n");
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reset();
            } else {
                mySendMessage("Scusa, non ho capito, prova a scrivere \"new\"!", sender_id);
            }
        }

    }

    public void mySendMessage(String string, long sender_id) {
        SendMessage message = new SendMessage();
        message.setChatId(sender_id);
        message.setText(string);
        try {
            //TODO: trovare funzione non deprecated per send message
            sendMessage(message);

        } catch (TelegramApiException e) {

            e.printStackTrace();

        }
    }

    public void reset() {
        file_text = "";
        file_name = "";
        state = 0;
    }

    public String getBotUsername() {
        //Nome del bot
        return "FileCreator Bot";

    }

    public String getBotToken() {
        String token = System.getenv("telegram_bot_token");
        //Token per lo sviluppo del bot
        return token;
    }

}
