package com.spacelapse.entities;

import com.spacelapse.GameClient;
import com.spacelapse.MainMenu;
import com.spacelapse.Response;
import com.spacelapse.Survival;
import com.spacelapse.resourcemanager.Textures;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Ship extends Entity{

    protected boolean isAI;
    protected boolean hasChanged = false;
    protected float shotSpeed = 0.8f;
    protected int defaultBulletDelay = 100;
    private int time = 0;
    public Integer score = 300;

    /**
     * Constructor for Ship
     */
    public Ship(int x, int y, float speed, float health) throws SlickException {
        super(x, y, speed, health);
    }

    public float applyDamage(float damage) {
        return this.health -= damage;
    }

    public void renderShipUI(GameContainer gameContainer, Graphics graphics) {
        graphics.drawString("Score: " + score, 500, 20);
    }

    /**
     * Player control is directed here
     */
    public void Controller(GameContainer gameContainer, int delta) throws SlickException {
        hasChanged = false;
        Input input = gameContainer.getInput();

        /* Basic movement code
        *  Supports game controllers
        *  PS4
        * */
        if (((input.getControllerCount() > 0 && input.isControllerRight(0)) || input.isKeyDown(Input.KEY_D)) && gameContainer.getWidth() >= this.position.x + speed * delta) {
            this.position.x += speed * delta;
            hasChanged = true;
        }
        else if (((input.getControllerCount() > 0 && input.isControllerLeft(0)) || input.isKeyDown(Input.KEY_A)) && 0 <= this.position.x + speed * delta) {
            this.position.x -= speed * delta;
            hasChanged = true;
        }

        if (((input.getControllerCount() > 0 && input.isControllerUp(0)) || input.isKeyDown(Input.KEY_W)) && 0 <= this.position.y + speed * delta) {
            this.position.y -= speed * delta;
            hasChanged = true;
        } else if (((input.getControllerCount() > 0 && input.isControllerDown(0)) || input.isKeyDown(Input.KEY_S)) && gameContainer.getHeight() >= this.position.y + speed * delta) {
            this.position.y += speed * delta;
            hasChanged = true;
        }


        if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) || (input.getControllerCount() > 0  && input.isButtonPressed(7, 0))) {
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
     * this adds a bullet to the bullet list of a entities
     */
    public void Shoot() {
        Bullet bullet = new Bullet(this.id, this.position.x, this.position.y, 0.3f, 10f, this.rotation, 10f);
        if (GameClient.isInitialized) {
            Response response = new Response(bullet);
            response.sendData();
        }
        else {
            MainMenu.entities.add(bullet);
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        Image texture = Textures.getFighter();
        Input input = gameContainer.getInput();
        graphics.drawString(Integer.toString(this.id), position.getX(), position.getY() - (texture.getHeight()/ 2) - 20);

        texture.setRotation(rotation + 90f);
        texture.drawCentered(position.getX(), position.getY());
    }

    @Override
    public boolean intersects(Bullet bullet) throws SlickException {
        return false;
    }

    /**
     * Rotates entities towards mouse
     */
    public void rotateTowardsMouse(GameContainer gameContainer) {
        Input input = gameContainer.getInput();
        if (input.getControllerCount() > 0 && input.getAxisValue(0, 0) != 0) {
            /* for controller input */
            /* Calculates the angle of the entities according to the controllers axis */
            float newrotation = (float)Math.toDegrees(Math.atan2(input.getAxisValue(0, 0) , input.getAxisValue(0, 1)));
            if (newrotation != rotation) {
                rotation = newrotation;
                hasChanged = true;
            }
        }
        else {
            /* for mouse input */
            float xdist = input.getMouseX() - position.x;
            float ydist = input.getMouseY() - position.y;

            float newrotation = (float) Math.toDegrees(Math.atan2(ydist, xdist));
            if (newrotation != rotation) {
                rotation = newrotation;
                hasChanged = true;
            }
        }
    }

    /**
     * updatePositionToServer
     */
    public void updatePositionToServer() {
        if (GameClient.isInitialized && hasChanged) {
            Response response = new Response(this);
            response.sendData();
        }
    }

    public boolean intersects(Ship entity) throws SlickException {
        if (entity == null || entity.position == null || position == null) {
            return false;
        }
        Shape shape = new Rectangle(position.x, position.y, 10, 10);
        return shape.intersects(new Rectangle(entity.position.x, entity.position.y, 10, 10));
    }
}
