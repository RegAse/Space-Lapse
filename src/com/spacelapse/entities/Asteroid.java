package com.spacelapse.entities;

import com.spacelapse.resourcemanager.Textures;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * Asteroid class
 * */
public class Asteroid extends Entity{

    public Asteroid(int x, int y, float speed, float health) {
        super(x, y, speed, health);
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        Image texture = Textures.getAsteroid();
        Input input = gameContainer.getInput();
        graphics.drawString(Integer.toString(this.id) + " hp: " + this.health, position.getX(), position.getY() - (texture.getHeight()/ 2) - 20);

        texture.setRotation(rotation + 90f);
        texture.drawCentered(position.getX(), position.getY());

        // Debug display collider
        //graphics.draw(new Rectangle(position.x - (texture.getWidth() / 2), position.y - (texture.getHeight() / 2), texture.getWidth(), texture.getHeight()));
    }

    @Override
    public boolean intersects(Bullet bullet) throws SlickException {
        if (bullet == null || bullet.position == null || position == null){
            return false;
        }
        Shape shape = new Rectangle(bullet.position.x, bullet.position.y, 10, 10);
        Image texture = Textures.getAsteroid();
        float textureWidth = texture.getWidth();
        float textureHeight = texture.getHeight();
        return shape.intersects(new Rectangle(position.x - (textureWidth / 2) - 10, position.y - (textureHeight / 2) - 10, textureWidth, textureHeight));
    }

    @Override
    public float applyDamage(float damage) {
        return this.health -= damage;
    }
}
