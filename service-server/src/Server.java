import io.karthik.Messanger.db.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.lang.System.out;

public class Server {
    private static ArrayList<String> users = new ArrayList<>();
    private static ArrayList<MessagingThread> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException, SQLException {
        ServerSocket serverSocket = new ServerSocket(9090,10);
        out.println("Server is running!");
        Database.createUserTable("users");
        Database.createChatTable("chat_backup");
        while (true) {
            Socket client = serverSocket.accept();
            MessagingThread thread = new MessagingThread(client);
            clients.add(thread);
            thread.start();
        }
    }

    public static void sendToAll(String user, String msg) {
        for (MessagingThread c: clients) {
            if (!c.getUser().equals(user))
                c.sendMessage(user, msg);
            else
                c.sendToMe(user,msg);
        }
    }

    private static class MessagingThread extends Thread {

        String user = "";
        BufferedReader input;
        PrintWriter output;

        public MessagingThread(Socket client) throws IOException, SQLException {
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);
            user = input.readLine();
            users.add(user);
            Database.addUserToDb(user);
        }

        @Override
        public void run() {
            String line;
            try {
                while(true) {
                    line = input.readLine();
                    if(line.equals("end")) {
                        clients.remove(this);
                        users.remove(user);
                        break;
                    } else {
                        sendToAll(user, line);
                        saveInDb(user, line);
                    }
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }

        public String getUser() {
            return user;
        }

        public void saveInDb(String user, String msg) throws SQLException {
            String msg_id = user + "_" + System.currentTimeMillis();
            Database.chatBackUp(user, msg_id, msg);
        }

        public void sendMessage(String user, String msg) {
            output.println(user + ": " + msg);
        }

        public void sendToMe(String user, String msg) {
            output.println("You: " + msg);
        }
    }
}

