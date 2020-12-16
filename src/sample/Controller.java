package sample;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final ServerConnect connector = ServerConnect.getInstance();

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;

    @FXML
    void login(ActionEvent event) throws IOException {

        List<String> adminUser = new ArrayList<>();

        String user_Name = userName.getText();
        String pass = password.getText();

        if(user_Name.isEmpty())
        {
            showAlert(Alert.AlertType.ERROR,"Error!", "Username can not be null");
            return;
        }

        else
        {
            adminUser.add(user_Name);
            adminUser.add(pass);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    checkUser(adminUser);
                }
            });

        }
    }

    private void checkUser(List<String> adminUser)
    {
        try {


            ObjectOutputStream objectOutputStream = new ObjectOutputStream(connector.getSocket().getOutputStream());

            objectOutputStream.writeObject(adminUser);  //sending to backend
            objectOutputStream.flush();

            ObjectInputStream objectInputStream = new ObjectInputStream(connector.getSocket().getInputStream());
            List<String> userinfo = (List<String>) objectInputStream.readObject(); //getting from backend

            if(userinfo.get(0).equals("false"))
            {
                User.setUserName(userinfo.get(1));
                User.setRole(userinfo.get(2));

                int size = userinfo.size();

                for(int i=3; i<size; i++) //Setting options
                {
                    User.getOptions().add(userinfo.get(i));
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("UserScene.fxml"));
                Stage stage = (Stage) loginBtn.getScene().getWindow();  //login button diye scene
                Scene scene = new Scene(loader.load(),700 , 460); //w,h
                stage.setScene(scene);
            }
            else
            {
                showAlert(Alert.AlertType.ERROR,"Credential Error!", "Please enter correct username and password");
            }

        } catch (IOException | ClassNotFoundException e) {
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


