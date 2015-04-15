package com.spacelapse;

import com.google.gson.Gson;
import com.spacelapse.ship.Enforcer;
import com.spacelapse.ship.Ship;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameClient extends Thread{

    public static DataOutputStream send_to_server;
    public static DataInputStream receive_from_server;
    public static boolean isInitialized = false;

    @Override
    public void run() {
        System.out.println("Receive from server constantly");
        while(true) {
            try {
                String serverMessage = receive_from_server.readUTF();

                // Ignore heartbeat messages let TCP take care of it
                if (!serverMessage.equals("Heartbeat")) {
                    ProcessServerData(serverMessage);
                }
            } catch (IOException e) {
                System.out.println("Connection to the server has been lost");
                break;
            }
        }
    }

    public void JoinGame(String ip, int port) {
        Socket consock;
        try {
            consock = new Socket(ip, port);

            send_to_server = new DataOutputStream(consock.getOutputStream());
            receive_from_server = new DataInputStream(consock.getInputStream());
            isInitialized = true;

            // Send hello message
            send_to_server.writeUTF("I Successfuly joined this game.");
            Integer myid = receive_from_server.readInt();
            Survival.my_id = myid;

            Thread listen = new Thread(this);
            listen.start();

        } catch (IOException e) {
            System.out.println("Server not found.");
        }
    }

    /**
     * Process Data from server
     * */
    public void ProcessServerData(String data) {
        Gson gson = new Gson();
        Ship ship = gson.fromJson(data, Enforcer.class);
        boolean f = false;
        for (int i = 0; i < Survival.ships.size(); i++) {
            if (Survival.ships.get(i).id == ship.id) {
                Survival.ships.set(i, ship);
                f = true;
                break;
            }
        }
        if (f == false) {
            Survival.ships.add(ship);
        }
    }
}
