package com.spacelapse;

import com.google.gson.Gson;
import com.spacelapse.entities.Asteroid;
import com.spacelapse.entities.Entity;
import com.spacelapse.entities.Ship;
import com.spacelapse.entities.Bullet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;

public class GameClient extends Thread{

    public static DataOutputStream send_to_server;
    public static DataInputStream receive_from_server;
    public static boolean isInitialized = false;

    /**
     * Will start a new thread to listen to the server
     * @param ip Internet Protocol (IP)
     * @param port Port number
     */
    public void JoinGame(String ip, int port) {
        Socket connectionSocket;
        try {
            connectionSocket = new Socket(ip, port);

            send_to_server = new DataOutputStream(connectionSocket.getOutputStream());
            receive_from_server = new DataInputStream(connectionSocket.getInputStream());
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
     * Starts listening to the server constantly
     */
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

    /**
     * Process the data from the server
     * @param data json string
     */
    public void ProcessServerData(String data) {
        Gson gson = new Gson();
        Response response = gson.fromJson(data, Response.class);
        /*
        * Need to find out how to convert this object to a entities then it will work
        * */
        for (Field field : response.getClass().getFields()) {
            /* Process only not null fields */
            try {
                if (field.get(response) != null){
                    processField(response, field);
                }
            } catch (IllegalAccessException e) {
                System.out.println("ProcessServerData Exception");
            }
        }
    }

    /**
     * Process the field
     * @param response object from client
     * @param field from the response object
     */
    private synchronized void processField(Response response, Field field) {
        switch(field.getName()){
            case "enforcer":
                Ship ship = response.enforcer;
                boolean f = false;
                for (int i = 0; i < Survival.entities.size(); i++) {
                    if (Survival.entities.get(i).id == ship.id) {
                        Survival.entities.set(i, ship);
                        f = true;
                        break;
                    }
                }
                if (f == false) {
                    Survival.entities.add(ship);
                }
                break;
            case "playerCount":
                break;
            case "removeEntity":
                int id = response.removeEntity;
                for (int i = 0; i < Survival.entities.size(); i++) {
                    Entity entity = Survival.entities.get(i);
                    if (entity.id == id){
                        Survival.entitiesToBeDestroyed.add(id);
                    }
                }
                break;
            case "asteroid":
                Asteroid asteroid = response.asteroid;
                boolean f1 = false;
                for (int i = 0; i < Survival.entities.size(); i++) {
                    if (Survival.entities.get(i).id == asteroid.id) {
                        asteroid.rotation = Survival.entities.get(i).rotation;
                        Survival.entities.set(i, asteroid);
                        f1 = true;
                        break;
                    }
                }
                if (f1 == false) {
                    Survival.entities.add(asteroid);
                }
                break;
            case "bullet":
                Bullet bullet = response.bullet;

                Survival.entities.add(bullet);
                break;
            case "gameSession":
                GameSession gameSession = response.gameSession;
                Survival.gameSession = gameSession;
                break;
        }
    }
}
