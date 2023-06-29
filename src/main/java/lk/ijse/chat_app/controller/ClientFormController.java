package lk.ijse.chat_app.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
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
    private Label lblClock;

    @FXML
    private ImageView imgUser;

    @FXML
    private VBox vBox;

    @FXML
    private TextField txtMsg;

    @FXML
    private Button btnSend;

    ClientBO clientBO = (ClientBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CLIENT);

    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

    private FileChooser fileChooser;
    private File filePath;

    public void initialize() throws IOException {
        Clock.setClock(lblClock);
        try {
            socket = new Socket("localhost", 7777);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUser(String username, String userID) {
        lblName.setText(username);
        ResultSet resultSet = null;
        try {
            resultSet = clientBO.getImage(userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert resultSet != null;
            if (resultSet.next()) {
                Image img = null;
                try {
                    img = new Image(resultSet.getBinaryStream("DP"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                imgUser.setImage(img);
                imgUser.setPreserveRatio(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

                if (firstChars.equalsIgnoreCase("img")) {
                    //for the Images
                    st = st.substring(3, st.length() - 1);

                    File file = new File(st);
                    Image image = new Image(file.toURI().toString());

                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(150);
                    imageView.setFitWidth(150);

                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.BOTTOM_RIGHT);

                    if (!cmd.equalsIgnoreCase(lblName.getText())) {
                        vBox.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);

                        Text text1 = new Text("  " + cmd + " :");
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
                        Text text2 = new Text(fullMsg + ": Me");
                        TextFlow flow2 = new TextFlow(text2);
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(flow2);
                    }
                    Platform.runLater(() -> vBox.getChildren().addAll(hBox));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        if(txtMsg.getText()!=null && !txtMsg.getText().isEmpty()) {
            String msg = txtMsg.getText();
            writer.println(lblName.getText() + ": " + msg + ("\t\t" + lblClock.getText()));
            txtMsg.clear();
            if (msg.equalsIgnoreCase("bye") || msg.equalsIgnoreCase("logout") || msg.equalsIgnoreCase("exit")) {
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            }
        }
    }

    public void btnImgOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        if(filePath!=null) {
            writer.println(lblName.getText() + " " + "img" + filePath.getPath());
        }
    }

    public void txtMsgOnAction(ActionEvent actionEvent) { btnSend.requestFocus(); }
}