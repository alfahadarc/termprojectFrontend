package sample.admin;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.ServerConnect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RemoveController {

    @FXML
    private TextField userName;

    @FXML
    private Button removeBtn;

    @FXML
    void removeUser(ActionEvent event) {
        //System.out.println(userName.getText());
        if(userName.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR,"Invalid input", "Username must not be empty");
        }else{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(ServerConnect.getInstance().getSocket().getOutputStream());
                        List<String> clientResponse_again = new ArrayList<>();
                        clientResponse_again.add(userName.getText());
                        objectOutputStream.writeObject(clientResponse_again);
                        objectOutputStream.flush();

                        ObjectInputStream objectInputStream = new ObjectInputStream(ServerConnect.getInstance().getSocket().getInputStream());
                        String response = (String) objectInputStream.readObject();
                        System.out.println(response);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }


    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
