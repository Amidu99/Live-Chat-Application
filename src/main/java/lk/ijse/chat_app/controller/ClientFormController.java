package lk.ijse.chat_app.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.chat_app.bo.BOFactory;
import lk.ijse.chat_app.bo.custom.ClientBO;
import lk.ijse.chat_app.util.Clock;

public class ClientFormController extends Thread {
    @FXML
    private Label lblName;

    @FXML
    private Label lblDpUpdate;

    @FXML
    private Label lblUserID;

    @FXML
    private Label lblClock;

    @FXML
    private ImageView imgUser;

    @FXML
    private VBox vBox;

    @FXML
    private TextField txtMsg;

    @FXML
    private Button btnSend;

    @FXML
    private Rectangle recEmoji;

    @FXML
    private Rectangle recImg;

    ClientBO clientBO = (ClientBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CLIENT);

    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

    private FileChooser fileChooser;
    private File filePath;

    public void initialize() {
        Clock.setClock(lblClock);
        try {
            socket = new Socket("localhost", 7777);
            System.out.println("New socket is connected with the server..");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUser(String username, String userID) {
        lblName.setText(username);
        lblUserID.setText(userID);
        InputStream inputStream = null;

        try {
            inputStream = clientBO.getUserDP(userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Image image;
        if(inputStream != null) {
            image = new Image(inputStream);
            imgUser.setImage(image);
            imgUser.setPreserveRatio(false);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];

                StringBuilder fullMsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fullMsg.append(tokens[i]+" ");
                }

                String[] msgToArray = msg.split(" ");
                String st = "";
                for (int i = 0; i < msgToArray.length - 1; i++) {
                    st += msgToArray[i + 1] + " ";
                }
                Text text = new Text(st);
                String firstChars = "";
                if (st.length() > 3) {
                    firstChars = st.substring(0, 3);
                }

                if (firstChars.equalsIgnoreCase("img") || firstChars.equalsIgnoreCase("emo")) {
                    //to add Images or Emojis
                    st = st.substring(3, st.length() - 1);
                    File file = new File(st);
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(150);
                    imageView.setFitWidth(150);

                    if(firstChars.equalsIgnoreCase("emo")){
                        imageView.setFitHeight(40);
                        imageView.setFitWidth(40);
                    }
                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.BOTTOM_RIGHT);

                    if (!cmd.equalsIgnoreCase(lblName.getText())) {
                        vBox.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        Text text1 = new Text(" " + cmd + " : ");
                        hBox.getChildren().add(text1);
                        hBox.getChildren().add(imageView);
                    } else {
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(imageView);
                        Text text1 = new Text(" : Me ");
                        hBox.getChildren().add(text1);
                    }
                    Platform.runLater(() -> vBox.getChildren().addAll(hBox));
                } else {
                    TextFlow tempFlow = new TextFlow();
                    if (!cmd.equalsIgnoreCase(lblName.getText() + ":")) {
                        Text txtName = new Text(cmd + " ");
                        txtName.getStyleClass().add("txtName");
                        tempFlow.getChildren().add(txtName);
                    }
                    tempFlow.getChildren().add(text);
                    tempFlow.setMaxWidth(200); //200
                    TextFlow flow = new TextFlow(tempFlow);
                    HBox hBox = new HBox(12); //12

                    if (!cmd.equalsIgnoreCase(lblName.getText() + ":")) {
                        vBox.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(flow);
                    } else {
                        Text text2 = new Text(fullMsg + ": Me ");
                        TextFlow flow2 = new TextFlow(text2);
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(flow2);
                    }
                    Platform.runLater(() -> vBox.getChildren().addAll(hBox));
                }
            }
        } catch (Exception e) {e.printStackTrace();
        }
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        if(txtMsg.getText()!=null && !txtMsg.getText().isEmpty()) {
            String msg = txtMsg.getText();
            writer.println(lblName.getText() + ": "+msg+("\t "+lblClock.getText()));
            txtMsg.clear();
            if (msg.equalsIgnoreCase("bye") || msg.equalsIgnoreCase("logout") || msg.equalsIgnoreCase("exit")) {
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            }
        }
    }

    public void btnImgOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        // Create a file filter for image files
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg", "*.png", "*.gif");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        if(filePath!=null) {
            writer.println(lblName.getText() +" "+"img"+ filePath.getPath());
        }
    }

    public void btnEmojiOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        // Set the initial directory to a specific location
        fileChooser.setInitialDirectory(new File("src/main/resources/asset/emojis"));
        fileChooser.setTitle("Open Emoji");
        this.filePath = fileChooser.showOpenDialog(stage);
        if(filePath!=null) {
            writer.println(lblName.getText() +" "+"emo"+ filePath.getPath());
        }
    }

    public void imgUserMouseClickAction(MouseEvent mouseEvent) {
        String userID = lblUserID.getText();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg", "*.png", "*.gif");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setTitle("Open Image");
        File file = fileChooser.showOpenDialog(new Stage());
        InputStream inputStream;

        if (file != null && userID != null) {
            try {
                FileInputStream fis = new FileInputStream(file);
                boolean isUpdated = clientBO.updateUserDP(fis, userID);
                if(isUpdated){
                    inputStream = clientBO.getUserDP(userID);
                    Image img;
                    if(inputStream != null) {
                        img = new Image(inputStream);
                        imgUser.setImage(img);
                        imgUser.setPreserveRatio(false);
                    }
                }else{
                    new Alert(Alert.AlertType.ERROR, "Oops! Something went wrong..\nUser DP not updated.").show();
                }
            } catch (FileNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnSendEnterOnAction(MouseEvent mouseEvent) {
        btnSend.setStyle("-fx-background-color: #98FF98;");
    }

    public void btnSendExitOnAction(MouseEvent mouseEvent) {
        btnSend.setStyle("-fx-background-image: none;");
    }

    public void btnEmojiEnterOnAction(MouseEvent mouseEvent) {
        recEmoji.setStyle("-fx-fill: #F6A507");
    }

    public void btnEmojiExitOnAction(MouseEvent mouseEvent) {
        recEmoji.setStyle("-fx-fill: #ffffff");
    }

    public void btnImgEnterOnAction(MouseEvent mouseEvent) {
        recImg.setStyle("-fx-fill: #F6A507");
    }

    public void btnImgExitOnAction(MouseEvent mouseEvent) {
        recImg.setStyle("-fx-fill: #ffffff");
    }

    public void imgUserMouseInAction(MouseEvent mouseEvent) {
        lblDpUpdate.setVisible(true);
        imgUser.setStyle("-fx-opacity: 0.9");
    }

    public void imgUserMouseOutAction(MouseEvent mouseEvent) {
        lblDpUpdate.setVisible(false);
        imgUser.setStyle("-fx-opacity: 1");
    }

    public void txtMsgOnAction(ActionEvent actionEvent) { btnSend.requestFocus(); }
}