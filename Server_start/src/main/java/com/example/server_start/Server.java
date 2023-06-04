package com.example.server_start;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{
    private List<PrintWriter> clientWriters;
    private List<ClientHandler>clients;

    ServerSocket ss;



    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            clients = new ArrayList<>();
            clientWriters = new ArrayList<>();

            try {
                ss = new ServerSocket(8888);
                System.out.println("Server started on port 8888...");
                System.out.println("Waiting for connections...");
                while (true) {
                    Socket client = ss.accept();
                    System.out.println("Client Connected:");
                    PrintWriter clientWriter = new PrintWriter(client.getOutputStream(), true);
                    clientWriters.add(clientWriter);
                    ClientHandler clientSocket = new ClientHandler(client, this);
                    clients.add(clientSocket);
                    new Thread(clientSocket).start();
                    sendClientList(clientSocket);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMessage(String message){
        for(PrintWriter writer:clientWriters){
            writer.println(message);
            writer.flush();
        }

    }

    private void sendClientList(ClientHandler clientHandler){
        List<String> clientNames = new ArrayList<>();
        for(ClientHandler client: clients){
            clientNames.add(client.getClientName());
        }
    }

    public void updateMessages(String message,ClientHandler sender){
        for(ClientHandler client: clients){
            if(client!= sender){
                client.sendMessage(message);
            }
        }
    }



    public void closeServer() throws IOException {
        broadcastMessage("Closing server...");
        broadcastMessage("Disconnected.");
        for(ClientHandler client:clients){
            client.stopClient();
        }
    }

    public void redirectConsoleOutput(TextArea outputArea){
        OutputStream consoleOutput = new OutputStream(){
            @Override
            public void write(int b){
                Platform.runLater(()->{
                    outputArea.appendText(String.valueOf((char)b));
                });
            }
        };

        PrintStream printStream = new PrintStream(consoleOutput,true);
        System.setOut(printStream);
        System.setErr(printStream);

    }


}