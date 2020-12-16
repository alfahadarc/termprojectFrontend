package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UserSceneController {

    @FXML
    private ListView<String> UserList;

    @FXML
    private TextField UserResponce;

    @FXML
    private Button UserSubmit;

    @FXML
    private Label userName;

    @FXML
    private Label role;
    @FXML
    public void initialize() {
        //user_name.setText(User.getUserName());
        //name_of_the_user.setText(User.getUser_name());
        //role_of_the_user.setText(User.getRole());

        userName.setText(User.getUserName());
        role.setText(User.getRole());


        for(int i=0; i<User.getOptions().size(); i++)
        {
            UserList.getItems().add(String.valueOf(i+1)+". "+User.getOptions().get(i));
        }
    }

    @FXML
    void userSubmit(ActionEvent event) { //Button click event
        String choice = UserResponce.getText();
        if(choice.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Error", "Enter a value");
            return;
        }

        int valueOfChoice = Integer.parseInt(choice);
        if(valueOfChoice > User.getOptions().size()){
            showAlert(Alert.AlertType.ERROR, "Unaccepted value", "Choose correct number");
        }else{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    sendToServer();
                }
            });
        }
    }

    private void sendToServer() {

        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(ServerConnect.getInstance().getSocket().getOutputStream());
            List<String> clientResponse = new ArrayList<>();
            clientResponse.add(UserResponce.getText());
            objectOutputStream.writeObject(clientResponse);
            objectOutputStream.flush();


            int valueOfChoice = Integer.parseInt(UserResponce.getText());
            if(User.getRole().equalsIgnoreCase("admin"))
            {
                if(valueOfChoice == 1)
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/admin/create.fxml"));
                    Stage stage = (Stage) UserSubmit.getScene().getWindow();
                    stage.setTitle("Create a New User");
                    Scene scene = new Scene(loader.load(), 700.0, 460.0); // width, height
                    stage.setScene(scene);

                }
                else if(valueOfChoice == 2)
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/admin/remove.fxml"));
                    Stage stage = (Stage) UserSubmit.getScene().getWindow();
                    stage.setTitle("Remove an User");
                    Scene scene = new Scene(loader.load(), 700.0, 460.0);
                    stage.setScene(scene);
                }
            }
            else if(User.getRole().equalsIgnoreCase("Viewer"))
            {
                if(valueOfChoice == 1)
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/viewer/view_all_cars.fxml"));
                    Stage stage = (Stage) UserSubmit.getScene().getWindow();
                    stage.setTitle("View All Cars");
                    Scene scene = new Scene(loader.load(), 703.0, 543.0);
                    stage.setScene(scene);
                }
                else if(valueOfChoice == 2)
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/viewer/search_by_reg.fxml"));
                    Stage stage = (Stage) UserSubmit.getScene().getWindow();
                    stage.setTitle("Search By Registration Number");
                    Scene scene = new Scene(loader.load(), 700.0, 460.0);
                    stage.setScene(scene);
                }
                else if(valueOfChoice == 3)
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/viewer/search_by_make_model.fxml"));
                    Stage stage = (Stage) UserSubmit.getScene().getWindow();
                    stage.setTitle("Search By Make & Model");
                    Scene scene = new Scene(loader.load(), 700.0, 567.0);
                    stage.setScene(scene);
                }
                else if(valueOfChoice == 4)
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/viewer/buy_car.fxml"));
                    Stage stage = (Stage) UserSubmit.getScene().getWindow();
                    stage.setTitle("Buy A Car");
                    Scene scene = new Scene(loader.load(), 700.0, 460.0);
                    stage.setScene(scene);
                }

            }
            else if(User.getRole().equalsIgnoreCase("Manufacturer"))
            {
                    //..................................//
            if(valueOfChoice == 1)
                {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/manufacturer/view_car.fxml"));
                Stage stage = (Stage) UserSubmit.getScene().getWindow();
                stage.setTitle("View All Cars");
                Scene scene = new Scene(loader.load(), 847.0, 565.0);
                stage.setScene(scene);
                }
            else if(valueOfChoice == 2)
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/manufacturer/add_car.fxml"));
                Stage stage = (Stage) UserSubmit.getScene().getWindow();
                stage.setTitle("Add A New Car");
                Scene scene = new Scene(loader.load(), 700.0, 460.0);
                stage.setScene(scene);
            }
            else if(valueOfChoice == 3)
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/manufacturer/edit_car.fxml"));
                Stage stage = (Stage) UserSubmit.getScene().getWindow();
                stage.setTitle("Edit A Car");
                Scene scene = new Scene(loader.load(), 700.0, 460.0);
                stage.setScene(scene);
            }
            else if(valueOfChoice == 4)
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/manufacturer/remove_car.fxml"));
                Stage stage = (Stage) UserSubmit.getScene().getWindow();
                stage.setTitle("Remove A Car");
                Scene scene = new Scene(loader.load(), 700.0, 460.0);
                stage.setScene(scene);
            }

                //......................................//
            }
        }catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
