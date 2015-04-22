package com.spacelapse.entities;

import com.spacelapse.entities.Bullet;
import com.spacelapse.server.GameServer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * Base class for all moving objects
 */
public abstract class Entity{

    public int id;
    public Vector2f position;
    public float speed;
    public float rotation;
    public float health;

    public Entity(float x, float y, float speed, float health) {
        this.position = new Vector2f(x, y);
        this.speed = speed;
        this.rotation = 0;
        this.health = health;

        /* Check if this a server then set a id on the entity according to the server id */
        if (GameServer.isInitialized) {
            this.id = GameServer.getNextEntityId();
        }
    }

    public abstract void render(GameContainer gameContainer, Graphics graphics) throws SlickException;

    public abstract boolean intersects(Entity entity) throws SlickException;

    public abstract float applyDamage(float damage);

    /**
     * I do this so i can remove from a list of this type
     * @param obj object
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Entity) {
            Entity entity = (Entity)obj;
            return (this.id == entity.id);
        }
        return false;
    }
}
