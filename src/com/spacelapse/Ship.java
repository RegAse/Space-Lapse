package com.spacelapse;

import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Ship{

    private Vector2f position;
    private Image texture;
    private Image bullet01;
    private float speed;
    private float rotation;

    private ArrayList<Bullet> shots;
    private float shotspeed = 0.8f;
    private static int default_bullet_delay = 10;
    private static int time = 0;

    public Ship(Vector2f position, Image texture, float speed) throws SlickException
    {
        this.position = position;
        this.texture = texture;
        this.speed = speed;
        this.rotation = 0;
        this.shots = new ArrayList<Bullet>();
        this.bullet01 = new Image("data/bullets/bullet01_tiny.png", false);
    }

    public Ship(Vector2f position, float speed) throws SlickException
    {
        this.position = position;
        this.speed = speed;
        this.rotation = 0;
        this.shots = new ArrayList<Bullet>();
        //this.bullet01 = new Image("data/bullets/bullet01_tiny.png", false);
    }

    public void Controller(GameContainer gc, int delta) throws SlickException
    {
        Input input = gc.getInput();

        /* Basic movement code */
        if (input.isKeyDown(Input.KEY_D) && gc.getWidth() >= this.position.x + speed * delta)
        {
            this.position.x += speed * delta;
            try
            {
                GameClient.send_to_server.writeUTF("x:" + position.x+ ", Y:" + position.y);
            } catch (IOException e) {
                System.out.println("Server not found.");
            }
        }
        else if (input.isKeyDown(Input.KEY_A) && 0 <= this.position.x + speed * delta)
        {
            this.position.x -= speed * delta;
        }

        if (input.isKeyDown(Input.KEY_W) && 0 <= this.position.y + speed * delta)
        {
            this.position.y -= speed * delta;
        }
        else if (input.isKeyDown(Input.KEY_S) && gc.getHeight() >= this.position.y + speed * delta)
        {
            this.position.y += speed * delta;
        }

        /* Code for rotation towards mouse */
        float xdist = input.getMouseX() - position.x;
        float ydist = input.getMouseY() - position.y;

        rotation = (float) Math.toDegrees(Math.atan2(ydist, xdist));
        texture.setRotation(rotation + 90f);

        if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
        {
            time -= delta;
            if (time <= 0)
            {
                Shoot();
                time = default_bullet_delay;
            }
        }

        // Add force to shots
        int i = - 1;
        while (++i < shots.size())
        {
            Bullet shot = shots.get(i);
            if (shot.position.x > gc.getWidth() || shot.position.y > gc.getHeight() || shot.position.x < - 10 || shot.position.y < - 10)
            {
                shots.remove(i);
            }
            else
            {
                shot.position.x += shotspeed * Math.sin((shot.direction * Math.PI / 180) + 90f) * delta;
                shot.position.y += shotspeed * Math.cos((shot.direction * Math.PI / 180) - 90f) * delta;
            }
        }
    }

    public void Shoot()
    {
        shots.add(new Bullet(this.position.x, this.position.y, this.rotation, true, 10f));
    }

    public void render(Graphics g)
    {
        texture.drawCentered(position.getX(), position.getY());

        DrawShots();
        g.drawString("Shots: " + shots.size(), 20, 20);
    }

    public void DrawUI()
    {
        // some user interface shit
    }

    private void DrawShots()
    {
        for (Bullet shot : shots)
        {
            bullet01.setRotation(shot.direction);
            bullet01.draw(shot.position.x, shot.position.y);
        }
    }
}
