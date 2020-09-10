package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public TextArea textArea;
    @FXML
    public TextField textField;
    private final String IP_ADDRESS = "localhost";
    private int PORT = 8189;
    private Socket socket;
    DataInputStream in;
    DataOutputStream out;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            socket = new Socket(IP_ADDRESS,PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(new Runnable(){
               @Override
               public void  run() {
               try {
                   while (true) {
                       String str = in.readUTF();
                       if(str.equals("/end")){
                           break;
                       }
//                       System.out.println("Сервер: " + str);
                       textArea.appendText(str + "\n");
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               } finally {
                   System.out.println("Мы отключились от сервера");
                   try {
                       socket.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }

            }).start();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
        public void sendMsg(ActionEvent actionEvent) {
            try {
                out.writeUTF(textField.getText());
                textField.clear();
                textField.requestFocus();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}