package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;



    private String nickname;
    private String login;

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    socket.setSoTimeout(120000);
                    //цикл аутентификации
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/auth ")) {
                            String[] token = str.split("\\s");
                            if (token.length < 3) {
                                continue;
                            }

                            String newNick = server
                                    .getAuthService()
                                    .getNicknameByLoginAndPassword(token[1], token[2]);
                            login = token[1];
                            if (newNick != null) {
                                if(!server.isLoginAuthenticated(login)){
                                    nickname = newNick;
                                    sendMsg("/authok " + nickname);
                                    server.subscribe(this);
                                    System.out.println("Клиент " + nickname + " подключился");
                                    break;
                                } else {
                                sendMsg("С данной учетной записью прошли аутентификацию.");
                            }

                            } else {
                                sendMsg("Неверный логин / пароль");
                            }
                        }
                        if (str.startsWith("/reg ")) {
                            String[] token = str.split("\\s");
                            if (token.length < 4) {
                                continue;
                            }
                            boolean b = server.getAuthService().registration(token[1],token[2],token[3]);
                            if (b) {
                                sendMsg("/regok");
                            } else {
                                sendMsg("regno");
                            }
                        }
                    }

                    //цикл работы
                    while (true) {
                        String str = in.readUTF();

                            if (str.equals("/end")) {
                                out.writeUTF("/end");
                                break;
                            }
                            if(str.startsWith("/w")) {
                                String[] tok = str.split("\\s+", 3);
                                if (tok.length < 3) {
                                    continue;
                                }
                                server.privateMsg(this, tok[2],tok[1]);

                            }
                            if(!str.startsWith("/w")) {
                                server.broadcastMsg(this, str);

                        }


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        socket.setSoTimeout(0);
                    } catch (SocketException socketException) {
                        socketException.printStackTrace();
                        try {
                            socket.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                } finally {
                    System.out.println("Клиент отключился");
                    server.unsubscribe(this);
                    try {
                        socket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("Клиент " + this.getNickname() + " отключился по таймауту");
                        sendMsg("Вы отключены по таймауту");
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }
}
