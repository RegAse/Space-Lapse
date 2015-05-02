package com.spacelapse.entities;

import com.spacelapse.Game;
import com.spacelapse.Survival;
import com.spacelapse.resourcemanager.Textures;
import com.spacelapse.server.GameServer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * Bullet entity class
 */
public class Bullet extends Entity{

    public float damage;
    public Integer ownerId;

    public Bullet(Integer ownerId, float x, float y, float speed, float health, float rotation, float damage) {
        super(x, y, speed, health);
        this.rotation = rotation;
        this.damage = damage;
        this.ownerId = ownerId;
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        Image bullet = Textures.getBullet();
        bullet.setRotation(rotation);
        bullet.drawCentered(position.x, position.y);
        //graphics.draw(new Rectangle(position.x, position.y, 10, 10));
    }

    @Override
    public boolean intersects(Bullet bullet) throws SlickException {
        return false;
    }

    @Override
    public float applyDamage(float damage) {
        return 0;
    }

    /**
     * Custom methods for Bullet
     */

    public void addForceToBullet(GameContainer gameContainer, int delta) {
        if (position.x > gameContainer.getWidth() || position.y > gameContainer.getHeight() || position.x < - 10 || position.y < - 10) {
            if (!GameServer.isInitialized) {
                Survival.entitiesToBeDestroyed.add(this.id);
            }
            else
            {
                GameServer.entitiesToBeDestroyed.add(this.id);
            }
        }
        else {
            position.x -= (speed * Math.sin(Math.toRadians(rotation) - 190f)) * delta;
            position.y += (speed * Math.cos(Math.toRadians(rotation) - 190f)) * delta;
        }
    }
}
