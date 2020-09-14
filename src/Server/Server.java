package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
public class Server {
    private List<ClientHandler> clients;
    private AuthService authService;

    private int PORT = 8189;
    ServerSocket server = null;
    Socket socket = null;

    public Server(){
        clients = new Vector<>();
        authService = new SimpleAuthService();

        try {
            server = new ServerSocket(PORT);
            System.out.println("Сервер запущен");

            while (true) {
                socket = server.accept();
                System.out.println("Клиент подключился");

                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public void broadcastMsg(ClientHandler sender, String msg){

        String message = String.format(sender.getNickname() + " : " +  msg);

            for (ClientHandler c : clients) {
                    c.sendMsg(message);

            }

    }

    public void privateMsg(ClientHandler sender, String msg, String res){

        for (ClientHandler c : clients) {
            if (c.getNickname().equals(res)) {
                c.sendMsg(sender.getNickname() + " private for " + res + ": " + msg );
                if(!c.equals(sender)) {
                    sender.sendMsg(msg);
                }
                return;
            }
        }
        sender.sendMsg("Пользователь не найден");

    }

    public void subscribe(ClientHandler clientHandler){
        clients.add(clientHandler);
    }

    public void unsubscribe(ClientHandler clientHandler){
        clients.remove(clientHandler);
    }

}
