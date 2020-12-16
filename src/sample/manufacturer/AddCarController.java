package sample.manufacturer;

import CommonClass.SharedCar;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.ServerConnect;

import java.io.*;
import java.util.Locale;

public class AddCarController {
    private File file;
    byte[] fileContent = null;

    @FXML
    private ImageView img_btn;

    @FXML
    private TextField reg_id;

    @FXML
    private TextField year_made;

    @FXML
    private TextField colour_1;

    @FXML
    private TextField colour_2;

    @FXML
    private TextField colour_3;

    @FXML
    private TextField car_make;

    @FXML
    private TextField car_model;

    @FXML
    private TextField price;

    @FXML
    private TextField quantity;

    @FXML
    private Button saveBtn;
    @FXML
    private Button returnBtn;

    @FXML
    void backToMain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/UserScene.fxml"));
            Stage stage = (Stage) returnBtn.getScene().getWindow();
            Scene scene = null;
            scene = new Scene(loader.load(), 700,460);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void save(ActionEvent event){
        if(reg_id.getText().isEmpty() || reg_id.getText().contains(" ")){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Registration number can not be null or contain space");
        }else if(file == null){
            showAlert(Alert.AlertType.ERROR,"Invalid", "Please choose a image");
        }
        else{
            SharedCar car = new SharedCar();
            car.setCar_reg(reg_id.getText().toLowerCase(Locale.ROOT)); //total 10-1 =9
            car.setYear_made(Integer.parseInt(year_made.getText()));
            car.setColour1(colour_1.getText());
            car.setColour2(colour_2.getText());
            car.setColour3(colour_3.getText());
            car.setCar_make(car_make.getText().toLowerCase(Locale.ROOT));
            car.setCar_model(car_model.getText().toLowerCase(Locale.ROOT));
            car.setQuantity(Integer.parseInt(quantity.getText()));
            car.setPrice(Integer.parseInt(price.getText()));

            try{
                FileInputStream fileInputStream = new FileInputStream(file.getPath());
                car.setByteArraySize((int) file.length());
                fileInputStream.read(car.getImage(),0,car.getImage().length);
                fileInputStream.close();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(ServerConnect.getInstance().getSocket().getOutputStream());
                            objectOutputStream.writeObject(car);
                            objectOutputStream.flush();

                            ObjectInputStream objectInputStream = new ObjectInputStream(ServerConnect.getInstance().getSocket().getInputStream());
                            String response = (String) objectInputStream.readObject();

                            if (response.equalsIgnoreCase("success")){
                                showAlert(Alert.AlertType.INFORMATION, "Success", "A Car Added Successfully");
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/UserScene.fxml"));
                                Stage stage = (Stage) saveBtn.getScene().getWindow();
                                Scene scene = new Scene(loader.load(), 700,460);
                                stage.setScene(scene);
                            }
                            else {
                                showAlert(Alert.AlertType.ERROR, "Error saving","Car not added, try again");
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void open_file(MouseEvent event) {

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg"); //getting only images

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(imageFilter);
        chooser.setTitle("Select Image");

        file = chooser.showOpenDialog(img_btn.getScene().getWindow());
        if(file != null)
        {
            Image image = new Image(file.toURI().toString());
            img_btn.setFitWidth(234);
            img_btn.setFitHeight(200);
            img_btn.setImage(image);
            img_btn.setPreserveRatio(true);
            img_btn.setSmooth(true);
            img_btn.setCache(true);
        }
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
