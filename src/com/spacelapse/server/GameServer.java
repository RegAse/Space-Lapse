package com.spacelapse.server;


import com.google.gson.Gson;
import com.spacelapse.Bullet;
import com.spacelapse.Response;
import com.spacelapse.ship.Enforcer;
import com.spacelapse.ship.Fighter;
import com.spacelapse.ship.Ship;
import org.newdawn.slick.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;

public class GameServer extends BasicGame{

    /**
     * Server variables
     */
    public static double versionNumber = 0.12;
    private static String ipAddress = "localhost";
    private static int portNumber = 8976;
    private static ServerSocket socket;
    private static ArrayList<Socket> connectedSockets = new ArrayList<Socket>();
    private Random random;
    private Timer timer;

    /**
     * Game variables
     */
    private static ArrayList<Ship> ships = new ArrayList<>();

    public GameServer()
    {
        super("Server Version: " + versionNumber);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        random = new Random();
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    ships.add(new Enforcer(100 + random.nextInt(100), 200 + random.nextInt(20), 0.5f, 30f));
                    sendGameData();
                    System.out.println("Lel");
                } catch (SlickException e) {
                    e.printStackTrace();
                }
            }
        }, 1*5*1000, 1*5*1000);
    }

    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        boolean f = false;
        /* Server bullet process */
        for (int i1 = 0; i1 < ships.size(); i1++) {
            Ship ship1 = ships.get(i1);
            int i = -1;
            while (++i < ship1.shots.size()) {
                Bullet bull = ship1.shots.get(i);
                for (int i2 = 0; i2 < ships.size(); i2++) {
                    Ship ship2 = ships.get(i2);
                    if (ship2 != ship1 && ship2.intersects(bull)) {
                        ship2.applyDamage(bull.damage);
                        ship1.shots.remove(i);
                        System.out.println("Removed bullet.");
                        if (ship2.health <= 0) {
                            removeShip(ship2);
                        } else {
                            sendShipData(ship2);
                        }
                        break;
                    }
                }
            }
            ship1.addForceToBullets(gameContainer, delta);
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        graphics.drawString("Server on port: " + portNumber, 15, 15);
        graphics.drawString("Connected players: " + connectedSockets.size(), 15, 30);
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

        try {
            AppGameContainer app = new AppGameContainer(new GameServer());
            app.setDisplayMode(800, 600, false);
            app.setShowFPS(false); // set to false later
            app.setAlwaysRender(true);
            app.start();
        }
        catch (SlickException e) {
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
       while(true) {
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

               try {
                   newPlayer();
               } catch (SlickException e) {
                   e.printStackTrace();
               }
               // Start the receive from client in another thread
               Thread t = new Thread(new Runnable() {
                   public void run() {
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
        while(true) {
            try {
                String data = receive_from_client.readUTF();

                /* Change this over the same as the client uses */
                Gson gson = new Gson();
                Enforcer ship = gson.fromJson(data, Response.class).enforcer;

                for (int i = 0; i < ships.size(); i++) {
                    if (ship.id == ships.get(i).id) {
                        ships.set(i, ship);
                    }
                }
                sendJsonToAll(data);

            } catch (IOException e) {
                System.out.println("Lost Connection");
                connectedSockets.remove(connection);
                connectionsStatus();
                break;
            }
        }
    }

    public static void sendJsonToAll(String json) {
        int i = - 1;
        while(++i < connectedSockets.size()) {
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
    public static void newPlayer() throws SlickException {
        Enforcer new_ship = new Enforcer(100, 100, 0.5f, 100f);
        ships.add(new_ship);

        sendGameData();
    }

    /**
     * Removes the ship and sends message telling the client to remove this ship
     * @param  ship the ship to remove
     */
    public static void removeShip(Ship ship) {
        ships.remove(ship);
        Response response = new Response();
        response.removeShip(ship.id);
        Gson gson = new Gson();
        String json = gson.toJson(response);
        sendJsonToAll(json);
    }

    public static void sendShipData(Ship ship) throws SlickException {
        if (ship instanceof Enforcer){
            Response dm = new Response((Enforcer) ship);
            Gson gson = new Gson();
            String json = gson.toJson(dm);
            System.out.println(json);
            sendJsonToAll(json);
        }
        else if(ship instanceof Fighter) {
            Response dm = new Response((Fighter)ship);
            Gson gson = new Gson();
            String json = gson.toJson(dm);
            sendJsonToAll(json);
        }
        else{
            Response dm = new Response(ship);
            Gson gson = new Gson();
            String json = gson.toJson(dm);
            sendJsonToAll(json);
        }
    }

    public static void sendGameData() throws SlickException {
        /* Send all the ships to all of the clients */
        for (int i = 0; i < ships.size(); i++) {
            sendShipData(ships.get(i));
        }
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
        while (++i < connectedSockets.size()) {
            try {
                DataOutputStream send_to_client = new DataOutputStream(connectedSockets.get(i).getOutputStream());
                send_to_client.writeUTF("Heartbeat");

            } catch (IOException e) {
                /* I lost connection to the client */
                connectedSockets.remove(i);
                connectionsStatus();
            }

        }
    }
}
