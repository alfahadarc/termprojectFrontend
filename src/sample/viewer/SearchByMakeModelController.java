package sample.viewer;

import CommonClass.SharedCar;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.ServerConnect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchByMakeModelController {

    @FXML
    private TextField carMake;

    @FXML
    private TextField carModel;

    @FXML
    private Button returnBtn;

    @FXML
    private TableView<SharedCar> carsTable;

    @FXML
    private TableColumn<SharedCar, String> reg_num;

    @FXML
    private TableColumn<SharedCar, Integer> year_made;

    @FXML
    private TableColumn<SharedCar, String> colour_1;

    @FXML
    private TableColumn<SharedCar, String> colour_2;

    @FXML
    private TableColumn<SharedCar, String> colour_3;

    @FXML
    private TableColumn<SharedCar, String> make;

    @FXML
    private TableColumn<SharedCar, String> model;

    @FXML
    private TableColumn<SharedCar, Integer> price;

    @FXML
    private TableColumn<SharedCar, Integer> quantity;

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

    private ObservableList<SharedCar> list;
    private List<SharedCar> cars;

    private  void initializeColumns(){
        reg_num.setCellValueFactory(new PropertyValueFactory<>("car_reg"));
        year_made.setCellValueFactory(new PropertyValueFactory<>("year_made"));
        colour_1.setCellValueFactory(new PropertyValueFactory<>("colour1"));
        colour_2.setCellValueFactory(new PropertyValueFactory<>("colour2"));
        colour_3.setCellValueFactory(new PropertyValueFactory<>("colour3"));
        make.setCellValueFactory(new PropertyValueFactory<>("car_make"));
        model.setCellValueFactory(new PropertyValueFactory<>("car_model"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }


    @FXML
    void searchCarByMakeModel(ActionEvent event) {
        initializeColumns();
        List<String> make_model = new ArrayList<>();
        if(carMake.getText().isEmpty() || carModel.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Can not empty");
        }else{
            make_model.add(carMake.getText()); //0
            make_model.add(carModel.getText()); //1

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(ServerConnect.getInstance().getSocket().getOutputStream());
                        objectOutputStream.writeObject(make_model);
                        objectOutputStream.flush();


                        ObjectInputStream objectInputStream = new ObjectInputStream(ServerConnect.getInstance().getSocket().getInputStream());
                        cars = (List<SharedCar>) objectInputStream.readObject();

                        if(cars == null){
                            showAlert(Alert.AlertType.ERROR,"Not Found","No Car Found");
                        }else{
                            list = FXCollections.observableList(cars);
                            carsTable.setItems(list);
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
