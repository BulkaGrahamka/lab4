package org.example.client;
import java.io.IOException;
import java.net.Socket;

public class Client{
    private Socket socket;
    public static void main(String[] args){
        Client klient = new Client();
        klient.polaczzserwerem("localhost", 6767);

    }

    public void polaczzserwerem(String host, int port){
        try{
            System.out.println("probojemy polaczyc sie z serwerem " + host + " : " + port);
            socket=new Socket(host, port);
            System.out.println("polaczono z serwerem :)");
            socket.close();
            System.out.println("koniec polaczenia");

        } catch (IOException e){
            System.out.println("blad poloczenia: " + e.getMessage());
        }
    }
}