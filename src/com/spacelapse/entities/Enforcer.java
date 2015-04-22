package com.spacelapse.entities;

import com.spacelapse.GameClient;
import com.spacelapse.Response;
import com.spacelapse.resourcemanager.Textures;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Enforcer extends Ship{

    public static Image texture;

    /**
     * Constructor for Enforcer
     */
    public Enforcer(int x, int y, float speed, float health) throws SlickException {
        super(x, y, speed, health);
    }

    /**
     * Render method
     */
    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        Image texture = Textures.getEnforcer();
        Input input = gameContainer.getInput();
        graphics.drawString(Integer.toString(this.id) + " hp: " + this.health, position.getX(), position.getY() - (texture.getHeight()/ 2) - 20);

        texture.setRotation(rotation + 90f);
        texture.drawCentered(position.getX(), position.getY());

        graphics.draw(new Rectangle(position.x - (texture.getWidth() / 2), position.y - (texture.getHeight() / 2), texture.getWidth(), texture.getHeight()));
    }

    /**
     * updatePositionToServer
     */
    @Override
    public void updatePositionToServer()
    {
        if (GameClient.isInitialized && hasChanged) {
            Response response = new Response(this);
            response.sendData();
        }
    }

    @Override
    public boolean intersects(Bullet bull) throws SlickException {
        if (bull == null || bull.position == null || position == null){
            return false;
        }
        Shape shape = new Rectangle(bull.position.x, bull.position.y, 10, 10);
        Image texture = Textures.getEnforcer();
        return shape.intersects(new Rectangle(position.x - (texture.getWidth() / 2), position.y - (texture.getHeight() / 2), texture.getWidth(), texture.getHeight()));
    }
}
