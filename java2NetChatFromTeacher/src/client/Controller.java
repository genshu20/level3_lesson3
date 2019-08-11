package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Controller {
    @FXML
    public TextField textFild;
    @FXML
    public TextArea textArea;
    @FXML
    public Button button;
    @FXML
    public HBox upperPanel;
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public HBox bottomPanel;
    @FXML
    public ListView<String> clientList;

    boolean isAuthorized;
    String login;
    String nick;

    RegController regController=null;

//    private boolean isTormoz;
//    long x=System.currentTimeMillis();

    public void sendMsg(ActionEvent actionEvent) {
        try {
            out.writeUTF(textFild.getText());
            textFild.setText("");
            textFild.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Socket socket;
    DataOutputStream out;
    DataInputStream in;

    final String IP_ADRESS = "localhost";
    final int PORT = 8189;

    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
        if (isAuthorized) {
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            bottomPanel.setVisible(true);
            bottomPanel.setManaged(true);
            clientList.setVisible(true);
            clientList.setManaged(true);

            Platform.runLater(() -> textFild.requestFocus());
        } else {
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);
            bottomPanel.setVisible(false);
            bottomPanel.setManaged(false);
            clientList.setVisible(false);
            clientList.setManaged(false);
        }
    }

    public  void connect() {

        try {
            socket = new Socket(IP_ADRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = in.readUTF();
                            if (str.equals("/authOk")) {
                                setAuthorized(true);
                                textArea.clear();
                                break;
                            }
                            if (str.equals("/end")) {
                                System.out.println("client disconnect by tormoz");
                                throw new RuntimeException("client disconnect by tormoz");
                            }
                            textArea.appendText(str + "\n");
                        }
//                            if(!isAuthorized){
//                                socket.close();
//                                isTormoz=true;
//                            }

                        while (true) {
                            String str = in.readUTF();
                            if (str.startsWith("/")) {
                                if (str.equals("/end")) {
                                    System.out.println("client is disconnect");
                                    break;
                                }
                                if (str.startsWith("/clientlist")) {
                                    String[] token = str.split(" ");
                                    Platform.runLater(() -> {
                                        clientList.getItems().clear();
                                        for (int i = 1; i < token.length; i++) {
                                            clientList.getItems().add(token[i]);
                                        }
                                    });
                                }
                                if(str.startsWith("/get ")){
                                    String[] token = str.split(" ");
                                    login=token[1];
                                    nick=token[2];
                                }
                            } else textArea.appendText(str + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setAuthorized(false);
                    }

                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tryToAuth(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()) {
            connect();
        }
        try {
            out.writeUTF("/auth " + loginField.getText().trim() + " " + passwordField.getText().trim());
            loginField.clear();
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickClientList(MouseEvent mouseEvent) {
        String receiver = clientList.getSelectionModel().getSelectedItem();
        textFild.setText("/w " + receiver + " ");
    }

    public void tryToReg(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()) {
            connect();
        }
       if(regController==null){
           try {
               FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("regsample.fxml"));
               Parent root1=(Parent) fxmlLoader.load();
               Stage stage=new Stage();
               stage.initModality(Modality.APPLICATION_MODAL);

               RegController regController=fxmlLoader.getController();
               regController.controller=this;
               this.regController=regController;

               stage.setTitle("reg/edit");
               stage.setScene(new Scene(root1,300,300));
               stage.show();
               System.out.println(root1);


           } catch (IOException e) {
               e.printStackTrace();
           }
       }else{
           Stage stage=(Stage) regController.buttonClose.getScene().getWindow();
           stage.show();
       }

    }

    public  void tryToEdit(ActionEvent actionEvent) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("regsample.fxml"));
                Parent root1=(Parent) fxmlLoader.load();
                Stage stage=new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);

                RegController regController=fxmlLoader.getController();
                regController.controller=this;
                this.regController=regController;

                stage.setTitle("reg/edit");
                stage.setScene(new Scene(root1,300,300));
                regController.loginFild.setText(login);
                regController.nickFild.setText(nick);
                stage.show();
                System.out.println(root1);
            } catch (IOException e) {

            }

    }
}
