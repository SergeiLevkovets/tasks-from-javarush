package simplechat.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BotClient extends Client {

    public class BotSocketThread extends SocketThread {
        @Override
        protected void processIncomingMessage(String message) {
            super.processIncomingMessage(message);
            if (message.contains(":")) {
                String[] mes = message.split(":");
                SimpleDateFormat dateFormat;
                switch (mes[1].trim()) {
                    case ("дата"):
                        dateFormat = new SimpleDateFormat("d.MM.YYYY");
                        break;
                    case ("день"):
                        dateFormat = new SimpleDateFormat("d");
                        break;
                    case ("месяц"):
                        dateFormat = new SimpleDateFormat("MMMM");
                        break;
                    case ("год"):
                        dateFormat = new SimpleDateFormat("YYYY");
                        break;
                    case ("время"):
                        dateFormat = new SimpleDateFormat("H:mm:ss");
                        break;
                    case ("час"):
                        dateFormat = new SimpleDateFormat("H");
                        break;
                    case ("минуты"):
                        dateFormat = new SimpleDateFormat("m");
                        break;
                    case ("секунды"):
                        dateFormat = new SimpleDateFormat("s");
                        break;
                    default:
                        dateFormat = null;
                }
                if (dateFormat != null) {
                    sendTextMessage("Информация для " + mes[0].trim() + ": " + dateFormat.format(Calendar.getInstance().getTime()));
                }
            }

        }

        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }
    }

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected String getUserName() {
        String userName = "date_bot_" + (int) (Math.random() * 100);
        return userName;
    }

    public static void main(String[] args) {
        com.javarush.task.task30.task3008.client.BotClient botClient = new com.javarush.task.task30.task3008.client.BotClient();
        botClient.run();
    }
}
