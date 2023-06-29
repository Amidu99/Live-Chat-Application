package lk.ijse.chat_app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.chat_app.bo.BOFactory;
import lk.ijse.chat_app.bo.custom.LoginBO;
import lk.ijse.chat_app.dto.UserDTO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {
    @FXML
    private AnchorPane logPane;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblgetHint;

    @FXML
    private TextField txtUserIDtoHint;

    @FXML
    private Label lblHint;

    @FXML
    private AnchorPane registerPane;

    @FXML
    private TextField txtUid;

    @FXML
    private TextField txtUname;

    @FXML
    private TextField txtUpass;

    @FXML
    private TextField txtUpassHint;

    @FXML
    private Button btnCreate;

    @FXML
    private Button btnImg;

    @FXML
    private JFXButton btnLog;

    @FXML
    private JFXButton btnReg;

    LoginBO loginBO = (LoginBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.LOGIN);

    public void btnLoginOnAction(ActionEvent actionEvent) throws SQLException, IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        if(!username.isEmpty() && !password.isEmpty()){
            boolean verified = loginBO.verifyLogin(username,password);
            if(verified){
                String userID = loginBO.getUserID(username, password);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/client_form.fxml"));
                Parent root = loader.load();
                ClientFormController clientFormController = loader.getController();
                clientFormController.setUser(username, userID);
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.setTitle(username+"'s chat room");
                stage.setResizable(false);
                stage.show();
                txtUsername.clear();
                txtPassword.clear();
            }else{
                new Alert(Alert.AlertType.ERROR, "Login Failed :\nInvalid Username or Password!").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Oops! Try again..\nUsername or Password cannot be empty.").show();
        }
    }

    public void lblGetHintOnAction(MouseEvent event) {
        lblgetHint.setVisible(true);
        txtUserIDtoHint.setVisible(true);
        txtUserIDtoHint.requestFocus();
    }

    public void btnRegOnAction(ActionEvent actionEvent) {
        logPane.setVisible(false);
        registerPane.setVisible(true);
        btnLog.setDisable(false);
        btnReg.setDisable(true);
        try {
            txtUid.setText(loginBO.generateNextUserID());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        txtUid.requestFocus();
        btnImg.setDisable(true);
        btnCreate.setDisable(false);
    }

    public void btnLogOnAction(ActionEvent actionEvent) {
        setDefaultRegister();
        registerPane.setVisible(false);
        logPane.setVisible(true);
        btnReg.setDisable(false);
        btnLog.setDisable(true);
        txtUsername.requestFocus();
    }

    public void btnCreateOnAction(ActionEvent actionEvent) {
        String userID = txtUid.getText();
        String userName = txtUname.getText();
        String password = txtUpass.getText();
        String passHint = txtUpassHint.getText();

        if(!userName.isEmpty() && !password.isEmpty() && !passHint.isEmpty()){
            try {
                boolean isAdded = loginBO.addUser(new UserDTO(userID, userName, password, passHint));
                if (isAdded){
                    new Alert(Alert.AlertType.INFORMATION, "You have successfully create the user account\nLogin with your Username & Password").show();
                    setDefaultRegister();
                    btnLogOnAction(actionEvent);
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Oops! something went wrong.").show();
                setDefaultRegister();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Oops! Try again..\nFields cannot be empty.").show();
        }
        setDefaultRegister();
    }

    private void setDefaultRegister() {
        txtUname.clear();
        txtUpass.clear();
        txtUpassHint.clear();
        txtUserIDtoHint.clear();
        btnImg.setDisable(true);
        lblgetHint.setVisible(false);
        txtUserIDtoHint.setVisible(false);
        lblHint.setVisible(false);
    }
    public void txtUserIDtoHintOnAction(ActionEvent actionEvent) {
        String userID = txtUserIDtoHint.getText();
        if (!userID.isEmpty()){
            String hint = null;
            try {
                hint = loginBO.getPasswordHint(userID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(hint != null){
                lblHint.setText("your hint = "+hint);
                lblHint.setVisible(true);
            } else { new Alert(Alert.AlertType.ERROR, "Oops! Try again..\nThis ID is not available").show(); }
        } else { new Alert(Alert.AlertType.ERROR, "Oops! Try again..\nFields cannot be empty.").show(); }
    }

    public void btnImgOnAction(ActionEvent actionEvent) {
        String userID = txtUid.getText();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                FileInputStream fis = new FileInputStream(file);
                boolean addImg = loginBO.addImage(userID, fis);
                if (addImg) {
                    new Alert(Alert.AlertType.INFORMATION, "Image Added Successfully!").show();
                    btnCreate.requestFocus();
                }
            } catch (SQLException | IOException | ClassNotFoundException e) {
                    e.printStackTrace();
            }
        }
    }

    public void txtUsernameOnAction(ActionEvent actionEvent) { txtPassword.requestFocus(); }

    public void txtPasswordOnAction(ActionEvent actionEvent) { btnLogin.requestFocus(); }

    public void txtUnameOnAction(ActionEvent actionEvent) { txtUpass.requestFocus(); }

    public void txtUpaasOnAction(ActionEvent actionEvent) { txtUpassHint.requestFocus(); }

    public void txtUpaasHintOnAction(ActionEvent actionEvent) { btnCreate.requestFocus(); }

    public void txtHintTypedAction(KeyEvent keyEvent) { btnImg.setDisable(false); }
}