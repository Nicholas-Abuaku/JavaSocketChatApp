package com.example.server_start;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

public class HelloController {

    Server server = new Server();
    Thread serverThread = new Thread(server);

    @FXML
    private TextField broadcastTextField;
    @FXML
    private Button closeServer;

    @FXML
    private Button sendBroadcast;
    @FXML
    private TextArea consoleOutputBox;



    @FXML
    void printHello(MouseEvent event) throws IOException, InterruptedException {
            serverThread.start();
            server.redirectConsoleOutput(consoleOutputBox);
    }

    @FXML
    void sendBroadcast(MouseEvent event) {
        String messageToBroadcast = broadcastTextField.getText();
        server.broadcastMessage("Server: "+ messageToBroadcast);

    }



    @FXML
    void closeServer(MouseEvent event) throws IOException {
        server.closeServer();
        serverThread.interrupt();
        System.exit(0);
        Platform.exit();
    }










}
