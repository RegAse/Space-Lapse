package com.spacelapse;

import com.google.gson.Gson;
import com.spacelapse.ship.Enforcer;
import com.spacelapse.ship.Ship;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This is the object send through sockets
 */
public class Response {

    public Enforcer enforcer;
    public Ship ship;
    public ArrayList<Ship> ships;
    public Integer playerCount = 1;
    public Integer removeShip;

    /**
     * Constructors
     */
    public Response(){}
    public Response(Enforcer ship) { this.enforcer = ship; }
    public Response(ArrayList<Ship> ships){ this.ships = ships; }
    public Response(Ship ship){ this.ship = ship; }
    public void removeShip(int removeShipWithThisId){ this.removeShip = removeShipWithThisId; }

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
