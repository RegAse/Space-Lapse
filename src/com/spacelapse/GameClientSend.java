package com.spacelapse;

import com.google.gson.Gson;

import java.io.IOException;

public class GameClientSend extends Thread {

    @Override
    public void run()
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
