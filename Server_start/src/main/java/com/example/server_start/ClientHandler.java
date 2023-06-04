package com.example.server_start;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {

    private final Socket clientSocket;
    private Server server;
    private PrintWriter out;
    private String clientName;
    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
        try{
            out = new PrintWriter(clientSocket.getOutputStream(),true);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public void sendMessage(String message){
        out.println(message);
    }
    public String getClientName(){
        return clientName;
    }

    public void stopClient(){
        try{
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void run(){
        PrintWriter out = null;
        BufferedReader in = null;

        try{
            out = new PrintWriter(clientSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;
            while((message = in.readLine())!= null){
                System.out.println(message);
                server.updateMessages(message,this);

            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            try{
                if (out != null){
                    out.close();
                }
                if (in != null){
                    in.close();
                    clientSocket.close();
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }


}
