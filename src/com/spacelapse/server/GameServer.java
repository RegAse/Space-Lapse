package com.spacelapse.server;


import com.google.gson.Gson;
import com.spacelapse.GameSession;
import com.spacelapse.entities.Bullet;
import com.spacelapse.Response;
import com.spacelapse.entities.*;
import com.spacelapse.resourcemanager.Fonts;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
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
    public static boolean isInitialized;
    private static String ipAddress = "localhost";
    private static int portNumber = 8976;
    private static ServerSocket socket;
    private static ArrayList<Socket> connectedSockets = new ArrayList<Socket>();
    public static GameSession gameSession = new GameSession(0, 0, 0);
    private Random random;
    private Timer timer;

    /**
     * Game variables
     */
    private static int next_entity_id = 0;
    private static ArrayList<Entity> entities = new ArrayList<>();
    public static ArrayList<Integer> entitiesToBeDestroyed = new ArrayList<>();

    public static int getNextEntityId(){
        return next_entity_id++;
    }


    public GameServer()
    {
        super("Server Version: " + versionNumber);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        isInitialized = true;
        random = new Random();
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int rand = random.nextInt(4);
                int width = gameContainer.getWidth();
                int height = gameContainer.getHeight();
                Asteroid asteroid = null;
                if (rand == 1) {
                    // Left
                    asteroid = new Asteroid(0, random.nextInt(height), 0.5f, 30f, width, random.nextInt(height));
                }
                else if (rand == 2) {
                    // Right
                    asteroid = new Asteroid(width, random.nextInt(height), 0.5f, 30f, 0, random.nextInt(height));
                }
                else if (rand == 3) {
                    // Top
                    asteroid = new Asteroid(random.nextInt(width), 0, 0.5f, 30f, random.nextInt(width), height);
                }
                else if (rand == 4) {
                    // Bottom
                    asteroid = new Asteroid(random.nextInt(width), height, 0.5f, 30f, random.nextInt(width), 0);
                }
                entities.add(asteroid);

                Gson gson = new Gson();
                Response response = new Response(asteroid);
                sendJsonToAll(gson.toJson(response));

            }
        }, 1*1*400, 1*1*400);
    }

    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        int i = - 1;
        while (++i < entities.size()) {
            Entity entity = entities.get(i);
            if (entity != null && entitiesToBeDestroyed.contains(entity.id)) {
                entities.remove(entity);
                entitiesToBeDestroyed.remove((Integer)entity.id);
            }
        }

        boolean f = false;
        /* Server bullet process */
        for (int i1 = 0; i1 < entities.size(); i1++) {
            Entity entity = entities.get(i1);

            if (entity instanceof Ship) {
                Ship ship = (Ship) entity;
            }
            else if (entity instanceof Bullet) {
                Bullet bullet = (Bullet)entity;
                for (int i2 = 0; i2 < entities.size(); i2++) {
                    Entity entity2 = entities.get(i2);

                    if (entity2 != null && !(entity2 instanceof Bullet) && entity2.intersects(bullet) && entity2.id != bullet.ownerId) {
                        entity2.applyDamage(bullet.damage);

                        if (entity2.health <= 0) {
                            removeEntity(entity2);
                            gameSession.score += 10;
                            sendGameSessionData();
                        }
                        else {
                            sendEntityData(entity2);
                        }

                        removeEntity(bullet);
                        break;
                    }
                }

                bullet.addForceToBullet(gameContainer, delta);
            }
            else if (entity != null && entity instanceof Asteroid) {
                Asteroid asteroid = (Asteroid)entity;
                asteroid.moveTowardsTarget(0.1f);
            }
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        graphics.setFont(Fonts.getImpact());
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

               //Send the id of the entity he can control
               DataOutputStream send_to_client = new DataOutputStream(new_connection.getOutputStream());
               send_to_client.writeInt(GameServer.next_entity_id);

               System.out.println("GameServer: " + firstMessage);

               try {
                   newPlayer();
                   gameSession.playerCount = connectedSockets.size();
                   sendGameSessionData();
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

                ProcessClientData(data);

            } catch (IOException e) {
                System.out.println("Lost Connection");
                connectedSockets.remove(connection);
                connectionsStatus();
                break;
            }
        }
    }

    /**
     * Process Data from server
     * */
    public static void ProcessClientData(String data) {
        Gson gson = new Gson();
        Response response = gson.fromJson(data, Response.class);

        for (Field field : response.getClass().getFields()) {
            /* Process only not null fields */
            try {
                if (field.get(response) != null){
                    processField(response, field, data);
                }
            } catch (IllegalAccessException e) {
                System.out.println("ProcessServerData Exception");
            }
        }
    }

    private static void processField(Response response, Field field, String data) {
        switch(field.getName()){
            case "bullet":
                Bullet bullet = response.bullet;
                Bullet bullet1 = new Bullet(bullet.ownerId, bullet.position.x, bullet.position.y, bullet.speed, bullet.health, bullet.rotation, bullet.damage);
                entities.add(bullet1);

                Response response1 = new Response(bullet1);
                Gson gson = new Gson();
                String json = gson.toJson(response1);
                sendJsonToAll(json);
                break;
            case "enforcer":
                sendJsonToAll(data);
                Enforcer ship = response.enforcer;

                for (int i = 0; i < entities.size(); i++) {
                    Entity entity = entities.get(i);
                    if (entity != null && ship.id == entities.get(i).id) {
                        entities.set(i, ship);
                    }
                }
                break;
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
        entities.add(new_ship);

        sendGameData();
    }

    /**
     * Removes the entities and sends message telling the client to remove this entities
     * @param  entity the entities to remove
     */
    public static void removeEntity(Entity entity) {
        entitiesToBeDestroyed.add(entity.id);
        Response response = new Response();
        response.removeEntity(entity.id);
        Gson gson = new Gson();
        String json = gson.toJson(response);
        sendJsonToAll(json);
    }

    public static void sendEntityData(Entity entity) throws SlickException {
       try{
           if (entity instanceof Enforcer){
               Response dm = new Response((Enforcer) entity);
               Gson gson = new Gson();
               String json = gson.toJson(dm);
               System.out.println(json);
               sendJsonToAll(json);
           }
           else if(entity instanceof Fighter) {
               Response dm = new Response((Fighter) entity);
               Gson gson = new Gson();
               String json = gson.toJson(dm);
               sendJsonToAll(json);
           }
           else if(entity instanceof Asteroid){
               Response dm = new Response((Asteroid) entity);
               Gson gson = new Gson();
               String json = gson.toJson(dm);
               sendJsonToAll(json);
           }
           else if (entity instanceof Bullet) {
               Response dm = new Response((Bullet) entity);
               Gson gson = new Gson();
               String json = gson.toJson(dm);
               sendJsonToAll(json);
           }
       } catch(Exception ex) {
           System.out.println("json error");
       }
    }

    public static void sendGameSessionData() {
        Response response = new Response();
        response.setGameSession(gameSession);
        Gson gson = new Gson();
        String json = gson.toJson(response);
        sendJsonToAll(json);
        System.out.println("Send gamesession data");
    }

    public static void sendGameData() throws SlickException {
        /* Send all the ships to all of the clients */
        for (int i = entities.size() - 1; i >= 0; i--) {
            try {
                Entity entity = entities.get(i);
                if(entity != null && !(entity instanceof Bullet)) {
                    sendEntityData(entities.get(i));
                }
            }catch (Exception ex){
                System.out.println("The collection was modified while trying to send it.");
            }
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
