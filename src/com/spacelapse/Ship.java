package com.spacelapse;

import com.google.gson.Gson;
import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Ship{

    public static int ids = 0;
    public int id;
    private Vector2f position;
    private float speed;
    private float rotation;
    private boolean hasMoved = false;

    private ArrayList<Bullet> shots;
    private float shotSpeed = 0.8f;
    private int defaultBulletDelay = 200;
    private int time = 0;

    public Ship(Vector2f position, float speed) throws SlickException {
        this.position = position;
        this.speed = speed;
        this.rotation = 0;
        this.shots = new ArrayList<Bullet>();
        this.id = ids;
        ids++;
    }

    public void Controller(GameContainer gc, int delta, Image texture) throws SlickException {
        Input input = gc.getInput();

        /* Basic movement code */
        if (input.isKeyDown(Input.KEY_D) && gc.getWidth() >= this.position.x + speed * delta) {
            this.position.x += speed * delta;
            hasMoved = true;
        }
        else if (input.isKeyDown(Input.KEY_A) && 0 <= this.position.x + speed * delta) {
            this.position.x -= speed * delta;
            hasMoved = true;
        }

        if (input.isKeyDown(Input.KEY_W) && 0 <= this.position.y + speed * delta) {
            this.position.y -= speed * delta;
            hasMoved = true;
        }
        else if (input.isKeyDown(Input.KEY_S) && gc.getHeight() >= this.position.y + speed * delta) {
            this.position.y += speed * delta;
            hasMoved = true;
        }

        /* Code for rotation towards mouse */
        float xdist = input.getMouseX() - position.x;
        float ydist = input.getMouseY() - position.y;

        rotation = (float) Math.toDegrees(Math.atan2(ydist, xdist));
        texture.setRotation(rotation + 90f);

        if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            time -= delta;
            if (time <= 0) {
                Shoot();
                time = defaultBulletDelay;
            }
        }

        // Add force to shots
        int i = - 1;
        while (++i < shots.size()) {
            Bullet shot = shots.get(i);
            if (shot.position.x > gc.getWidth() || shot.position.y > gc.getHeight() || shot.position.x < - 10 || shot.position.y < - 10) {
                shots.remove(i);
            }
            else {
                shot.position.x += shotSpeed * Math.sin((shot.direction * Math.PI / 180) + 90f) * delta;
                shot.position.y += shotSpeed * Math.cos((shot.direction * Math.PI / 180) - 90f) * delta;
            }
        }
        if (GameClient.isInitialized && hasMoved) {
            sendData();
            hasMoved = false;
        }
    }

    public void Shoot()
    {
        shots.add(new Bullet(this.position.x, this.position.y, this.rotation, true, 10f));
    }

    public void render(Graphics graphics, Image texture, Image bulletTexture) {
        texture.drawCentered(position.getX(), position.getY());

        renderShots(bulletTexture);
    }

    private void renderShots(Image bullet) {
        for (Bullet shot : shots) {
            bullet.setRotation(shot.direction);
            bullet.drawCentered(shot.position.x, shot.position.y);
        }
    }

    public void sendData() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            GameClient.send_to_server.writeUTF(json);
        } catch (IOException e) {
            System.out.println("Server not found.");
        }
    }
}
