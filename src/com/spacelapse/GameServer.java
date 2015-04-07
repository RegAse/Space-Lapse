package com.spacelapse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class GameServer extends Thread {

    private String ip;
    private int port;
    private ServerSocket socket;
    private ArrayList<Socket> sockets = new ArrayList<Socket>();

    public GameServer(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run()
    {
        try {
            socket = new ServerSocket(this.port);
        } catch (IOException e) {
            System.out.println("Error while trying to create ServerSocket");
        }
        System.out.println("Server Started");

        // Always listen for a new connection
        while (true)
        {
            try {
                Socket new_connection = socket.accept();
                sockets.add(new_connection);
                System.out.println("Connection established with: " + new_connection.getInetAddress());

                DataInputStream receive_from_client = new DataInputStream(new_connection.getInputStream());

                String firstMessage = receive_from_client.readUTF();
                System.out.println("GameServer: " + firstMessage);

                // Start the receive from client in another thread
                Thread t = new Thread(new Runnable() {
                    public void run()
                    {
                        try {
                            ProcessData(receive_from_client);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (IOException e) {
                System.out.println("Connection to pending client failed.");
            }
        }
    }

    public void ProcessData(DataInputStream receive_from_client) throws IOException {
        // Do something with the data the client sent
        // This acts basicaly like a listener
        while (true)
        {
            System.out.println(receive_from_client.readUTF());
        }
    }
}
