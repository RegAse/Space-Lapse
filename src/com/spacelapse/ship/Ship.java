package com.spacelapse.ship;

import com.google.gson.Gson;
import com.spacelapse.Bullet;
import com.spacelapse.GameClient;
import com.spacelapse.Response;
import com.spacelapse.resourcemanager.Textures;
import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.geom.Shape;

import java.io.IOException;
import java.util.ArrayList;

public class Ship{

    public static int ids = 0; // REMOVE LATER
    public int id;
    public Vector2f position;
    protected float speed;
    protected float rotation;
    public float health;

    protected boolean isAI;
    public ArrayList<Bullet> shots = new ArrayList<Bullet>();
    protected boolean hasChanged = false;
    protected float shotSpeed = 0.8f;
    protected int defaultBulletDelay = 100;
    private int time = 0;

    /**
     * Constructor for Ship
     */
    public Ship(int x, int y, float speed, float health) throws SlickException {
        this.position = new Vector2f(x, y);
        this.speed = speed;
        this.rotation = 0;
        this.id = ids;
        this.health = health;
        ids++;
    }

    public float applyDamage(float damage) {
        return this.health -= damage;
    }

    /**
     * Player control is directed here
     */
    public void Controller(GameContainer gameContainer, int delta) throws SlickException {
        hasChanged = false;
        Input input = gameContainer.getInput();

        /* Basic movement code */
        if (input.isKeyDown(Input.KEY_D) && gameContainer.getWidth() >= this.position.x + speed * delta) {
            this.position.x += speed * delta;
            hasChanged = true;
        }
        else if (input.isKeyDown(Input.KEY_A) && 0 <= this.position.x + speed * delta) {
            this.position.x -= speed * delta;
            hasChanged = true;
        }

        if (input.isKeyDown(Input.KEY_W) && 0 <= this.position.y + speed * delta) {
            this.position.y -= speed * delta;
            hasChanged = true;
        }
        else if (input.isKeyDown(Input.KEY_S) && gameContainer.getHeight() >= this.position.y + speed * delta) {
            this.position.y += speed * delta;
            hasChanged = true;
        }


        if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            time -= delta;
            if (time <= 0) {
                Shoot();
                time = defaultBulletDelay;
                hasChanged = true;
            }
        }
    }

    /**
     * Shoot method
     * this adds a bullet to the bullet list of a ship
     */
    public void Shoot()
    {
        shots.add(new Bullet(this.position.x, this.position.y, this.rotation, true, 10f));
    }

    /**
     * Render method
     */
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        Image texture = Textures.getFighter();
        Input input = gameContainer.getInput();
        graphics.drawString(Integer.toString(this.id), position.getX(), position.getY() - (texture.getHeight()/ 2) - 20);

        texture.setRotation(rotation + 90f);
        texture.drawCentered(position.getX(), position.getY());

        renderShots(Textures.getBullet(), graphics);
    }

    /**
     * Render shots method
     */
    protected void renderShots(Image bullet, Graphics graphics) {
        for (Bullet shot : shots) {
            bullet.setRotation(shot.direction);
            bullet.drawCentered(shot.position.x, shot.position.y);
            graphics.draw(new Rectangle(shot.position.x, shot.position.y, 10, 10));
        }
    }

    /**
     * Rotates ship towards mouse
     */
    public void rotateTowardsMouse(GameContainer gameContainer)
    {
        Input input = gameContainer.getInput();
        float xdist = input.getMouseX() - position.x;
        float ydist = input.getMouseY() - position.y;

        float newrotation = (float) Math.toDegrees(Math.atan2(ydist, xdist));
        if (newrotation != rotation)
        {
            rotation = newrotation;
            hasChanged = true;
        }
    }

    /**
     * Add force to bullets
     */
    public void addForceToBullets(GameContainer gameContainer, int delta) {
        // Add force to shots
        int i = - 1;
        while (++i < shots.size()) {
            Bullet shot = shots.get(i);
            if (shot.position.x > gameContainer.getWidth() || shot.position.y > gameContainer.getHeight() || shot.position.x < - 10 || shot.position.y < - 10) {
                shots.remove(i);
            }
            else {
                shot.position.x -= (shotSpeed * Math.sin(Math.toRadians(shot.direction) - 190f)) * delta;
                shot.position.y += (shotSpeed * Math.cos(Math.toRadians(shot.direction) - 190f)) * delta;
            }
        }
    }

    /**
     * updatePositionToServer
     */
    public void updatePositionToServer()
    {
        if (GameClient.isInitialized && hasChanged) {
            Response response = new Response(this);
            response.sendData();
        }
    }

    public boolean intersects(Ship entity) throws SlickException {
        if (entity == null || entity.position == null || position == null){
            return false;
        }
        Shape shape = new Rectangle(position.x, position.y, 10, 10);
        return shape.intersects(new Rectangle(entity.position.x, entity.position.y, 10, 10));
    }

    public boolean intersects(Bullet bullet) throws SlickException {
        if (bullet == null || bullet.position == null || position == null){
            return false;
        }
        Shape shape = new Rectangle(position.x, position.y, 10, 10);
        return shape.intersects(new Rectangle(bullet.position.x, bullet.position.y, 10, 10));
    }
}
