package com.spacelapse.entities;

import com.spacelapse.resourcemanager.Textures;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * Asteroid class
 * */
public class Asteroid extends Entity{

    private Vector2f target;

    public Asteroid(int x, int y, float speed, float health, float targetX, float targetY) {
        super(x, y, speed, health);
        target = new Vector2f(targetX, targetY);
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        Image texture = Textures.getAsteroid();
        Input input = gameContainer.getInput();
        //graphics.drawString(Integer.toString(this.id) + " hp: " + this.health, position.getX(), position.getY() - (texture.getHeight()/ 2) - 20);

        rotation += 0.1f;
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
        return shape.intersects(new Rectangle(position.x - (textureWidth / 2), position.y - (textureHeight / 2), textureWidth, textureHeight));
    }

    @Override
    public float applyDamage(float damage) {
        return this.health -= damage;
    }

    public void moveTowardsTarget(float speed) {
        Vector2f dir = new Vector2f();
        dir.x = target.x - position.x;
        dir.y = target.y - position.y;

        double hyp = Math.sqrt(dir.x * dir.x + dir.y * dir.y);
        dir.x /= hyp;
        dir.y /= hyp;

        position.x += dir.x * speed;
        position.y += dir.y * speed;
    }
}
