package sample.viewer;

import CommonClass.SharedCar;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.ServerConnect;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class SearchByRegController {

    @FXML
    private TextField reg_num;

    @FXML
    private TextField year_made;

    @FXML
    private ImageView img_show;

    @FXML
    private TextField colour_1;

    @FXML
    private TextField colour_2;

    @FXML
    private TextField colour_3;

    @FXML
    private TextField make;

    @FXML
    private TextField model;

    @FXML
    private TextField price;

    @FXML
    private TextField quantity;

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
    void searchCar(ActionEvent event) {

        String regNum = reg_num.getText();
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
                        SharedCar car = (SharedCar) objectInputStream.readObject();
                        if(car != null){
                            year_made.setText(Integer.toString(car.getYear_made()) );
                            colour_1.setText(car.getColour1());
                            colour_2.setText(car.getColour2());
                            colour_3.setText(car.getColour3());
                            make.setText(car.getCar_make());
                            model.setText(car.getCar_model());
                            price.setText(Integer.toString(car.getPrice()));
                            quantity.setText(Integer.toString(car.getQuantity()));
                            img_show.setImage(new Image(new ByteArrayInputStream(car.getImage())));

                        }else{
                            showAlert(Alert.AlertType.INFORMATION,"NotFound","No Car Exists please return to main menu");
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
