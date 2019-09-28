package simplechat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public static void sendBroadcastMessage(Message message) {
        for (Map.Entry<String, Connection> entry : connectionMap.entrySet()) {
            try {
                entry.getValue().send(message);
            } catch (IOException e) {
                System.out.println("Сообщение не отправлено!");
            }
        }
    }

    private static class Handler extends Thread {

        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            Message userName;

            do {
                connection.send(new Message(MessageType.NAME_REQUEST, "Введите ваше имя: "));
                userName = connection.receive();
                if (userName.getType() == MessageType.USER_NAME) {
                    if (!userName.getData().isEmpty() && !connectionMap.containsKey(userName.getData())) {
                        connectionMap.put(userName.getData(), connection);
                        connection.send(new Message(MessageType.NAME_ACCEPTED));
                        break;
                    } else {
//                        connection.send(new Message(MessageType.TEXT, "Имя пустое или уже существует"));
                    }
                } else {
//                    connection.send(new Message(MessageType.TEXT, "Неверный тип сообщения"));
                }
            } while (true);

            return userName.getData();
        }

        private void notifyUsers(Connection connection, String userName) throws IOException {
            for (Map.Entry<String, Connection> entry : connectionMap.entrySet()) {
                if (!entry.getKey().equals(userName)) {
                    connection.send(new Message(MessageType.USER_ADDED, entry.getKey()));
                }
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                Message newMessage = connection.receive();
                if (newMessage.getType() == MessageType.TEXT){
                    sendBroadcastMessage(new Message(MessageType.TEXT, userName + ": " + newMessage.getData()));
                }else {
                    ConsoleHelper.writeMessage("Нe правильный формат сообщения отличный от \"TEXT\"!");
                }

            }
        }

        public void run(){
            String userName = "";
            ConsoleHelper.writeMessage(String.valueOf(socket.getRemoteSocketAddress()));
            try (Connection connection = new Connection(socket)) {

                userName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName));
                notifyUsers(connection, userName);
                serverMainLoop(connection, userName);

            } catch (IOException | ClassNotFoundException e) {
                ConsoleHelper.writeMessage("Произошла ошибка при обмене данными с удаленным адресом");
            }finally {
                if (!userName.equals("")) {
                    connectionMap.remove(userName);
                    sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));
                }
            }
            ConsoleHelper.writeMessage("Соединение с удаленным адресом закрыто!");

        }

    }

    public static void main(String[] args) {

        int port = ConsoleHelper.readInt();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ConsoleHelper.writeMessage("Сервер запущен!");
            while (true) {
                Handler handler = new Handler(serverSocket.accept());
                handler.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
