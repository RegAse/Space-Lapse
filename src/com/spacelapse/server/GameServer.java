package com.spacelapse.server;


import com.google.gson.Gson;
import com.spacelapse.Ship;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

public class GameServer extends BasicGame{

    /**
     * Server variables
     */
    public static double versionNumber = 0.12;
    private static String ipAddress;
    private static int portNumber = 8976;
    private static ServerSocket socket;
    private static ArrayList<Socket> connectedSockets = new ArrayList<Socket>();

    /**
     * Game variables
     */
    private static ArrayList<Ship> ships;

    public GameServer()
    {
        super("Server Version: " + versionNumber);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException
    {
        ships = new ArrayList<Ship>();
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {

    }

    public static void main(String[] arguments) throws SlickException {
        System.out.println("Server setting up... ");
        System.out.println("Server Version: " + versionNumber);
        setupServer();
        //SetupHeartBeat();

        // Print the connection status
        connectionsStatus();

        Thread t = new Thread(new Runnable() {
            public void run()
            {
                listenForNewConnection();
            }
        });
        t.start();

        try
        {
            AppGameContainer app = new AppGameContainer(new GameServer());
            app.setDisplayMode(200, 140, false);
            app.setShowFPS(true); // set to false later
            app.setAlwaysRender(true);
            app.start();
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }
    }

    public static void setupServer() {
        try {
            socket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println("Error while trying to create ServerSocket");
        }
        System.out.println("Server Started");
    }

    public static void connectionsStatus()
    {
        System.out.println("Connections: " + connectedSockets.size());
    }

    public static void listenForNewConnection() {
       while(true)
       {
           try {
               Socket new_connection = socket.accept();
               connectedSockets.add(new_connection);
               System.out.println("Connection established with: " + new_connection.getInetAddress());
               connectionsStatus();

               DataInputStream receive_from_client = new DataInputStream(new_connection.getInputStream());

               String firstMessage = receive_from_client.readUTF();

               //Send the id of the ship he can control
               DataOutputStream send_to_client = new DataOutputStream(new_connection.getOutputStream());
               send_to_client.writeInt(Ship.ids);

               System.out.println("GameServer: " + firstMessage);

               // Start the receive from client in another thread
               Thread t = new Thread(new Runnable() {
                   public void run() {
                       try {
                           newPlayerShip();
                       } catch (SlickException e) {
                           e.printStackTrace();
                       }
                       listenToClient(new_connection, receive_from_client);
                   }
               });
               t.start();
           } catch (IOException e) {
               System.out.println("Connection to pending client failed.");
           }
       }
    }

    public static void listenToClient(Socket connection, DataInputStream receive_from_client) {
        while(true)
        {
            try
            {
                sendJsonToAll(receive_from_client.readUTF());

            } catch (IOException e)
            {
                System.out.println("Lost Connection");
                connectedSockets.remove(connection);
                connectionsStatus();
                break;
            }
        }
    }

    public static void sendJsonToAll(String json) {
        int i = - 1;
        while(++i < connectedSockets.size())
        {
            try {
                DataOutputStream send_to_client = new DataOutputStream(connectedSockets.get(i).getOutputStream());
                send_to_client.writeUTF(json);
            } catch (IOException e)
            {
                /* I lost connection to the client */
                connectedSockets.remove(i);
                connectionsStatus();
            }
        }
    }

    /**
     * Game setup functions
     **/
    public static void newPlayerShip() throws SlickException {
        Ship ship = new Ship(new Vector2f(100, 100), 1.5f);
        ships.add(ship);

        Gson gson = new Gson();
        String json = gson.toJson(ship);
        sendJsonToAll(json);
    }

    /**
     * HeartBeat for experiment purposes
     **/

    private static Timer heartBeatTimer = new Timer();
    private static int heartBeatInterval = 2; // Seconds

    public static void setupHeartBeat() {
        heartBeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                heartBeat();
            }
        }, heartBeatInterval * 1000, heartBeatInterval * 1000);
    }

    public static void heartBeat() {
        int i = - 1;
        while (++i < connectedSockets.size())
        {
            try {
                DataOutputStream send_to_client = new DataOutputStream(connectedSockets.get(i).getOutputStream());
                send_to_client.writeUTF("Heartbeat");

            } catch (IOException e)
            {
                /* I lost connection to the client */
                connectedSockets.remove(i);
                connectionsStatus();
            }

        }
    }
}
