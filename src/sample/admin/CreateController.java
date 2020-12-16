package sample.admin;

import CommonClass.SharedUser;
import com.sun.net.httpserver.Authenticator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.ServerConnect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CreateController {

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private ChoiceBox<String> role_drop_down;

    @FXML
    private Button saveBtn;

    @FXML
    private void initialize()
    {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll("Admin", "Viewer","Manufacturer");
        role_drop_down.setItems(list);
        role_drop_down.getSelectionModel().selectFirst();
    }

    @FXML
    void saveUser(ActionEvent event) {
        System.out.println(userName.getText()+password.getText()+role_drop_down.getValue());
        String user_name = userName.getText();

        if(user_name.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Username must not be empty");
        }else{
            SharedUser newUser = new SharedUser();

            newUser.setUserName(user_name);
            newUser.setPassword(password.getText());
            newUser.setRole(role_drop_down.getValue());

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(ServerConnect.getInstance().getSocket().getOutputStream());
                        objectOutputStream.writeObject(newUser);
                        objectOutputStream.flush();

                        ObjectInputStream objectInputStream = new ObjectInputStream(ServerConnect.getInstance().getSocket().getInputStream());
                        String response = (String) objectInputStream.readObject();
                        if (response.equalsIgnoreCase("success")){
                            showAlert(Alert.AlertType.INFORMATION, "Success", "User created successfully");
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/UserScene.fxml"));
                            Stage stage = (Stage) saveBtn.getScene().getWindow();
                            Scene scene = new Scene(loader.load(), 700,460);
                            stage.setScene(scene);
                        }
                        else {
                            showAlert(Alert.AlertType.ERROR, "Error saving","User not created, try again");
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
