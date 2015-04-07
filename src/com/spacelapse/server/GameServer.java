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

    public static float serverVersion = 0.01f;
    private static String ip = "localhost";
    private static int port = 8976;
    private static ServerSocket socket;
    private static ArrayList<Socket> sockets = new ArrayList<Socket>();

    private static Timer heartBeatTimer = new Timer();
    private static int heartBeatInterval = 2; // Seconds

    /* Game variables */
    // Player ships
    private static ArrayList<Ship> playerships;

    public GameServer()
    {
        super("Server Version: " + serverVersion);
    }

    public static void main(String[] arguments) throws SlickException {
        System.out.println("Server setting up... ");
        System.out.println("Server Version: " + serverVersion);
        Setup();
        SetupHeartBeat();

        // Print the connection status
        ConnectionsStatus();

        Thread t = new Thread(new Runnable() {
            public void run()
            {
                ListenForNewConnection();
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

    public static void Setup()
    {
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error while trying to create ServerSocket");
        }
        System.out.println("Server Started");
    }

    public static void ConnectionsStatus()
    {
        System.out.println("Connections: " + sockets.size());
    }

    public static void ListenForNewConnection()
    {
        try {
            Socket new_connection = socket.accept();
            sockets.add(new_connection);
            System.out.println("Connection established with: " + new_connection.getInetAddress());
            ConnectionsStatus();

            DataInputStream receive_from_client = new DataInputStream(new_connection.getInputStream());

            String firstMessage = receive_from_client.readUTF();
            System.out.println("GameServer: " + firstMessage);

            // Start the receive from client in another thread
            Thread t = new Thread(new Runnable() {
                public void run()
                {
                    try {
                        newPlayerShip();
                    } catch (SlickException e) {
                        e.printStackTrace();
                    }
                    ListenToClient(new_connection, receive_from_client);
                }
            });
            t.start();
        } catch (IOException e) {
            System.out.println("Connection to pending client failed.");
        }

        // Wait for another connection
        ListenForNewConnection();
    }

    public static void ListenToClient(Socket connection, DataInputStream receive_from_client)
    {
        System.out.println("Hey");
        System.out.println("PlayerCount: " + sockets.size());
        try
        {
            System.out.println(receive_from_client.readUTF());
        } catch (IOException e)
        {
            System.out.println("Lost Connection");
        }


        // Wait for another message
        ListenToClient(connection, receive_from_client);
    }

    /**
     * HeartBeat for experiment purposes
     **/
    public static void SetupHeartBeat()
    {
        heartBeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                HeartBeat();
            }
        }, heartBeatInterval * 1000, heartBeatInterval * 1000);
    }

    public static void HeartBeat()
    {
        int i = - 1;
        while (++i < sockets.size())
        {
            try {
                DataOutputStream send_to_client = new DataOutputStream(sockets.get(i).getOutputStream());
                send_to_client.writeUTF("Heartbeat");

            } catch (IOException e)
            {
                /* I lost connection to the client */
                sockets.remove(i);
                ConnectionsStatus();
            }

        }
    }

    public static void SendJsonToAll(String json)
    {
        int i = - 1;
        while(++i < sockets.size())
        {
            try {
                DataOutputStream send_to_client = new DataOutputStream(sockets.get(i).getOutputStream());
                send_to_client.writeUTF(json);
            } catch (IOException e)
            {
                /* I lost connection to the client */
                sockets.remove(i);
                ConnectionsStatus();
            }
        }
    }

    /**
     * Game setup functions
     **/
    public static void newPlayerShip() throws SlickException
    {
        Ship ship = new Ship(new Vector2f(100, 100), 10f);
        playerships.add(ship);

        Gson gson = new Gson();
        String json = gson.toJson(ship);
        SendJsonToAll(json);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException
    {
        playerships = new ArrayList<Ship>();
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {

    }
}
