package com.chatapp.client.client;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

public void run() {
        try{
            socket = new Socket("localhost",8888);
            out = new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        }
        catch(Exception e){e.printStackTrace();}
    }
    
    public void sendMessage(String message){
        out.println(message);
        out.flush();

    }

    public String getReply() throws IOException {
            String reply = in.readLine();
            return reply;
    }

    public void updateMessages(TextArea textArea){

    }
}
