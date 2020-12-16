package sample.viewer;

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

public class BuyCarController {

    @FXML
    private TextField reg_num;

    @FXML
    private Button buyBtn;

    @FXML
    void backToMain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/UserScene.fxml"));
            Stage stage = (Stage) reg_num.getScene().getWindow();
            Scene scene = null;
            scene = new Scene(loader.load(), 700,460);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buyCar(ActionEvent event) {

        String regNum = reg_num.getText();
        if(regNum.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please reg num");
        }else{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(ServerConnect.getInstance().getSocket().getOutputStream());
                        objectOutputStream.writeObject(regNum);
                        objectOutputStream.flush();
                        ObjectInputStream objectInputStream = new ObjectInputStream(ServerConnect.getInstance().getSocket().getInputStream());

                        String result = (String) objectInputStream.readObject();
                        if(result.equalsIgnoreCase("success")){
                            showAlert(Alert.AlertType.INFORMATION,"Success", "Successfully buy Please return to main menu");
                        }else{
                            showAlert(Alert.AlertType.ERROR,"Failed", "Can not buy");
                        }
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
