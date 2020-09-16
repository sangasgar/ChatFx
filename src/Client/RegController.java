package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RegController {
    @FXML
    private TextArea textArea1;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField nickField;
    private Controller controller;

    public void tryToReg(ActionEvent actionEvent) {
        controller.tryToReg(loginField.getText().trim(),passwordField.getText().trim(),nickField.getText().trim());
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
    public void addMsgToTextArea(String msg) {
        textArea1.appendText(msg + "\n");
    }
}
