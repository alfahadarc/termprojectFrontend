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

public class EditCarController {

    private File file;
    byte[] fileContent = null;

    @FXML
    private TextField reg_num;

    @FXML
    private TextField year_made;

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
    private Button returnBtn;

    @FXML
    private TextField newRegNum;

    @FXML
    private ImageView img_btn;

    int id=0;

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
    void editCar(ActionEvent event) {
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
                        SharedCar carSend = (SharedCar) objectInputStream.readObject();

                        if(carSend != null){
                            id = carSend.getId();
                            newRegNum.setText(carSend.getCar_reg());
                            year_made.setText(Integer.toString(carSend.getYear_made()) );
                            colour_1.setText(carSend.getColour1());
                            colour_2.setText(carSend.getColour2());
                            colour_3.setText(carSend.getColour3());
                            make.setText(carSend.getCar_make());
                            model.setText(carSend.getCar_model());
                            price.setText(Integer.toString(carSend.getPrice()));
                            quantity.setText(Integer.toString(carSend.getQuantity()));
                            try{
                                img_btn.setImage(new Image(new ByteArrayInputStream(carSend.getImage())));
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }

                        }else{
                            showAlert(Alert.AlertType.INFORMATION,"NotFound","No Car Exists");
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    @FXML
    void sendEditedValue(ActionEvent event) {
        if (newRegNum.getText().isEmpty() || newRegNum.getText().contains(" ")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Registration number can not be null or contain space");
        } else if (file == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid", "Please choose a image");
        } else {
            SharedCar car = new SharedCar();
            car.setId(id);
            car.setCar_reg(newRegNum.getText().toLowerCase(Locale.ROOT)); //total 10-1 =9
            car.setYear_made(Integer.parseInt(year_made.getText()));
            car.setColour1(colour_1.getText());
            car.setColour2(colour_2.getText());
            car.setColour3(colour_3.getText());
            car.setCar_make(make.getText().toLowerCase(Locale.ROOT));
            car.setCar_model(model.getText().toLowerCase(Locale.ROOT));
            car.setQuantity(Integer.parseInt(quantity.getText()));
            car.setPrice(Integer.parseInt(price.getText()));

            try {
                FileInputStream fileInputStream = new FileInputStream(file.getPath());
                car.setByteArraySize((int) file.length());
                fileInputStream.read(car.getImage(), 0, car.getImage().length);
                fileInputStream.close();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(ServerConnect.getInstance().getSocket().getOutputStream());
                            objectOutputStream.writeObject(car);
                            objectOutputStream.flush();

                            ObjectInputStream objectInputStream = new ObjectInputStream(ServerConnect.getInstance().getSocket().getInputStream());
                            String ans = (String) objectInputStream.readObject();
                            if(ans.equalsIgnoreCase("success")){
                                showAlert(Alert.AlertType.INFORMATION,"Success","Updated");
                            }else {
                                showAlert(Alert.AlertType.ERROR, "Failed", "Not Updated");
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
