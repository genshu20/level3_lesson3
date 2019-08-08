package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegController {
    @FXML
    public TextField loginFild;
    @FXML
    public TextField passFild;
    @FXML
    public TextField nickFild;
    @FXML
    public Button buttonClose;
    @FXML
    public Button buttonReg;
    @FXML
    public Button buttonEdit;
    @FXML
    public TextField information;
    Controller controller;

    public void clickClose(ActionEvent actionEvent) {
        Stage stage=(Stage) buttonClose.getScene().getWindow();
        stage.close();
    }

    public void clickReg(ActionEvent actionEvent) {
        try {
            if(loginFild.getText().contains(" ")||passFild.getText().contains(" ")||nickFild.getText().contains(" ")){
                information.setText("invalid characters");
            }else{
                controller.out.writeUTF("/reg "+loginFild.getText()+" "+passFild.getText()+" "+nickFild.getText());
                information.setText("OK");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickEdit(ActionEvent actionEvent) {
        if(controller.isAuthorized){
            try {
                if(loginFild.getText().contains(" ")||passFild.getText().contains(" ")||nickFild.getText().contains(" ")){
                    information.setText("invalid characters");
                }else{
                    controller.out.writeUTF("/edit "+loginFild.getText()+" "+passFild.getText()+" "+nickFild.getText());
                    information.setText("OK");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
