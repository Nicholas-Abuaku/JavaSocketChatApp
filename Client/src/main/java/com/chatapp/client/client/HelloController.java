package com.chatapp.client.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class HelloController {
    private Client c;
    private ExecutorService executor;
    @FXML
    private TextArea messageBox;
    @FXML
    private Button sendMessage;

    @FXML
    private TextArea chatBox;
    @FXML
    private TextField UserNameField;

    private String userName;
    @FXML
    private TextField serverIPField;
    @FXML
    private TextField portField;

    public HelloController() throws IOException {
        //c = new Client();
        executor = Executors.newSingleThreadExecutor();
    }

    @FXML
    void sendMessage(MouseEvent event) {
        String Message  = messageBox.getText();
    }

    @FXML
    void startClient(MouseEvent event) throws IOException, InterruptedException {
        String serverIP = serverIPField.getText();
        Integer serverPort = Integer.parseInt(portField.getText());
        executor.submit(c = new Client(serverIP,serverPort));
        TimeUnit.SECONDS.sleep(2);
        Thread refreshMessages = new Thread(()->{
            while(true){
                try{
                    String message = c.getReply();
                    if(message !=null && !message.isEmpty()){
                        Platform.runLater(()->{
                            chatBox.appendText(message+"\n");
                        });
                    }
                } catch(Exception e){
                    //e.printStackTrace();
                    chatBox.appendText("Error: Please check correct IP and port");
                }
            }
        });
        refreshMessages.setDaemon(true);
        refreshMessages.start();

    }

    public void initialize(){
        sendMessage.setOnAction(event -> {
            try {
                sendMessages();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void sendMessages() throws IOException, InterruptedException {
        String message = messageBox.getText();
        System.out.println("Message: " + message);
        c.sendMessage(userName+ ": "+ message);
        chatBox.appendText("You: "+ message+"\n");
        messageBox.clear();
    }

    @FXML
    void updateMessages() throws IOException, InterruptedException {
      executor.execute(()->{
          String reply = null;
          try {
              reply = c.getReply();
          } catch (IOException e) {
              throw new RuntimeException(e);
          }
          String finalReply = reply;
          Platform.runLater(()->{
             String chatBoxText = chatBox.getText();
              chatBox.setText(chatBoxText + "\n"+ finalReply);
              System.out.println(finalReply);
          });
      });
    }

    @FXML
    void updateUsername(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            userName = UserNameField.getText();
        }
        }


}
