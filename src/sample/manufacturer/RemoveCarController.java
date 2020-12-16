package sample.manufacturer;

import CommonClass.SharedCar;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.ServerConnect;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

public class RemoveCarController {

    @FXML
    private TextField reg_num;

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
    void removeCar(ActionEvent event) {
            String regNum = reg_num.getText().toLowerCase(Locale.ROOT);
        if(regNum.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Give a Registration Number");
        }else{

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(ServerConnect.getInstance().getSocket().getOutputStream());
                        objectOutputStream.writeObject(regNum);
                        objectOutputStream.flush();

                        ObjectInputStream objectInputStream = new ObjectInputStream(ServerConnect.getInstance().getSocket().getInputStream());
                        String result= (String) objectInputStream.readObject();
                        if(result.equalsIgnoreCase("success")){
                            showAlert(Alert.AlertType.INFORMATION,"Successd","Car removed");
                        }else{
                            showAlert(Alert.AlertType.INFORMATION,"Failed","Not Removed");
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
