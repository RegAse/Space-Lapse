package com.spacelapse;

import com.google.gson.Gson;
import com.spacelapse.entities.Asteroid;
import com.spacelapse.entities.Enforcer;
import com.spacelapse.entities.Fighter;
import com.spacelapse.entities.Ship;
import com.spacelapse.entities.Bullet;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This is the object send through sockets
 */
public class Response {

    /** Collections **/
    public ArrayList<Ship> ships;

    /** Single instance **/
    public Enforcer enforcer;
    public Fighter fighter;
    public Ship ship;
    public Asteroid asteroid;
    public Bullet bullet;
    public GameSession gameSession;

    /** Must be Integer not int **/
    public Integer playerCount = 1;
    public Integer removeEntity;

    /**
     * Constructors
     */
    public Response(){}
    public Response(Enforcer ship) { this.enforcer = ship; }
    public Response(Fighter ship) { this.fighter = ship; }
    public Response(Asteroid asteroid) { this.asteroid = asteroid; }
    public Response(ArrayList<Ship> ships){ this.ships = ships; }
    public Response(Ship ship){ this.ship = ship; }
    public Response(Bullet bullet) { this.bullet = bullet; }
    public void removeEntity(int removeEntityWithThisId){ this.removeEntity = removeEntityWithThisId; }
    public void setGameSession(GameSession gameSession) { this.gameSession = gameSession; }

    /**
     * Send the data to the server
     */
    public void sendData()
    {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            GameClient.send_to_server.writeUTF(json);
        } catch (IOException e) {
            System.out.println("Server not found.");
        }
    }
}
