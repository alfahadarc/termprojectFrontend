package sample.manufacturer;

import CommonClass.SharedCar;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.ServerConnect;

import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class ViewCarController {

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

    private ObservableList<SharedCar> list;
    private List<SharedCar> carList;

    private void initializeColumns(){
        reg_num.setCellValueFactory(new PropertyValueFactory<>("car_reg"));
        year_made.setCellValueFactory(new PropertyValueFactory<>("year_made"));
        colour_1.setCellValueFactory(new PropertyValueFactory<>("colour1"));
        colour_2.setCellValueFactory(new PropertyValueFactory<>("colour2"));
        colour_3.setCellValueFactory(new PropertyValueFactory<>("colour3"));
        make.setCellValueFactory(new PropertyValueFactory<>("car_make"));
        model.setCellValueFactory(new PropertyValueFactory<>("car_model"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        //Btn
    }


    public void initialize() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initializeColumns();
                populateTable();
            }
        });
    }

    private void populateTable() {
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(ServerConnect.getInstance().getSocket().getInputStream());
            carList = (List<SharedCar>) objectInputStream.readObject();
            list = FXCollections.observableList(carList);
            carsTable.setItems(list);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}